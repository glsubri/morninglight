#ifndef LIGHT_MANAGER_H
#define LIGHT_MANAGER_H

#include <LightSubscriber.h>
#include <mbed.h>
#include <mbed_events.h>
#include <rtos.h>

#include <set>

/**
 * @brief LightMode is used to represent the different state the light can be in.
 *
 * normal: signifies that the lamp can be freely controlled by the user
 * alarm: signifies that the lamp is in the middle of an alarm. If the users sends commands, it
 * will cancel the alarm
 *
 */
enum LightMode { LightModeNormal, LightModeAlarm };

/**
 * @brief This class manages a lamp. It can do some operations on it such as turn it on or off,
 * change the intensity and so forth...
 *
 */
class LightManager : public LightPublisher {
   public:
    /**
     * @brief Construct a new Light Manager object
     *
     * @param pwmPin The PWM pin
     * @param zcPin The zero crossing pin
     * @param toggleButtonPin The pin used by a button to turn the light on or off
     */
    LightManager(int pwmPin, int zcPin, int toggleButtonPin);

    /**
     * @brief Queue of operations to do
     *
     */
    events::EventQueue queue;

    /**
     * @brief Turns the lamp on.
     *
     * The lamp will have the intensity given by @see LightManager#getIntensity
     *
     */
    void turnOn();

    /**
     * @brief Turns the light off.
     *
     */
    void turnOff();

    /**
     * @brief Sets the intensity of the lamp.
     *
     * Note that this will not turn the light on, only set the intensity for when it is turned on.
     *
     * @param intensity The intensity of the lamp. Bounded by [0, 100].
     * @param mode The mode in which the lamp should take the command.
     * @return true If the intensity was correctly set.
     * @return false If the given intensity was not inside the boudaries.
     */
    bool setIntensity(unsigned short intensity, LightMode mode = LightModeNormal);

    /**
     * @brief Get the current intensity of the lamp
     *
     * @return short The intensity of the lamp. Bounded by [0, 100]
     */
    unsigned short getIntensity();

    /**
     * @brief Is the light currently on or off.
     *
     * @return true If the light is on.
     * @return false If the light is off.
     */
    bool isTurnedOn();

    /**
     * @brief Sets the lamp in Alarm mode.
     *
     */
    void turnAlarmModeOn();

    /**
     * @brief Make the lamp go back to it's normal mode
     *
     */
    void turnAlarmModeOff();

    /**
     * @brief Get the current mode of the lamp manager
     *
     * @return LightMode The mode in which the lamp manager is in
     */
    LightMode getCurrentMode();

    // Publisher
    void subscribe(LightSubscriber& subscriber) override;
    void unsubscribe(LightSubscriber& subscriber) override;

    /**
     * @brief Sets which LightManager will be used by the zero crossing interruption.
     *
     */
    static void setInstance(LightManager&);

    /**
     * @brief Get the current saved instance.
     *
     * @return LightManager* A pointer on the current saved instance.
     */
    static LightManager* getInstance();

    /**
     * @brief Get the EventQueue of the current saved instance.
     *
     * @return events::EventQueue*
     */
    static events::EventQueue* getQueue();

   private:
    // The saved instance of LightManager
    static LightManager* instance;

    /**
     * @brief A mutex which protects intensity, turnedOn, and subscribers from concurrent access.
     *
     */
    rtos::Mutex mutex;

    /**
     * @brief The PWM pin
     *
     */
    int pwmPin;

    /**
     * @brief The Zero Crossing pin to use to trigger interruptions.
     *
     */
    int zcPin;

    /**
     * @brief The pin used to trigger interruptions to turn the light on or off
     *
     */
    int toggleButtonPin;

    /**
     * @brief The current intensity of the lamp.
     *
     */
    unsigned short intensity;

    /**
     * @brief Value used to calculate the amount of downtime for the PWM
     *
     */
    int lowTime;

    /**
     * @brief Is the light currently on or off.
     *
     */
    bool turnedOn;

    /**
     * @brief The current mode of the alarm.
     *
     */
    LightMode lightMode;

    /**
     * @brief Set of subscribers
     *
     */
    std::set<LightSubscriber*> subscribers;

    /**
     * @brief Helper method used to turn on the lamp.
     * Note: this method is not mutex-protected
     *
     */
    void internalTurnOn();

    /**
     * @brief Helper method used to turn the lamp off.
     * Note: this method is not mutex-protected
     *
     */
    void internalTurnOff();

    /**
     * @brief Static function used to dim the lamp.
     *
     * This is ran in the thread processing the event queue. Should be as close to RealTime as
     * possible.
     *
     * @param instance the instance of LightManager to use
     */
    static void dim(LightManager* instance);

    /**
     * @brief Interrupt function triggered on a zero crossing from the zcPin.
     *
     */
    static void zeroCrossingInt();

    /**
     * @brief Interrupt functoin triggered when the user has pressed on the external button.
     *
     */
    static void buttonInt();

    /**
     * @brief Turns the light on if it is off or off if it is on.
     *
     * This is ran in the thread processing the event queue. Should be as close to RealTime as
     * possible.
     *
     * @param instance the instance of LightManager to use
     */
    static void toggle(LightManager* instance);
};

#endif