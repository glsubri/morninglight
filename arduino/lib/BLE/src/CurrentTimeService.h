#ifndef CURRENT_TIME_SERVICE_H
#define CURRENT_TIME_SERVICE_H

#include <ArduinoBLE.h>
#include <TimeManager.h>
#include <TimeSubscriber.h>

/**
 * @brief CurrentTimeService is a singleton class used to setup the BLE Current Time Service.
 *
 * Note that this is a singleton class because there cannot be more thant one CurrentTimeService as
 * it is defined by the Bluetooth group.
 *
 */
class CurrentTimeService : public TimeSubscriber {
   public:
    /**
     * @brief Returns the current instance. If no instance exists, it will create one.
     *
     * @return CurrentTimeService& The singleton instance
     */
    static CurrentTimeService& getInstance();

    // Singleton pattern
    CurrentTimeService& operator=(const CurrentTimeService&) = delete;
    CurrentTimeService(const CurrentTimeService&) = delete;

    /**
     * @brief Set the Time Manager object
     *
     * @param timeManager The timeManager object to set
     */
    void setTimeManager(TimeManager& timeManager);

    // Subscriber
    void updateDatetime(tm& datetime) override;

   private:
    // Private constructor is necessary for singleton
    CurrentTimeService();

    /**
     * @brief A pointer to the TimeManager used to know the time.
     *
     */
    TimeManager* timeManager;

    /**
     * @brief The UUID of the Current Time Service.
     *
     * This is regulated by the Bluetooth specification, see the Assigned Numbers document.
     * https://btprodspecificationrefs.blob.core.windows.net/assigned-values/16-bit%20UUID%20Numbers%20Document.pdf
     */
    static constexpr const char* SERVICE_UUID = "1805";

    /**
     * @brief The UUID of the Current Time Characteristic.
     *
     * This is regulated by the Bluetooth specification, see the Assigned Numbers document.
     * https://btprodspecificationrefs.blob.core.windows.net/assigned-values/16-bit%20UUID%20Numbers%20Document.pdf
     */
    static constexpr const char* CURRENT_TIME_UUID = "2A2B";

    /**
     * @brief The properties of the Current Time Characteristic.
     *
     * This is regulated by the Bluetooth specification, see the Current Time Service document.
     * https://www.bluetooth.com/specifications/specs/current-time-service-1-1/
     */
    static constexpr uint8_t CURRENT_TIME_PROPERTIES = BLERead | BLEWrite | BLENotify;

    BLEService service;
    BLECharacteristic currentTime;

    /**
     * @brief This method is called just before the currentTime characteristic is read from.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being read
     */
    static void onRead(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the currentTime characteristic is written to.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being written to
     */
    static void onWrite(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the currentTime characteristic is subscribed to.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being subscribed to
     */
    static void onSubscribe(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the currentTime characteristic is unsubscribed from.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being unsubscribed from
     */
    static void onUnsubscribe(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This static method is used to transform a datetime to it's BLE
     * representation
     *
     * @param datetime The time to transform
     * @param bytes The array of bytes where the BLE representation will be
     * stored.
     */
    static void timeToBytes(const tm& datetime, uint8_t* bytes);

    // The TestWrapper class is used to test CurrentTimeService
    friend class TestWrapper;
};

#endif
