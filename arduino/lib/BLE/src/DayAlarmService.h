#ifndef DAY_ALARM_SERVICE_H
#define DAY_ALARM_SERVICE_H

#include <ArduinoBLE.h>

#include <string>

/**
 * @brief The DayAlarmService class is responsible to hold the data for one alarm (one per day).
 *
 */
class DayAlarmService {
   public:
    /**
     * @brief Construct a new Day Alarm Service object
     *
     * @param serviceUUID The UUID of the service
     * @param enabledUUID The UUID of the "enabled" characteristic
     * @param startTimeUUID The UUID of the "startTime" characteristic
     * @param durationUUID The UUID of the "duration" characteristic
     */
    DayAlarmService(const std::string& serviceUUID, const std::string& enabledUUID,
                    const std::string& startTimeUUID, const std::string& durationUUID);

   private:
    BLEService service;
    BLEBoolCharacteristic enabled;
    BLEUnsignedIntCharacteristic startTime;
    BLEShortCharacteristic duration;

    // Callback handlers

    /**
     * @brief This method is called just before the enabled characteristic is read from.
     *
     * @param central Is the central device reading our characteristic
     * @param characteristic Is the BLECharacteristic being read
     */
    static void onEnabledRead(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the enabled characteristic is written to.
     *
     * @param central Is the central device writting to our characteristic
     * @param characteristic Is the BLECharacteristic being written to
     */
    static void onEnabledWrite(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called just before the startTime characteristic is read from.
     *
     * @param central Is the central device reading our characteristic
     * @param characteristic Is the BLECharacteristic being read
     */
    static void onStartTimeRead(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the startTime characteristic is written to.
     *
     * @param central Is the central device writting to our characteristic
     * @param characteristic Is the BLECharacteristic being written to
     */
    static void onStartTimeWrite(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called just before the duration characteristic is read from.
     *
     * @param central Is the central device reading our characteristic
     * @param characteristic Is the BLECharacteristic being read
     */
    static void onDurationRead(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the duration characteristic is written to.
     *
     * @param central Is the central device writting to our characteristic
     * @param characteristic Is the BLECharacteristic being written to
     */
    static void onDurationWrite(BLEDevice central, BLECharacteristic characteristic);
};

#endif