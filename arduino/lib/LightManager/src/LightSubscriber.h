#ifndef LIGHT_SUBSCRIBER_H
#define LIGHT_SUBSCRIBER_H

/**
 * @brief LightSubscriber is an abstract class representing an object which would like to be
 * notified of:
 * 1. the intensity of the light
 * 2. the activity of the light (turned on or off)
 *
 * when they change.
 */
class LightSubscriber {
   public:
    virtual ~LightSubscriber(){};

    /**
     * @brief Method called on a LightSubscriber by a LightPublisher when the intensity of the Light
     * has changed.
     *
     * @param intensity The intensity given by the LightPublisher
     */
    virtual void updateIntensity(unsigned short intensity) = 0;

    /**
     * @brief Method called on a LightSubscriber by a LightPublisher when the activity of the Light
     * has changed.
     *
     * @param on Is the light on or not.
     */
    virtual void updateActive(bool on) = 0;
};

/**
 * @brief LightPublisher is an abstract class representing an object which can provide it's
 * subscribers data on the state of a lamp.
 *
 */
class LightPublisher {
   public:
    virtual ~LightPublisher(){};

    /**
     * @brief Method used by a LightSubscriber to start being notified by a LightPublisher
     *
     * @param subscriber The subscriber to add to the notification list.
     */
    virtual void subscribe(LightSubscriber& subscriber) = 0;

    /**
     * @brief Method used by a LightSubscriber to stop being notified by a LightPublisher
     *
     * @param subscriber The subscriber to remove from the notification list.
     */
    virtual void unsubscribe(LightSubscriber& subscriber) = 0;
};

#endif