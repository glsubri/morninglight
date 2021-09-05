#include <Arduino.h>
#include <LightManager.h>
#include <NightLightManager.h>
#include <TimeManager.h>
#include <TimePublisherMock.h>
#include <unity.h>

const int zcPin = 2;
const int pwmPin = 3;
const int buttonPin = 4;

TimePublisherMock timePublisher;

void test_enter_exit_is_correct_normal_mode() {
    LightManager lightManager(zcPin, pwmPin, buttonPin);
    NightLightManager manager(timePublisher, lightManager);

    int initialIntensity = 58;
    int intensity = 34;

    lightManager.setIntensity(initialIntensity);
    lightManager.turnAlarmModeOff();

    manager.setEnabled(true);
    manager.setIntensity(intensity);
    manager.setStartTime(Time(10, 0));
    manager.setEndTime(Time(10, 5));

    tm datetime = TimeManager::toDeviceTime(2021, 1, 1, 9, 59, 59);
    manager.updateDatetime(datetime);
    TEST_ASSERT_TRUE(lightManager.getIntensity() == initialIntensity);

    datetime.tm_hour = 10;
    datetime.tm_min = 0;
    datetime.tm_sec = 0;
    manager.updateDatetime(datetime);
    TEST_ASSERT_TRUE(lightManager.getIntensity() == intensity);

    datetime.tm_min = 5;
    manager.updateDatetime(datetime);
    TEST_ASSERT_TRUE(lightManager.getIntensity() == initialIntensity);
}

void test_enter_alarm_exit_normal_is_correct() {
    LightManager lightManager(zcPin, pwmPin, buttonPin);
    NightLightManager manager(timePublisher, lightManager);

    int initialIntensity = 58;
    int intensity = 34;

    lightManager.setIntensity(initialIntensity);
    lightManager.turnAlarmModeOn();

    manager.setEnabled(true);
    manager.setIntensity(intensity);
    manager.setStartTime(Time(10, 0));
    manager.setEndTime(Time(10, 5));

    tm datetime = TimeManager::toDeviceTime(2021, 1, 1, 10, 0, 0);
    manager.updateDatetime(datetime);
    TEST_ASSERT_TRUE(lightManager.getIntensity() == initialIntensity);

    lightManager.turnAlarmModeOff();

    datetime.tm_min = 5;
    manager.updateDatetime(datetime);
    TEST_ASSERT_TRUE(lightManager.getIntensity() == NightLightManager::defaultIntensity);
}

void test_enter_alarm_exit_alarm_is_correct() {
    LightManager lightManager(zcPin, pwmPin, buttonPin);
    NightLightManager manager(timePublisher, lightManager);

    int initialIntensity = 58;
    int intensity = 34;

    lightManager.setIntensity(initialIntensity);
    lightManager.turnAlarmModeOn();

    manager.setEnabled(true);
    manager.setIntensity(intensity);
    manager.setStartTime(Time(10, 0));
    manager.setEndTime(Time(10, 5));

    tm datetime = TimeManager::toDeviceTime(2021, 1, 1, 10, 0, 0);
    manager.updateDatetime(datetime);
    TEST_ASSERT_TRUE(lightManager.getIntensity() == initialIntensity);

    datetime.tm_min = 5;
    manager.updateDatetime(datetime);
    TEST_ASSERT_TRUE(lightManager.getIntensity() == initialIntensity);
}

void setup() {
    delay(2000);

    UNITY_BEGIN();
    RUN_TEST(test_enter_exit_is_correct_normal_mode);
    RUN_TEST(test_enter_alarm_exit_normal_is_correct);
    RUN_TEST(test_enter_alarm_exit_alarm_is_correct);
    UNITY_END();
}

void loop() {
    digitalWrite(13, HIGH);
    delay(100);
    digitalWrite(13, LOW);
    delay(500);
}
