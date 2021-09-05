#include <Alarm.h>

Alarm::Alarm() : Alarm(false, Time(), 1) {}

Alarm::Alarm(bool enabled, Time startTime, unsigned int duration)
    : enabled(enabled), startTime(startTime), duration(duration) {}

bool Alarm::equals(const Alarm& other) const {
    return this->duration == other.duration && this->enabled == other.enabled &&
           this->startTime == other.startTime;
}