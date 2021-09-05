#ifndef ALARM_H
#define ALARM_H

#include <Time.h>

/**
 * @brief Data class used to store an Alarm
 *
 */
class Alarm {
   public:
    /**
     * @brief Construct a new default Alarm object
     *
     */
    Alarm();

    /**
     * @brief Construct a new specific Alarm object
     *
     * @param enabled Is the alarm on or off
     * @param startTime The Start time of the alarm
     * @param duration The duration of the alarm (in minutes)
     */
    Alarm(bool enabled, Time startTime, unsigned int duration);

    // Definition of == operator
    bool operator==(const Alarm& other) const { return this->equals(other); }

    // Definition of != operator
    bool operator!=(const Alarm& other) const { return !this->equals(other); }

    // All of these members are public because this is a data class

    bool enabled;
    Time startTime;
    unsigned int duration;  // minutes

   private:
    /**
     * @brief Defines equality between current Alarm instance and another Alarm instance
     *
     * @param other The other Alarm instance to compare with
     * @return true If both instances are equal
     * @return false If they are not equal
     */
    bool equals(const Alarm& other) const;
};

#endif