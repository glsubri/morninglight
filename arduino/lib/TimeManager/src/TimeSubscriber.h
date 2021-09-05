#ifndef TIME_SUBSCRIBER_H
#define TIME_SUBSCRIBER_H

#include <time.h>

/**
 * @brief TimeSubscriber is an abstract class representing an object which would like to be notified
 * of the time when it changes.
 */
class TimeSubscriber {
   public:
    virtual ~TimeSubscriber(){};

    /**
     * @brief Method used by a TimePublisher to update a subscriber's datetime.
     *
     * @param datetime The datetime given by the publisher.
     */
    virtual void updateDatetime(tm& datetime) = 0;
};

/**
 * @brief TimePublisher is an abstract class representing an object which can provide it's
 * subscribers the current datetime.
 *
 */
class TimePublisher {
   public:
    virtual ~TimePublisher(){};

    /**
     * @brief Method used by a TimeSubscriber to start being notified by a TimePublisher
     *
     * @param subscriber The subscriber to add to the notification list.
     */
    virtual void subscribe(TimeSubscriber& subscriber) = 0;

    /**
     * @brief Method used by a TimeSubscriber to stop being notified by a TimePublisher
     *
     * @param subscriber The subscriber to remove from the notification list.
     */
    virtual void unsubscribe(TimeSubscriber& subscriber) = 0;
};

#endif