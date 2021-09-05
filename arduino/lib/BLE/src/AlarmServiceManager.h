#ifndef ALARM_SERVICE_MANAGER_H
#define ALARM_SERVICE_MANAGER_H

#include <AlarmProvider.h>
#include <ArduinoBLE.h>
#include <DayAlarmService.h>
#include <Time.h>
#include <rtos.h>

#include <array>
#include <map>
#include <string>
#include <vector>

/**
 * @brief This class is responsible to manager the different alarms
 *
 */
class AlarmServiceManager {
   public:
    /**
     * @brief Get the Instance object or creates one
     *
     * @return AlarmServiceManager& The AlarmServiceManager instance
     */
    static AlarmServiceManager& getInstance();

    // Redefine operators according to singleton pattern
    AlarmServiceManager& operator=(const AlarmServiceManager&) = delete;
    AlarmServiceManager(const AlarmServiceManager&) = delete;

    /**
     * @brief Set the Alarm Provider object
     *
     * @param alarmProvider The AlarmProvider to set
     */
    void setAlarmProvider(AlarmProvider& alarmProvider);

    /**
     * @brief This function is called when one of the enabled characteristic is read from
     *
     * @param characteristic The enabled characteristic which is read from
     */
    void onEnabledRead(BLECharacteristic& characteristic);

    /**
     * @brief This function is called when one of the enabled characteristic is written to
     *
     * @param characteristic The enabled characteristic which is written to
     */
    void onEnabledWrite(BLECharacteristic& characteristic);

    /**
     * @brief This function is called when one of the startTime characteristic is read from
     *
     * @param characteristic The startTime characteristic which is read from
     */
    void onStartTimeRead(BLECharacteristic& characteristic);

    /**
     * @brief This function is called when one of the startTime characteristic is written to
     *
     * @param characteristic The startTime characteristic which is written to
     */
    void onStartTimeWrite(BLECharacteristic& characteristic);

    /**
     * @brief This function is called when one of the duration characteristic is read from
     *
     * @param characteristic The duration characteristic which is read from
     */
    void onDurationRead(BLECharacteristic& characteristic);

    /**
     * @brief This function is called when one of the duration characteristic is written to
     *
     * @param characteristic The duration characteristic which is written to
     */
    void onDurationWrite(BLECharacteristic& characteristic);

   private:
    // Private constructor is part of the singleton pattern
    AlarmServiceManager();

    AlarmProvider* alarmProvider;

    std::vector<DayAlarmService> alarmServicies;
    std::map<std::string, Weekday> characteristicsDay;

    Weekday dayOf(const BLECharacteristic& characteristic);
    Alarm alarmOf(const BLECharacteristic& characteristic);

    // protect concurrent access to alarms
    rtos::Mutex mutex;

    // Static

    // All of these array are necessary because, the ArduinoBLE library does not copy the uuid given
    // in the constructor. If the uuid string is lost, the characteristics and services will not be
    // able to know their own uuid. A Github issue was openend:
    // https://github.com/arduino-libraries/ArduinoBLE/issues/191

    /**
     * @brief This array stores the 7 dayAlarmSerivces UUID
     *
     */
    static const std::array<std::string, 7> serviceUUID;

    /**
     * @brief This array stores the 7 enabled characteristics UUID
     *
     */
    static const std::array<std::string, 7> enabledUUID;

    /**
     * @brief This array stores the 7 startTime characteristic UUID
     *
     */
    static const std::array<std::string, 7> startTimeUUID;

    /**
     * @brief This array stores the 7 duration characteristics UUID
     *
     */
    static const std::array<std::string, 7> durationUUID;

    /**
     * @brief This static method is used to transform a time object into a sequence of bytes, ready
     * to be sent through BLE
     *
     * @param time The time object to transform
     * @param bytes The array of bytes to store the results in
     */
    static void startTimeToBytes(const Time& time, uint16_t* bytes);
};

#endif