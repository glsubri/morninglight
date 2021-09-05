#include "test_CurrentTimeService.h"

#include <Arduino.h>
#include <CurrentTimeService.h>
#include <TimeManager.h>
#include <unity.h>

TestWrapper wrapper;

void TestWrapper::test_zero() {
    tm datetime = tm{};

    int year = 1900;
    uint8_t expected[10] = {
        (uint8_t)year,
        (uint8_t)(year >> 8),
        1,  // month
        0,  // day of month
        0,  // hour
        0,  // minute
        0,  // second
        0,  // week day
        0,  // fraction
        0   // adjust reason
    };

    uint8_t actual[10];
    CurrentTimeService::timeToBytes(datetime, actual);

    TEST_ASSERT_EQUAL_INT8_ARRAY(expected, actual, 10);
}

void TestWrapper::test_birthday() {
    // TimeManager does not handle weekdays correctly
    tm datetime = TimeManager::toDeviceTime(1993, 10, 29, 16, 30, 45);
    datetime.tm_wday = 4;

    int year = 1993;
    uint8_t expected[10] = {
        (uint8_t)year,
        (uint8_t)(year >> 8),
        10,  // month
        29,  // day of month
        16,  // hour
        30,  // minute
        45,  // second
        4,   // week day
        0,   // fraction
        0    // adjust reason
    };

    uint8_t actual[10];
    CurrentTimeService::timeToBytes(datetime, actual);

    TEST_ASSERT_EQUAL_INT8_ARRAY(expected, actual, 10);
}

void setup() {
    delay(2000);

    UNITY_BEGIN();
    RUN_TEST(wrapper.test_zero);
    RUN_TEST(wrapper.test_birthday);
    UNITY_END();
}

void loop() {
    digitalWrite(13, HIGH);
    delay(100);
    digitalWrite(13, LOW);
    delay(500);
}
