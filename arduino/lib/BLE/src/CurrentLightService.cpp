#include <CurrentLightService.h>

const std::string INTENSITY_UUID = "00000001" + SERVICE_CURRENT_LIGHT_UUID;
const uint8_t INTENSITY_PROP = BLERead | BLEWrite | BLENotify;

const std::string ACTIVE_UUID = "00000002" + SERVICE_CURRENT_LIGHT_UUID;
const uint8_t ACTIVE_PROP = BLERead | BLEWrite | BLENotify;

CurrentLightService::CurrentLightService()
    : service(SERVICE_CURRENT_LIGHT_UUID.c_str()),
      intensity(INTENSITY_UUID.c_str(), INTENSITY_PROP),
      active(ACTIVE_UUID.c_str(), ACTIVE_PROP) {
    // Setup characteristics in service
    this->service.addCharacteristic(this->intensity);
    this->service.addCharacteristic(this->active);

    // Advertise BLE service
    BLE.setAdvertisedService(this->service);
    BLE.addService(this->service);

    // Setup callbacks
    this->intensity.setEventHandler(BLERead, onIntensityRead);
    this->intensity.setEventHandler(BLEWritten, onIntensityWrite);
    this->intensity.setEventHandler(BLESubscribed, onIntensitySubscribe);
    this->intensity.setEventHandler(BLEUnsubscribed, onIntensityUnsubscribe);

    this->active.setEventHandler(BLERead, onActiveRead);
    this->active.setEventHandler(BLEWritten, onActiveWrite);
    this->active.setEventHandler(BLESubscribed, onActiveSubscribe);
    this->active.setEventHandler(BLEUnsubscribed, onActiveUnsubscribe);
}

CurrentLightService& CurrentLightService::getInstance() {
    static CurrentLightService instance;
    return instance;
}

events::EventQueue* CurrentLightService::getQueue() { return &getInstance().queue; }

void CurrentLightService::setLightManager(LightManager& lightManager) {
    if (this->lightManager != nullptr) {
        this->lightManager->unsubscribe(*this);
    }

    this->lightManager = &lightManager;
    this->lightManager->subscribe(*this);
}

// Subscriber

void CurrentLightService::updateIntensity(unsigned short intensity) {
    this->intensity.setValue(intensity);
}

void CurrentLightService::updateActive(bool on) { this->active.setValue(on); }

// We internally depend on only one subscription but we expose two of them: one on intensity and one
// on active. This is why we must manage our own subscription correctly
bool CurrentLightService::isSubscribed() { return this->subActive || this->subIntensity; }

// Static handlers
void CurrentLightService::onIntensityRead(BLEDevice central, BLECharacteristic characteristic) {
    CurrentLightService* instance = &CurrentLightService::getInstance();

    if (instance->lightManager == nullptr) {
        return;
    }

    instance->intensity.writeValue(instance->lightManager->getIntensity());
}

void CurrentLightService::onIntensityWrite(BLEDevice central, BLECharacteristic characteristic) {
    CurrentLightService* instance = &CurrentLightService::getInstance();

    if (instance->lightManager == nullptr) {
        return;
    }

    const uint8_t* buf = characteristic.value();
    unsigned short intensity = buf[0];

    if (intensity > 100) {
        return;
    }

    instance->lightManager->setIntensity(intensity);
}

void CurrentLightService::onIntensitySubscribe(BLEDevice central,
                                               BLECharacteristic characteristic) {
    CurrentLightService* instance = &CurrentLightService::getInstance();

    if (instance->lightManager == nullptr) {
        return;
    }

    // We do not need to check if we are already subscribed since we can only subcribe once
    instance->subIntensity = true;
    instance->lightManager->subscribe(*instance);
}

void CurrentLightService::onIntensityUnsubscribe(BLEDevice central,
                                                 BLECharacteristic characteristic) {
    CurrentLightService* instance = &CurrentLightService::getInstance();

    if (instance->lightManager == nullptr) {
        return;
    }

    instance->subIntensity = false;
    // Check internal subcription state
    if (!instance->isSubscribed()) {
        instance->lightManager->unsubscribe(*instance);
    }
}

void CurrentLightService::onActiveRead(BLEDevice central, BLECharacteristic characteristic) {
    CurrentLightService* instance = &CurrentLightService::getInstance();

    if (instance->lightManager == nullptr) {
        return;
    }

    // Ask lightManager if lamp is active or not
    bool isActive = instance->lightManager->isTurnedOn();
    instance->active.writeValue(isActive);
}

void CurrentLightService::onActiveWrite(BLEDevice central, BLECharacteristic characteristic) {
    CurrentLightService* instance = &CurrentLightService::getInstance();

    if (instance->lightManager == nullptr) {
        return;
    }

    const uint8_t* buf = characteristic.value();
    bool on = static_cast<bool>(buf[0]);

    if (on) {
        instance->lightManager->turnOn();
    } else {
        instance->lightManager->turnOff();
    }
}

void CurrentLightService::onActiveSubscribe(BLEDevice central, BLECharacteristic characteristic) {
    CurrentLightService* instance = &CurrentLightService::getInstance();

    if (instance->lightManager == nullptr) {
        return;
    }

    // We do not need to check if we are already subscribed since we can only subcribe once
    instance->subActive = true;
    instance->lightManager->subscribe(*instance);
}

void CurrentLightService::onActiveUnsubscribe(BLEDevice central, BLECharacteristic characteristic) {
    CurrentLightService* instance = &CurrentLightService::getInstance();

    if (instance->lightManager == nullptr) {
        return;
    }

    instance->subActive = false;
    // Check internal subcription state
    if (!instance->isSubscribed()) {
        instance->lightManager->unsubscribe(*instance);
    }
}