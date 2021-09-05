#include <Arduino.h>
#include <unity.h>
#include <TimeManager.h>

void assert_equal_tm(tm expected, tm actual) {
  TEST_ASSERT_EQUAL_INT(expected.tm_sec, actual.tm_sec);
  TEST_ASSERT_EQUAL_INT(expected.tm_min, actual.tm_min);
  TEST_ASSERT_EQUAL_INT(expected.tm_hour, actual.tm_hour);
  TEST_ASSERT_EQUAL_INT(expected.tm_mday, actual.tm_mday);
  TEST_ASSERT_EQUAL_INT(expected.tm_mon, actual.tm_mon);
  TEST_ASSERT_EQUAL_INT(expected.tm_year, actual.tm_year);
}

void test_lower_bound() {
  struct tm expected = {
    .tm_sec = 0,
    .tm_min = 0,
    .tm_hour = 0,
    .tm_mday = 0,
    .tm_mon = 0,
    .tm_year = 0,
    .tm_wday = 0,
    .tm_yday = 0,
    .tm_isdst = 0,
  };
  tm actual = TimeManager::toDeviceTime(1850, -2, -3, -4, -5, -6);

  assert_equal_tm(expected, actual);
}

void test_upper_bound() {
  struct tm expected = {
    .tm_sec = 0,
    .tm_min = 0,
    .tm_hour = 0,
    .tm_mday = 0,
    .tm_mon = 0,
    .tm_year = 0,
    .tm_wday = 0,
    .tm_yday = 0,
    .tm_isdst = 0,
  };
  tm actual = TimeManager::toDeviceTime(2000, 100, 100, 100, 100, 100);

  assert_equal_tm(expected, actual);
}

void test_birthday() {
  struct tm expected = {
    .tm_sec = 45,
    .tm_min = 30,
    .tm_hour = 16,
    .tm_mday = 29,
    .tm_mon = 9,
    .tm_year = 93,
    .tm_wday = 0,
    .tm_yday = 0,
    .tm_isdst = 0,
  };
  tm actual = TimeManager::toDeviceTime(1993, 10, 29, 16, 30, 45);

  assert_equal_tm(expected, actual);
}

void setup() {
  delay(2000);

  UNITY_BEGIN();
  RUN_TEST(test_birthday);
  RUN_TEST(test_lower_bound);
  RUN_TEST(test_upper_bound);
  UNITY_END();
}

void loop() {
  digitalWrite(13, HIGH);
  delay(100);
  digitalWrite(13, LOW);
  delay(500);
}
