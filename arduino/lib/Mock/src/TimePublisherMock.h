#ifndef TIME_PUBLISHER_MOCK_H
#define TIME_PUBLISHER_MOCK_H

#include <TimeSubscriber.h>

class TimePublisherMock : public TimePublisher {
   public:
    void subscribe(TimeSubscriber& subscriber) override{};

    virtual void unsubscribe(TimeSubscriber& subscriber) override{};
};

#endif