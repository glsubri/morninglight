#include <Arduino.h>
#include <LightManager.h>
#include <unity.h>

const int zcPin = 2;
const int pwmPin = 3;
const int buttonPin = 4;

void test_setIntensity_upper_bound() {
    LightManager light(pwmPin, zcPin, buttonPin);
    int intensity = 110;

    bool actual = light.setIntensity(intensity);

    TEST_ASSERT_FALSE(actual);
}

void test_setIntensity_lower_bound() {
    LightManager light(pwmPin, zcPin, buttonPin);
    int intensity = -10;

    bool actual = light.setIntensity(intensity);

    TEST_ASSERT_FALSE(actual);
}

void test_setIntensity_is_getIntensity() {
    LightManager light(pwmPin, zcPin, buttonPin);
    int intensity = 57;

    light.setIntensity(intensity);
    int actual = light.getIntensity();

    TEST_ASSERT_EQUAL_INT(intensity, actual);
}

void test_isTurnedOn_correct() {
    LightManager light(pwmPin, zcPin, buttonPin);
    LightManager::setInstance(light);

    int intensity = 57;
    light.setIntensity(intensity);

    light.turnOn();
    TEST_ASSERT_TRUE(light.isTurnedOn());

    light.turnOff();
    TEST_ASSERT_FALSE(light.isTurnedOn());
}

void setup() {
    delay(2000);

    UNITY_BEGIN();
    RUN_TEST(test_setIntensity_upper_bound);
    RUN_TEST(test_setIntensity_lower_bound);
    RUN_TEST(test_setIntensity_is_getIntensity);
    RUN_TEST(test_isTurnedOn_correct);
    UNITY_END();
}

void loop() {
    digitalWrite(13, HIGH);
    delay(100);
    digitalWrite(13, LOW);
    delay(500);
}
