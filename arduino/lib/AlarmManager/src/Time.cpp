#include <Time.h>

Time::Time() : Time(0, 0) {}
Time::Time(unsigned short hour, unsigned short minute)
    : hour(inBoundsOrDefault(hour, 23)), minute(inBoundsOrDefault(minute, 59)) {}

bool Time::equals(const Time& other) const {
    return this->hour == other.hour && this->minute == other.minute;
}

unsigned short Time::inBoundsOrDefault(unsigned short n, unsigned short upperBound,
                                       unsigned short defaultValue) {
    if (n > upperBound) {
        return defaultValue;
    } else {
        return n;
    }
}

Time Time::fromBytes(const uint8_t* bytes) {
    unsigned short hour = static_cast<unsigned short>(bytes[0]);
    unsigned short minute = static_cast<unsigned short>(bytes[1]);

    Time time(hour, minute);
    return time;
}