#ifndef CURRENT_LIGHT_SERVICE_H
#define CURRENT_LIGHT_SERVICE_H

#include <ArduinoBLE.h>
#include <BLE.h>
#include <LightManager.h>
#include <LightSubscriber.h>

#include <string>

/**
 * @brief This class is responsible to hold the data relating to the current state of our lamp
 *
 */
class CurrentLightService : public LightSubscriber {
   public:
    // Singleton related
    /**
     * @brief Returns the current instance of CurrentLightService, or builds one if not existing
     *
     * @return CurrentLightService& The saved instance of CurrentLightService
     */
    static CurrentLightService& getInstance();

    // Redefine operators for singleton pattern
    CurrentLightService& operator=(const CurrentLightService&) = delete;
    CurrentLightService(const CurrentLightService&) = delete;

    /**
     * @brief Get the EventQueue of the CurrentLightService instance
     *
     * @return events::EventQueue* A pointer to the queue of the current CurrentLightService
     * instance
     */
    static events::EventQueue* getQueue();

    // must be public to be accessed
    events::EventQueue queue;

    /**
     * @brief Sets the Light Manager object.
     *
     *
     * Note that this cannot be done in the constructor because of the singleton pattern
     *
     * @param lightManager The light manager to attach to the CurrentLightService. Should a
     * LightPublisher
     */
    void setLightManager(LightManager& lightManager);

    // Subscriber
    void updateIntensity(unsigned short intensity) override;
    void updateActive(bool on) override;

   private:
    // Private constructor for singleton pattern
    CurrentLightService();

    LightManager* lightManager;

    // BLE
    BLEService service;

    BLEUnsignedShortCharacteristic intensity;

    BLEBoolCharacteristic active;

    /**
     * @brief Is someone subscribed to the intensity
     *
     */
    bool subIntensity;

    /**
     * @brief Is someone subscribed to the activity
     *
     */
    bool subActive;

    /**
     * @brief Is someone subscribed to one of our characteristics
     *
     * @return true If someone is subscribed to at least one of our characteristics
     * @return false If no one is subscribed to any characteristics
     */
    bool isSubscribed();

    // Static access handlers
    /**
     * @brief This method is called just before the intensity characteristic is read from.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being read
     */
    static void onIntensityRead(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the intensity characteristic is written to.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being written to
     */
    static void onIntensityWrite(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the intensity characteristic is subscribed to.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being subscribed to
     */
    static void onIntensitySubscribe(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the intensity characteristic is unsubscribed from.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being unsubscribed from
     */
    static void onIntensityUnsubscribe(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called just before the active characteristic is read from.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being read
     */
    static void onActiveRead(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the active characteristic is written to.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being written to
     */
    static void onActiveWrite(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the active characteristic is subscribed to.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being subscribed to
     */
    static void onActiveSubscribe(BLEDevice central, BLECharacteristic characteristic);

    /**
     * @brief This method is called when the active characteristic is unsubscribed from.
     *
     * @param central Is the central device writing our characteristic
     * @param characteristic Is the BLECharacteristic being unsubscribed from
     */
    static void onActiveUnsubscribe(BLEDevice central, BLECharacteristic characteristic);
};

#endif