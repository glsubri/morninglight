#include <BLE.h>
#include <NightLightService.h>

#include <string>

const std::string ENABLED_UUID = "00000001" + SERVICE_NIGHT_LIGHT_UUID;
const uint8_t ENABLED_PROP = BLERead | BLEWrite;

const std::string INTENSITY_UUID = "00000002" + SERVICE_NIGHT_LIGHT_UUID;
const uint8_t INTENSITY_PROP = BLERead | BLEWrite;

const std::string START_TIME_UUID = "00000003" + SERVICE_NIGHT_LIGHT_UUID;
const uint8_t START_TIME_PROP = BLERead | BLEWrite;

const std::string END_TIME_UUID = "00000004" + SERVICE_NIGHT_LIGHT_UUID;
const uint8_t END_TIME_PROP = BLERead | BLEWrite;

NightLightService::NightLightService()
    : service(SERVICE_NIGHT_LIGHT_UUID.c_str()),
      enabled(ENABLED_UUID.c_str(), ENABLED_PROP),
      intensity(INTENSITY_UUID.c_str(), INTENSITY_PROP),
      startTime(START_TIME_UUID.c_str(), START_TIME_PROP),
      endTime(END_TIME_UUID.c_str(), END_TIME_PROP) {
    // Setup characteristics in service
    this->service.addCharacteristic(this->enabled);
    this->service.addCharacteristic(this->intensity);
    this->service.addCharacteristic(this->startTime);
    this->service.addCharacteristic(this->endTime);

    // Advertise BLE service
    BLE.setAdvertisedService(this->service);
    BLE.addService(this->service);

    // Setup callbacks
    this->enabled.setEventHandler(BLEWritten, onEnabledWrite);
    this->intensity.setEventHandler(BLEWritten, onIntensityWrite);
    this->startTime.setEventHandler(BLEWritten, onStartTimeWrite);
    this->endTime.setEventHandler(BLEWritten, onEndTimeWrite);
}

NightLightService& NightLightService::getInstance() {
    static NightLightService instance;
    return instance;
}

void NightLightService::setNightLightManager(NightLightManager& nightLightManager) {
    this->nightLightManager = &nightLightManager;
}

// Static handlers

void NightLightService::onEnabledWrite(BLEDevice central, BLECharacteristic characteristic) {
    NightLightService* instance = &NightLightService::getInstance();

    const uint8_t* buf = characteristic.value();
    bool enabled = static_cast<bool>(buf[0]);

    if (instance->nightLightManager != nullptr) {
        instance->nightLightManager->setEnabled(enabled);
    }
}

void NightLightService::onIntensityWrite(BLEDevice central, BLECharacteristic characteristic) {
    NightLightService* instance = &NightLightService::getInstance();

    const uint8_t* buf = characteristic.value();
    unsigned short intensity = buf[0];

    if (intensity > 100) {
        return;
    }

    if (instance->nightLightManager != nullptr) {
        instance->nightLightManager->setIntensity(intensity);
    }
}

void NightLightService::onStartTimeWrite(BLEDevice central, BLECharacteristic characteristic) {
    NightLightService* instance = &NightLightService::getInstance();

    if (instance->nightLightManager != nullptr) {
        instance->nightLightManager->setStartTime(Time::fromBytes(characteristic.value()));
    }
}

void NightLightService::onEndTimeWrite(BLEDevice central, BLECharacteristic characteristic) {
    NightLightService* instance = &NightLightService::getInstance();

    if (instance->nightLightManager != nullptr) {
        instance->nightLightManager->setEndTime(Time::fromBytes(characteristic.value()));
    }
}