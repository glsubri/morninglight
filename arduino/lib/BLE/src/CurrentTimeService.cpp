#include <CurrentTimeService.h>
#include <TimeManager.h>

CurrentTimeService::CurrentTimeService()
    : service(this->SERVICE_UUID),
      currentTime(this->CURRENT_TIME_UUID, this->CURRENT_TIME_PROPERTIES, sizeof(this->currentTime),
                  true) {
    this->service.addCharacteristic(this->currentTime);

    // Advertise BLE service
    BLE.setAdvertisedService(this->service);
    BLE.addService(this->service);

    // Setup callbacks
    this->currentTime.setEventHandler(BLERead, CurrentTimeService::onRead);
    this->currentTime.setEventHandler(BLEWritten, CurrentTimeService::onWrite);
    this->currentTime.setEventHandler(BLESubscribed, CurrentTimeService::onSubscribe);
    this->currentTime.setEventHandler(BLEUnsubscribed, CurrentTimeService::onUnsubscribe);
}

CurrentTimeService& CurrentTimeService::getInstance() {
    static CurrentTimeService instance;
    return instance;
}

void CurrentTimeService::setTimeManager(TimeManager& timeManager) {
    if (this->timeManager != nullptr) {
        this->timeManager->unsubscribe(*this);
    }

    this->timeManager = &timeManager;
}

void CurrentTimeService::onRead(BLEDevice central, BLECharacteristic characteristic) {
    CurrentTimeService* instance = &CurrentTimeService::getInstance();
    tm& datetime = instance->timeManager->getDateTime();

    instance->updateDatetime(datetime);
}

void CurrentTimeService::onWrite(BLEDevice central, BLECharacteristic characteristic) {
    // Contains 10 bytes
    const uint8_t* buf = characteristic.value();

    // year is given in LE
    int year = buf[0] | buf[1] << 8;
    int month = buf[2];
    int day = buf[3];
    int hour = buf[4];
    int minute = buf[5];
    int second = buf[6];

    CurrentTimeService* instance = &CurrentTimeService::getInstance();
    instance->timeManager->setDateTime(year, month, day, hour, minute, second);
}

void CurrentTimeService::onSubscribe(BLEDevice central, BLECharacteristic characteristic) {
    CurrentTimeService* instance = &CurrentTimeService::getInstance();

    if (instance->timeManager != nullptr) {
        instance->timeManager->subscribe(*instance);
    }
}

void CurrentTimeService::onUnsubscribe(BLEDevice central, BLECharacteristic characteristic) {
    CurrentTimeService* instance = &CurrentTimeService::getInstance();

    if (instance->timeManager != nullptr) {
        instance->timeManager->unsubscribe(*instance);
    }
}

void CurrentTimeService::updateDatetime(tm& datetime) {
    // Note that we use C arrays because we cannot use std::array().data() until
    // C++17 so we have no way of easily converting std::array to C-array
    uint8_t bleDateTime[10];
    CurrentTimeService::timeToBytes(datetime, bleDateTime);

    this->currentTime.writeValue(bleDateTime, sizeof(bleDateTime));
}

void CurrentTimeService::timeToBytes(const tm& datetime, uint8_t* bytes) {
    // The year starts at 1900 on the device
    int year = datetime.tm_year + 1900;

    // The year should be a uint16 in LE
    bytes[0] = (uint8_t)year;
    bytes[1] = (uint8_t)(year >> 8);

    // The months are counted starting at 0 on the device
    bytes[2] = datetime.tm_mon + 1;

    bytes[3] = datetime.tm_mday;
    bytes[4] = datetime.tm_hour;
    bytes[5] = datetime.tm_min;
    bytes[6] = datetime.tm_sec;
    bytes[7] = datetime.tm_wday;

    // We do not really care about those last two fields
    bytes[8] = 0;  // Fraction 256
    bytes[9] = 0;  // Adjust Reason
}
