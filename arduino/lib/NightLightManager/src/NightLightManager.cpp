#include <NightLightManager.h>

NightLightManager::NightLightManager(TimePublisher& timePublisher, LightManager& lightManager)
    : timePublisher(timePublisher),
      lightManager(lightManager),
      active(false),
      enabled(false),
      intensity(NightLightManager::defaultIntensity),
      previousIntensity(0),
      startTime(Time(0, 0)),
      endTime(Time(1, 0)) {}

void NightLightManager::updateDatetime(tm& datetime) {
    Time current(datetime.tm_hour, datetime.tm_min);
    bool startOfMinute = datetime.tm_sec == 0 || datetime.tm_sec == 1;

    this->mutex.lock();

    if (this->enabled && this->startTime == current && startOfMinute && !this->active) {
        // Set internal state
        this->active = true;

        if (this->lightManager.getCurrentMode() == LightModeAlarm) {
            this->previousIntensity = NightLightManager::defaultIntensity;
        } else {
            this->previousIntensity = this->lightManager.getIntensity();
            this->lightManager.setIntensity(this->intensity);
        }

    } else if (this->active && this->endTime == current && startOfMinute) {
        // Communicate with the light manager if no alarms
        if (this->lightManager.getCurrentMode() == LightModeNormal) {
            this->lightManager.setIntensity(this->previousIntensity);
        }

        // Set back the internal state
        this->active = false;
        this->previousIntensity = 0;
    }

    this->mutex.unlock();
}

void NightLightManager::setEnabled(bool enabled) {
    this->mutex.lock();

    this->enabled = enabled;
    if (enabled) {
        this->timePublisher.subscribe(*this);
    } else {
        this->timePublisher.unsubscribe(*this);
    }

    this->mutex.unlock();
}

void NightLightManager::setIntensity(unsigned short intensity) {
    if (intensity > 100) {
        return;
    }

    this->mutex.lock();
    this->intensity = intensity;
    this->mutex.unlock();
}

void NightLightManager::setStartTime(Time startTime) {
    this->mutex.lock();
    this->startTime = startTime;
    this->mutex.unlock();
}
void NightLightManager::setEndTime(Time endTime) {
    this->mutex.lock();
    this->endTime = endTime;
    this->mutex.unlock();
}