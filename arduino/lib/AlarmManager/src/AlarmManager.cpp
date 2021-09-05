#include <AlarmManager.h>
#include <Time.h>

AlarmManager::AlarmManager(TimePublisher& timePublisher, LightManager& lightManager)
    : timePublisher(timePublisher),
      lightManager(lightManager),
      inAlarm(false),
      countdown(0),
      initialDuration(0) {
    this->alarms = {};

    // Automatically subscribe
    this->timePublisher.subscribe(*this);
}

void AlarmManager::updateDatetime(tm& datetime) {
    // We don't know about the day of the week => skip
    if (datetime.tm_wday == 7) {
        return;
    }

    this->mutex.lock();

    int day = (datetime.tm_wday + 6) % 7;  // For us, monday is 0, and sunday is 7
    Alarm& alarm = this->alarms[day];
    Time current(datetime.tm_hour, datetime.tm_min);

    // Only start alarms at the beginning of the minute
    // Allow a bit of leeway in case we are not triggered exactly on the minute
    bool startOfMinute = datetime.tm_sec == 0 || datetime.tm_sec == 1;

    if (alarm.enabled && alarm.startTime == current && startOfMinute && !this->inAlarm) {
        // Set internal state
        this->inAlarm = true;
        this->initialDuration = alarm.duration * 60;  // Countdown is in seconds
        this->countdown = this->initialDuration;

        // Set LightManager state
        this->lightManager.turnAlarmModeOn();
    }

    if (this->inAlarm) {
        this->mutex.unlock();
        this->step();
    } else {
        this->mutex.unlock();
    }
}

void AlarmManager::step() {
    this->mutex.lock();

    // minimize amount of time in mutex
    unsigned int countdown = this->countdown;
    unsigned int initialDuration = this->initialDuration;

    // perform step in mutex
    this->countdown--;

    this->mutex.unlock();

    unsigned int intensity = computeIntensity(countdown, initialDuration);
    lightManager.setIntensity(intensity, LightModeAlarm);

    if (countdown == 0) {
        this->resetState();
    }
}

void AlarmManager::resetState() {
    this->mutex.lock();

    // Reset internal state
    this->inAlarm = false;
    this->initialDuration = 0;

    this->mutex.unlock();

    // Reset lightManager state
    this->lightManager.turnAlarmModeOff();
}

void AlarmManager::setAlarm(Weekday day, Alarm alarm) {
    this->mutex.lock();

    this->alarms[day] = alarm;

    this->mutex.unlock();
}

Alarm AlarmManager::getAlarm(Weekday day) {
    this->mutex.lock();

    Alarm alarm = this->alarms[day];
    this->mutex.unlock();

    return alarm;
}

bool AlarmManager::isInAlarmState() {
    this->mutex.lock();
    bool inAlarm = this->inAlarm;
    this->mutex.unlock();

    return inAlarm;
}

unsigned int AlarmManager::computeIntensity(unsigned int countdown, unsigned int initialDuration) {
    double ratio = static_cast<double>(countdown) / static_cast<double>(initialDuration);
    return round((1 - ratio) * 100);
}