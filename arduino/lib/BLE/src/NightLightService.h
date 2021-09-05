#ifndef NIGHT_LIGHT_SERVICE_H
#define NIGHT_LIGHT_SERVICE_H

#include <ArduinoBLE.h>
#include <NightLightManager.h>

/**
 * @brief This class is responsible to hold the data relating to the Night Light setting
 *
 */
class NightLightService {
   public:
    // Singleton related
    /**
     * @brief Returns the current instance of NightLightService, or builds one if needed
     *
     * @return NightLightService& The saved instance of NightLightService
     */
    static NightLightService& getInstance();

    // Redefine operators for singleton pattern
    NightLightService& operator=(const NightLightService&) = delete;
    NightLightService(const NightLightService&) = delete;

    /**
     * @brief Sets the Light Manager object.
     *
     *
     * Note that this cannot be done in the constructor because of the singleton pattern
     *
     * @param nightLightManager The light manager to attach to the CurrentLightService. Should a
     * LightPublisher
     */
    void setNightLightManager(NightLightManager& nightLightManager);

   private:
    // Private constructor for singleton pattern
    NightLightService();

    NightLightManager* nightLightManager;

    // BLE
    BLEService service;

    BLEBoolCharacteristic enabled;
    BLEUnsignedShortCharacteristic intensity;
    BLEUnsignedIntCharacteristic startTime;
    BLEUnsignedIntCharacteristic endTime;

    // Static access handlers

    /**
     * @brief This method is called when the enabled characteristic is written to.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being written to
     */
    static void onEnabledWrite(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the intensity characteristic is written to.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being written to
     */
    static void onIntensityWrite(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the startTime characteristic is written to.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being written to
     */
    static void onStartTimeWrite(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the endTime characteristic is written to.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being written to
     */
    static void onEndTimeWrite(BLEDevice central, BLECharacteristic characteristic);
};

#endif