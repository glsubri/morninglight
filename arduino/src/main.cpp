#include <AlarmManager.h>
#include <AlarmServiceManager.h>
#include <Arduino.h>
#include <ArduinoBLE.h>
#include <BLE.h>
#include <CurrentLightService.h>
#include <CurrentTimeService.h>
#include <LightManager.h>
#include <NightLightManager.h>
#include <NightLightService.h>
#include <TimeManager.h>
#include <mbed.h>
#include <mbed_events.h>
#include <platform/Callback.h>
#include <rtos.h>

const int zcPin = 2;
const int pwmPin = 3;
const int buttonPin = 4;

const long BLEPollTimeout = 10L;

CurrentTimeService* currentTimeService;
CurrentLightService* currentLightService;
AlarmServiceManager* alarmServiceManager;
NightLightService* nightLightService;

TimeManager timeManager;
LightManager lightManager(pwmPin, zcPin, buttonPin);
AlarmManager alarmManager(timeManager, lightManager);
NightLightManager nightLightManager(timeManager, lightManager);

events::EventQueue buttonQueue;

rtos::Thread threadTime(osPriorityNormal);
rtos::Thread threadLight(osPriorityRealtime);

void onBLEConnected(BLEDevice central) { digitalWrite(LED_BUILTIN, HIGH); }

void onBLEDisconnected(BLEDevice central) { digitalWrite(LED_BUILTIN, LOW); }

void setup() {
    // Initialize Serial
    Serial.begin(9600);

    // Pins
    pinMode(LED_BUILTIN, OUTPUT);

    // Initialize BLE
    while (!BLE.begin()) {
        Serial.println("Starting BLE Failed.");
    }

    BLE.setLocalName(GAP_LOCAL_NAME.c_str());
    BLE.setAppearance(GAP_APPEARANCE);

    // Managers
    LightManager::setInstance(lightManager);

    currentTimeService = &CurrentTimeService::getInstance();
    currentTimeService->setTimeManager(timeManager);

    currentLightService = &CurrentLightService::getInstance();
    currentLightService->setLightManager(lightManager);

    alarmServiceManager = &AlarmServiceManager::getInstance();
    alarmServiceManager->setAlarmProvider(alarmManager);

    nightLightService = &NightLightService::getInstance();
    nightLightService->setNightLightManager(nightLightManager);

    // Threads
    threadTime.start(mbed::callback(&timeManager.queue, &events::EventQueue::dispatch_forever));
    threadLight.start(
        mbed::callback(LightManager::getQueue(), &events::EventQueue::dispatch_forever));

    // BLE connection settings
    BLE.setConnectionInterval(0x28, 0x50);  // between 50 ms to 100 ms (units of 1.25ms)
    BLE.setAdvertisingInterval(32);  // 20 ms (units of 0.625ms), which is the lowest advertisment
                                     // rate allowed by BLE standard

    BLE.setEventHandler(BLEConnected, onBLEConnected);
    BLE.setEventHandler(BLEDisconnected, onBLEDisconnected);

    BLE.advertise();
}

void loop() { BLE.poll(BLEPollTimeout); }