#ifndef ALARM_MANAGER_H
#define ALARM_MANAGER_H

#include <Alarm.h>
#include <AlarmProvider.h>
#include <LightManager.h>
#include <TimeManager.h>
#include <TimeSubscriber.h>
#include <rtos.h>
#include <time.h>

#include <array>

/**
 * @brief AlarmManager is responsible to manage the state of the different alarms in the system
 *
 */
class AlarmManager : public TimeSubscriber, public AlarmProvider {
   public:
    /**
     * @brief Construct a new Alarm Manager object
     *
     * @param timePublisher The time publisher used to subscribe to the time
     * @param lightManager The light manger used to communicate with the lamp
     */
    AlarmManager(TimePublisher& timePublisher, LightManager& lightManager);

    // TimeSubscriber
    void updateDatetime(tm& datetime) override;

    // Alarm Provider
    void setAlarm(Weekday day, Alarm alarm) override;
    Alarm getAlarm(Weekday day) override;
    bool isInAlarmState() override;

   private:
    /**
     * @brief Mutex used to protect concurrent alarms access
     *
     */
    rtos::Mutex mutex;

    TimePublisher& timePublisher;
    LightManager& lightManager;

    /**
     * @brief Array of alarms, one per day
     *
     */
    std::array<Alarm, 7> alarms;

    /**
     * @brief Are we in an alarm state or not
     *
     */
    bool inAlarm;

    /**
     * @brief Used to track the amount of seconds left in the alarm state
     *
     */
    unsigned int countdown;

    /**
     * @brief Initial duration of the alarm, in seconds
     *
     */
    unsigned int initialDuration;

    /**
     * @brief Used to execute a "setp" in the alarm mechanisme. Should be executed every second
     *
     */
    void step();

    /**
     * @brief Method used to reset coherent state when an alarm is finished
     *
     */
    void resetState();

    /**
     * @brief Static method used to know the intensity to set the light to
     *
     * @param countdown The amount of seconds left in alarm state
     * @param initialDuration The initial duration (in seconds) of the alarm
     * @return unsigned int The intensity corresponding to our progression in the alarm
     */
    static unsigned int computeIntensity(unsigned int countdown, unsigned int initialDuration);
};

#endif