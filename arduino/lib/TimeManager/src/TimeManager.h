#ifndef TIME_MANAGER_H
#define TIME_MANAGER_H

#include <TimeSubscriber.h>
#include <mbed.h>
#include <mbed_events.h>
#include <rtos.h>

#include <set>

/**
 * @brief The TimeManager class is in charge of managing who wants to know about time changes
 *
 */
class TimeManager : public TimePublisher {
   public:
    /**
     * @brief Construct a new Time Manager object
     *
     */
    TimeManager();

    // We don't want to allow copy or assignment
    TimeManager(const TimeManager& other) = delete;
    TimeManager& operator=(const TimeManager& other) = delete;

    /**
     * @brief Get the queue of the TimeManager instance
     *
     * @return events::EventQueue* the job queue
     */
    events::EventQueue* getQueue() { return &this->queue; }

    // Needs to be public to access it through pointer returned from getQueue
    events::EventQueue queue;

    // Publisher
    void subscribe(TimeSubscriber& subscriber) override;
    void unsubscribe(TimeSubscriber& subscriber) override;

    // Static methods

    /**
     * @brief Fills a tm struct with the given data
     *
     * @param year The year to set. Years below 1900 are not supported
     * @param month The month to set. Starts at 1 and goes up to 12.
     * @param day The day to set. Starts at 1 and goes up to 31.
     * @param hour The hour to set
     * @param minute The minute to set
     * @param second The second to set
     */
    static tm toDeviceTime(int year, int month, int day, int hour, int minute, int second);

    /**
     * @brief Sets the Date Time object
     *
     * @note As per mbed_rtc_time.h: Synchronization level: Thread safe
     *
     * @param year The year to set. Years below 1900 are not supported
     * @param month The month to set. Starts at 1 and goes up to 12.
     * @param day The day to set. Starts at 1 and goes up to 31.
     * @param hour The hour to set
     * @param minute The minute to set
     * @param second The second to set
     */
    static void setDateTime(int year, int month, int day, int hour, int minute, int second);

    /**
     * @brief Get the Date Time object
     *
     * @return tm& The datetime as returned by the device's clock
     */
    static tm& getDateTime();

    /**
     * @brief Get the Formatted Date Time object
     *
     * @return char* The formatted "string" version of the datetime
     */
    static char* getFormattedDateTime();

   private:
    /**
     * @brief This mutex is used to protect subscribers from concurrent access.
     *
     */
    rtos::Mutex mutex;

    /**
     * @brief The set of current TimeSubscribers
     *
     */
    std::set<TimeSubscriber*> subscribers;

    /**
     * @brief Frequency at which we want to notify our subscribers.
     *
     */
    std::chrono::seconds updateFrequency;

    /**
     * @brief Id of the job that will notify our subscribers. Used to cancel the job once no
     * subscribers are left.
     *
     * Per EventQueue documentation, value 0 means that no current job.
     *
     */
    int timeJobId;

    /**
     * @brief Static method used to notify all our subscribers.
     *
     * @param instance The instance on which to notify subscribers
     */
    static void notifySubscribers(TimeManager* instance);
};

#endif
