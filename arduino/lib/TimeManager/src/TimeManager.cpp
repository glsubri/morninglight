#include "TimeManager.h"

#include <Arduino.h>
#include <utils.h>

TimeManager::TimeManager() : updateFrequency(1), timeJobId(0) {}

void TimeManager::subscribe(TimeSubscriber& subscriber) {
    this->mutex.lock();

    this->subscribers.insert(&subscriber);

    // per EventQueue documentation, a 0 value means no job
    if (this->timeJobId == 0) {
        // Save job id to cancel later
        this->timeJobId =
            this->queue.call_every(this->updateFrequency, TimeManager::notifySubscribers, this);
    }

    this->mutex.unlock();
}

void TimeManager::unsubscribe(TimeSubscriber& subscriber) {
    this->mutex.lock();

    this->subscribers.erase(&subscriber);

    // If nobody is subscribed, cancel job
    // per EventQueue documentation, a 0 value means no job
    if (this->subscribers.size() == 0 && this->timeJobId != 0) {
        this->queue.cancel(this->timeJobId);
        this->timeJobId = 0;
    }

    this->mutex.unlock();
}

// Static
void TimeManager::notifySubscribers(TimeManager* instance) {
    if (instance == nullptr) {
        return;
    }

    tm& datetime = getDateTime();

    instance->mutex.lock();

    for (auto sub : instance->subscribers) {
        sub->updateDatetime(datetime);
    }

    instance->mutex.unlock();
}

tm TimeManager::toDeviceTime(int year, int month, int day, int hour, int minute, int second) {
    tm datetime = {};
    year = std::max(year - 1900, 0);

    if (inBounds(month, 1, 12) && inBounds(day, 1, 31) && inBounds(hour, 0, 23) &&
        inBounds(minute, 0, 59) && inBounds(second, 0, 59)) {
        datetime = {
            .tm_sec = second,     // 0 - 59
            .tm_min = minute,     // 0 - 59
            .tm_hour = hour,      // 0 - 23
            .tm_mday = day,       // 1 - 31
            .tm_mon = month - 1,  // 0 - 11
            .tm_year = year       // number of years since 1900
        };
    }

    return datetime;
}

void TimeManager::setDateTime(int year, int month, int day, int hour, int minute, int second) {
    struct tm newTime = TimeManager::toDeviceTime(year, month, day, hour, minute, second);
    set_time(mktime(&newTime));
}

tm& TimeManager::getDateTime() {
    time_t seconds = time(NULL);
    return *gmtime(&seconds);
}

char* TimeManager::getFormattedDateTime() {
    tm datetime = getDateTime();
    return asctime(&datetime);
}
