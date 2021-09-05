#include <AlarmServiceManager.h>
#include <DayAlarmService.h>

const uint8_t ENABLED_PROPERTIES = BLERead | BLEWrite;
const uint8_t START_TIME_PROPERTIES = BLERead | BLEWrite;
const uint8_t DURATION_PROPERTIES = BLERead | BLEWrite;

DayAlarmService::DayAlarmService(const std::string& serviceUUID, const std::string& enabledUUID,
                                 const std::string& startTimeUUID, const std::string& durationUUID)
    : service(serviceUUID.c_str()),
      enabled(enabledUUID.c_str(), ENABLED_PROPERTIES),
      startTime(startTimeUUID.c_str(), START_TIME_PROPERTIES),
      duration(durationUUID.c_str(), DURATION_PROPERTIES) {
    // Setup characteristics in service
    this->service.addCharacteristic(this->enabled);
    this->service.addCharacteristic(this->startTime);
    this->service.addCharacteristic(this->duration);

    // Advertise BLE service
    BLE.setAdvertisedService(this->service);
    BLE.addService(this->service);

    // Setup callbacks
    this->enabled.setEventHandler(BLERead, onEnabledRead);
    this->enabled.setEventHandler(BLEWritten, onEnabledWrite);

    this->startTime.setEventHandler(BLERead, onStartTimeRead);
    this->startTime.setEventHandler(BLEWritten, onStartTimeWrite);

    this->duration.setEventHandler(BLERead, onDurationRead);
    this->duration.setEventHandler(BLEWritten, onDurationWrite);
};

void DayAlarmService::onEnabledRead(BLEDevice central, BLECharacteristic characteristic) {
    AlarmServiceManager::getInstance().onEnabledRead(characteristic);
}
void DayAlarmService::onEnabledWrite(BLEDevice central, BLECharacteristic characteristic) {
    AlarmServiceManager::getInstance().onEnabledWrite(characteristic);
}

void DayAlarmService::onStartTimeRead(BLEDevice central, BLECharacteristic characteristic) {
    AlarmServiceManager::getInstance().onStartTimeRead(characteristic);
}
void DayAlarmService::onStartTimeWrite(BLEDevice central, BLECharacteristic characteristic) {
    AlarmServiceManager::getInstance().onStartTimeWrite(characteristic);
}

void DayAlarmService::onDurationRead(BLEDevice central, BLECharacteristic characteristic) {
    AlarmServiceManager::getInstance().onDurationRead(characteristic);
}
void DayAlarmService::onDurationWrite(BLEDevice central, BLECharacteristic characteristic) {
    AlarmServiceManager::getInstance().onDurationWrite(characteristic);
}
