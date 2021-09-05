#include <Alarm.h>
#include <AlarmManager.h>
#include <Arduino.h>
#include <LightManager.h>
#include <Time.h>
#include <TimeManager.h>
#include <TimePublisherMock.h>
#include <unity.h>

const int zcPin = 2;
const int pwmPin = 3;
const int buttonPin = 4;

TimePublisherMock timePublisher;

void test_setAlarm_equivalent_getAlarm() {
    LightManager lightManager(zcPin, pwmPin, buttonPin);
    AlarmManager manager(timePublisher, lightManager);

    Alarm expected(true, Time(12, 0), 5);
    manager.setAlarm(monday, expected);

    Alarm actual = manager.getAlarm(monday);

    TEST_ASSERT_TRUE(expected == actual);
}

void test_isInAlarmState_is_correct() {
    LightManager lightManager(zcPin, pwmPin, buttonPin);
    AlarmManager manager(timePublisher, lightManager);

    int duration = 1;
    Alarm alarm(true, Time(10, 0), duration);
    manager.setAlarm(monday, alarm);

    tm datetime = TimeManager::toDeviceTime(2021, 1, 4, 10, 0, 0);
    datetime.tm_wday = 1;  // tm_wday is our weekday + 1

    manager.updateDatetime(datetime);
    TEST_ASSERT_TRUE(manager.isInAlarmState());

    // not 0 or 1 to not trigger new alarm
    datetime.tm_sec = 30;
    int steps = 59 * duration;
    for (int i = 0; i < steps; i++) {
        manager.updateDatetime(datetime);
    }
    TEST_ASSERT_TRUE(manager.isInAlarmState());

    manager.updateDatetime(datetime);
    TEST_ASSERT_FALSE(manager.isInAlarmState());
}

void setup() {
    delay(2000);

    UNITY_BEGIN();
    RUN_TEST(test_setAlarm_equivalent_getAlarm);
    RUN_TEST(test_isInAlarmState_is_correct);
    UNITY_END();
}

void loop() {
    digitalWrite(13, HIGH);
    delay(100);
    digitalWrite(13, LOW);
    delay(500);
}
