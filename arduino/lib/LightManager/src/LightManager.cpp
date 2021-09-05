#include <Arduino.h>
#include <LightManager.h>
#include <math.h>

const long buttonDebounce = 500L;
long lastButtonPressed = 0L;

// TODO: For now, assume that setup was made correctly and that we will never
// have a nullptr when any function are called...
LightManager* LightManager::instance = nullptr;

LightManager::LightManager(int pwmPin, int zcPin, int toggleButtonPin)
    : pwmPin(pwmPin),
      zcPin(zcPin),
      toggleButtonPin(toggleButtonPin),
      intensity(0),
      turnedOn(false),
      lightMode(LightModeNormal) {
    // Setup pins
    pinMode(this->pwmPin, OUTPUT);
    pinMode(this->zcPin, INPUT_PULLUP);
    pinMode(this->toggleButtonPin, INPUT_PULLUP);
    attachInterrupt(this->toggleButtonPin, buttonInt, FALLING);
}

void LightManager::setInstance(LightManager& newInstance) { LightManager::instance = &newInstance; }

LightManager* LightManager::getInstance() { return LightManager::instance; }

events::EventQueue* LightManager::getQueue() {
    if (LightManager::instance == nullptr) {
        return nullptr;
    }

    return &LightManager::instance->queue;
}

void LightManager::zeroCrossingInt() {
    LightManager* instance = LightManager::getInstance();

    instance->queue.call(LightManager::dim, instance);
}

void LightManager::dim(LightManager* instance) {
    if (instance == nullptr) {
        return;
    }

    // instance->mutex.lock();
    int intensity = instance->intensity;
    int lowTime = instance->lowTime;
    // instance->mutex.unlock();

    // Clean 0%
    if (intensity == 0) {
        digitalWrite(instance->pwmPin, LOW);
        return;
    }

    digitalWrite(instance->pwmPin, LOW);

    // Looks like we cannot sleep for less than a millisecond. Recommended usage
    // is wait_us().
    //
    // https://os.mbed.com/docs/mbed-os/v6.10/mbed-os-api-doxy/namespacertos_1_1_this_thread.html
    // https://os.mbed.com/forum/mbed/topic/3973/
    wait_us(lowTime);

    digitalWrite(instance->pwmPin, HIGH);
}

void LightManager::buttonInt() {
    long current = millis();

    if (current > lastButtonPressed + buttonDebounce) {
        lastButtonPressed = current;

        LightManager* instance = LightManager::getInstance();
        instance->queue.call(LightManager::toggle, instance);
    }
}

void LightManager::internalTurnOn() {
    attachInterrupt(this->zcPin, zeroCrossingInt, RISING);
    this->turnedOn = true;

    // notify subscribers
    // Subscribers should *not* use LightManager -> deadlock
    for (const auto sub : this->subscribers) {
        sub->updateActive(this->turnedOn);
    }
}

void LightManager::turnOn() {
    this->mutex.lock();

    this->internalTurnOn();

    this->mutex.unlock();
}

void LightManager::internalTurnOff() {
    detachInterrupt(this->zcPin);
    this->turnedOn = false;

    // Make sure that we stop the alarm
    this->lightMode = LightModeNormal;

    digitalWrite(this->pwmPin, LOW);

    // notify subscribers
    // Subscribers should *not* use LightManager -> deadlock
    for (const auto sub : this->subscribers) {
        sub->updateActive(this->turnedOn);
    }
}

void LightManager::turnOff() {
    this->mutex.lock();

    this->internalTurnOff();

    this->mutex.unlock();
}

void LightManager::toggle(LightManager* instance) {
    if (instance == nullptr) {
        return;
    }

    instance->mutex.lock();

    if (instance->turnedOn) {
        instance->internalTurnOff();
    } else {
        instance->internalTurnOn();
    }

    instance->mutex.unlock();
}

bool LightManager::setIntensity(unsigned short intensity, LightMode mode) {
    if (intensity > 100) return false;

    this->mutex.lock();

    if (this->lightMode == LightModeAlarm && mode == LightModeNormal) {
        this->lightMode = LightModeNormal;
    } else if (this->lightMode == LightModeNormal && mode == LightModeAlarm) {
        this->mutex.unlock();
        return true;
    }

    this->intensity = intensity;

    double variation = 0.5 * intensity;
    double power = 100 - variation;
    // range is 3750 - 7500 us
    this->lowTime = round(75 * power);  // microseconds

    // notify subscribers
    std::set<LightSubscriber*> subscribers = this->subscribers;
    this->mutex.unlock();

    for (const auto sub : subscribers) {
        sub->updateIntensity(intensity);
    }

    return true;
}

unsigned short LightManager::getIntensity() {
    this->mutex.lock();
    int intensity = this->intensity;
    this->mutex.unlock();

    return intensity;
}

void LightManager::turnAlarmModeOn() {
    this->mutex.lock();
    this->lightMode = LightModeAlarm;
    this->turnOn();
    this->mutex.unlock();
}

void LightManager::turnAlarmModeOff() {
    this->mutex.lock();
    this->lightMode = LightModeNormal;
    this->mutex.unlock();
}

bool LightManager::isTurnedOn() {
    this->mutex.lock();
    int turnedOn = this->turnedOn;
    this->mutex.unlock();

    return turnedOn;
}

LightMode LightManager::getCurrentMode() {
    this->mutex.lock();
    LightMode mode = this->lightMode;
    this->mutex.unlock();

    return mode;
}

// Publisher
void LightManager::subscribe(LightSubscriber& subscriber) {
    this->mutex.lock();
    this->subscribers.insert(&subscriber);
    this->mutex.unlock();
}

void LightManager::unsubscribe(LightSubscriber& subscriber) {
    this->mutex.lock();
    this->subscribers.erase(&subscriber);
    this->mutex.unlock();
}