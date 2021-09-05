#include <AlarmServiceManager.h>
#include <BLE.h>
#include <utils.h>

const std::string BASE_SERVICE_UUID = "00000000-";
const std::string BASE_ENABLED_UUID = "00000001-";
const std::string BASE_START_TIME_UUID = "00000002-";
const std::string BASE_DURATION_UUID = "00000003-";

// BLECharacteristics and BLEServices do not copy their uuids internally,
// so we need to "keep it somewhere secret, somewhere safe"
const std::array<std::string, 7> AlarmServiceManager::serviceUUID{
    BASE_SERVICE_UUID + SERVICE_MONDAY_ALARM_UUID,
    BASE_SERVICE_UUID + SERVICE_TUESDAY_ALARM_UUID,
    BASE_SERVICE_UUID + SERVICE_WEDNESDAY_ALARM_UUID,
    BASE_SERVICE_UUID + SERVICE_THURSDAY_ALARM_UUID,
    BASE_SERVICE_UUID + SERVICE_FRIDAY_ALARM_UUID,
    BASE_SERVICE_UUID + SERVICE_SATURDAY_ALARM_UUID,
    BASE_SERVICE_UUID + SERVICE_SUNDAY_ALARM_UUID};

const std::array<std::string, 7> AlarmServiceManager::enabledUUID{
    BASE_ENABLED_UUID + SERVICE_MONDAY_ALARM_UUID,
    BASE_ENABLED_UUID + SERVICE_TUESDAY_ALARM_UUID,
    BASE_ENABLED_UUID + SERVICE_WEDNESDAY_ALARM_UUID,
    BASE_ENABLED_UUID + SERVICE_THURSDAY_ALARM_UUID,
    BASE_ENABLED_UUID + SERVICE_FRIDAY_ALARM_UUID,
    BASE_ENABLED_UUID + SERVICE_SATURDAY_ALARM_UUID,
    BASE_ENABLED_UUID + SERVICE_SUNDAY_ALARM_UUID};

const std::array<std::string, 7> AlarmServiceManager::startTimeUUID{
    BASE_START_TIME_UUID + SERVICE_MONDAY_ALARM_UUID,
    BASE_START_TIME_UUID + SERVICE_TUESDAY_ALARM_UUID,
    BASE_START_TIME_UUID + SERVICE_WEDNESDAY_ALARM_UUID,
    BASE_START_TIME_UUID + SERVICE_THURSDAY_ALARM_UUID,
    BASE_START_TIME_UUID + SERVICE_FRIDAY_ALARM_UUID,
    BASE_START_TIME_UUID + SERVICE_SATURDAY_ALARM_UUID,
    BASE_START_TIME_UUID + SERVICE_SUNDAY_ALARM_UUID};

const std::array<std::string, 7> AlarmServiceManager::durationUUID{
    BASE_DURATION_UUID + SERVICE_MONDAY_ALARM_UUID,
    BASE_DURATION_UUID + SERVICE_TUESDAY_ALARM_UUID,
    BASE_DURATION_UUID + SERVICE_WEDNESDAY_ALARM_UUID,
    BASE_DURATION_UUID + SERVICE_THURSDAY_ALARM_UUID,
    BASE_DURATION_UUID + SERVICE_FRIDAY_ALARM_UUID,
    BASE_DURATION_UUID + SERVICE_SATURDAY_ALARM_UUID,
    BASE_DURATION_UUID + SERVICE_SUNDAY_ALARM_UUID};

AlarmServiceManager& AlarmServiceManager::getInstance() {
    static AlarmServiceManager instance;
    return instance;
}

AlarmServiceManager::AlarmServiceManager() {
    // Initialize DayAlarmServices
    for (Weekday day = monday; day <= sunday; day = Weekday(day + 1)) {
        // Create BLE service
        DayAlarmService service(
            AlarmServiceManager::serviceUUID[day], AlarmServiceManager::enabledUUID[day],
            AlarmServiceManager::startTimeUUID[day], AlarmServiceManager::durationUUID[day]);

        // Store "metadata"
        this->alarmServicies[day] = service;
        this->characteristicsDay[AlarmServiceManager::enabledUUID[day]] = day;
        this->characteristicsDay[AlarmServiceManager::startTimeUUID[day]] = day;
        this->characteristicsDay[AlarmServiceManager::durationUUID[day]] = day;
    }
}

void AlarmServiceManager::setAlarmProvider(AlarmProvider& alarmProvider) {
    this->mutex.lock();
    this->alarmProvider = &alarmProvider;
    this->mutex.unlock();
}

void AlarmServiceManager::onEnabledRead(BLECharacteristic& characteristic) {
    this->mutex.lock();

    Alarm alarm = this->alarmOf(characteristic);
    uint8_t value = static_cast<uint8_t>(alarm.enabled);
    characteristic.writeValue(value, false);

    this->mutex.unlock();
}

void AlarmServiceManager::onEnabledWrite(BLECharacteristic& characteristic) {
    this->mutex.lock();

    Weekday day = dayOf(characteristic);
    const uint8_t* buf = characteristic.value();
    bool enabled = static_cast<bool>(buf[0]);

    Alarm alarm = this->alarmProvider->getAlarm(day);
    alarm.enabled = enabled;

    this->alarmProvider->setAlarm(day, alarm);

    this->mutex.unlock();
}

void AlarmServiceManager::onStartTimeRead(BLECharacteristic& characteristic) {
    this->mutex.lock();

    Alarm alarm = this->alarmOf(characteristic);

    uint16_t bytes[2];
    startTimeToBytes(alarm.startTime, bytes);
    characteristic.writeValue(bytes, sizeof(bytes));

    this->mutex.unlock();
}

void AlarmServiceManager::onStartTimeWrite(BLECharacteristic& characteristic) {
    this->mutex.lock();

    Weekday day = dayOf(characteristic);

    Alarm alarm = this->alarmProvider->getAlarm(day);
    alarm.startTime = Time::fromBytes(characteristic.value());

    this->alarmProvider->setAlarm(day, alarm);

    this->mutex.unlock();
}

void AlarmServiceManager::onDurationRead(BLECharacteristic& characteristic) {
    this->mutex.lock();

    Alarm alarm = this->alarmOf(characteristic);

    uint16_t value = static_cast<uint16_t>(alarm.duration);
    characteristic.writeValue(value, false);

    this->mutex.unlock();
}

void AlarmServiceManager::onDurationWrite(BLECharacteristic& characteristic) {
    this->mutex.lock();

    Weekday day = dayOf(characteristic);
    const uint8_t* buf = characteristic.value();
    unsigned short duration = static_cast<unsigned short>(buf[0]);

    // Duration should be bigger than a minute and less than 23 hours
    if (!inBounds(duration, 1, 23 * 60)) {
        return;
    }

    Alarm alarm = this->alarmProvider->getAlarm(day);
    alarm.duration = duration;

    this->alarmProvider->setAlarm(day, alarm);

    this->mutex.unlock();
}

Weekday AlarmServiceManager::dayOf(const BLECharacteristic& characteristic) {
    std::string uuid(characteristic.uuid());
    return this->characteristicsDay[uuid];
}

Alarm AlarmServiceManager::alarmOf(const BLECharacteristic& characteristic) {
    Weekday day = dayOf(characteristic);
    return this->alarmProvider->getAlarm(day);
}

void AlarmServiceManager::startTimeToBytes(const Time& time, uint16_t* bytes) {
    bytes[0] = time.hour;
    bytes[1] = time.minute;
}