#ifndef ALARM_PROVIDER_H
#define ALARM_PROVIDER_H

#include <Alarm.h>
#include <Weekday.h>

/**
 * @brief AlarmProvider is an abstract class representing an object that can manage alarms
 *
 */
class AlarmProvider {
   public:
    virtual ~AlarmProvider(){};

    /**
     * @brief Set the value of an alarm for a specific day
     *
     * @param day The day for which to set the alarm
     * @param alarm The alarm to set
     */
    virtual void setAlarm(Weekday day, Alarm alarm) = 0;

    /**
     * @brief Retrieve the alarm for a specific day
     *
     * @param day The day for which to get the alarm
     * @return Alarm The alarm for the corresponding day
     */
    virtual Alarm getAlarm(Weekday day) = 0;

    /**
     * @brief Used to know if the system is currently in an alarm
     *
     * @return true During an alarm
     * @return false In a normal state
     */
    virtual bool isInAlarmState() = 0;
};

#endif