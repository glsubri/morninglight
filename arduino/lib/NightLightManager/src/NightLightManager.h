#ifndef NIGHT_LIGHT_MANAGER_H
#define NIGHT_LIGHT_MANAGER_H

#include <LightManager.h>
#include <Time.h>
#include <TimeSubscriber.h>
#include <rtos.h>
#include <time.h>

/**
 * @brief AlarmManager is responsible to manage the state of the different alarms in the system
 *
 */
class NightLightManager : public TimeSubscriber {
   public:
    /**
     * @brief Construct a new Alarm Manager object
     *
     * @param timePublisher The time publisher used to subscribe to the time
     * @param lightManager The light manger used to communicate with the lamp
     */
    NightLightManager(TimePublisher& timePublisher, LightManager& lightManager);

    // TimeSubscriber
    void updateDatetime(tm& datetime) override;

    /**
     * @brief Set the NightLight setting on or off
     *
     * @param enabled Is true if the NightLight should be on
     */
    void setEnabled(bool enabled);

    /**
     * @brief Set the desired intensity of the NightLight setting
     *
     * @param intensity The desired intensity, bounded between [0-100].
     */
    void setIntensity(unsigned short intensity);

    /**
     * @brief Set the Start Time of the NightLight setting
     *
     * @param startTime The moment at which the NightLight setting should be enabled
     */
    void setStartTime(Time startTime);

    /**
     * @brief Set the End Time of the NightLight setting
     *
     * @param endTime The moment at which the NightLight setting should stop and restore the
     * original intensity
     */
    void setEndTime(Time endTime);

    static constexpr int defaultIntensity = 15;

   private:
    /**
     * @brief Mutex used to protect concurrent access
     *
     */
    rtos::Mutex mutex;

    TimePublisher& timePublisher;
    LightManager& lightManager;

    /**
     * @brief Is the NightLight setting enabled or not
     *
     */
    bool enabled;

    /**
     * @brief The intensity to set at the beggining of the NightLight setting
     *
     */
    unsigned short intensity;

    /**
     * @brief Time at which the NightLight setting should be started
     *
     */
    Time startTime;

    /**
     * @brief Time at which we should stop the NightLight setting
     *
     */
    Time endTime;

    /**
     * @brief Used to track if we are currently in an NightLight setting
     *
     */
    bool active;

    /**
     * @brief Used to set back the intensity of the lamp when we go out of the NightLight setting
     *
     */
    unsigned short previousIntensity;
};

#endif