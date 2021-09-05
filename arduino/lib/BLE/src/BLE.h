#ifndef BLE_H
#define BLE_H

#include <string>

/**
 * @brief Name of the BLE device. Used for discovery. Setup in GAP.
 *
 */
static const std::string GAP_LOCAL_NAME = "Morning Light";

/**
 * @brief Appearance of the device per BLE specification. Setup in GAP.
 *
 * 0x07C0 represents a Generic Light Source
 *
 */
static uint16_t GAP_APPEARANCE = 0x07C0;

// UUID definition for self owned services and characteristics
//
//  00000000-0000-400c-9e40-1c3267e089b9
//  |------| |--| |--------------------|
//    |      |            |
//    |      |          BASE APP
//    |  SERVICES
//    |
//  CHARACTERISTICS
//
//

/**
 * @brief Base UUID of our entire BLE setup.
 *
 */
static const std::string BASE_UUID = "-400C-9E40-1C3267E089B9";

/**
 * @brief UUID of MondayAlarmService
 *
 */
static const std::string SERVICE_MONDAY_ALARM_UUID = "0001" + BASE_UUID;

/**
 * @brief UUID of TuesdayAlarmService
 *
 */
static const std::string SERVICE_TUESDAY_ALARM_UUID = "0002" + BASE_UUID;

/**
 * @brief UUID of WednesdayAlarmService
 *
 */
static const std::string SERVICE_WEDNESDAY_ALARM_UUID = "0003" + BASE_UUID;

/**
 * @brief UUID of ThursdayAlarmService
 *
 */
static const std::string SERVICE_THURSDAY_ALARM_UUID = "0004" + BASE_UUID;

/**
 * @brief UUID of FridayAlarmService
 *
 */
static const std::string SERVICE_FRIDAY_ALARM_UUID = "0005" + BASE_UUID;

/**
 * @brief UUID of SaturdayAlarmService
 *
 */
static const std::string SERVICE_SATURDAY_ALARM_UUID = "0006" + BASE_UUID;

/**
 * @brief UUID of SundayAlarmService
 *
 */
static const std::string SERVICE_SUNDAY_ALARM_UUID = "0007" + BASE_UUID;

/**
 * @brief UUID of our Current Light Service.
 *
 */
static const std::string SERVICE_CURRENT_LIGHT_UUID = "0008" + BASE_UUID;

/**
 * @brief UUID of our Night Light Service.
 *
 */
static const std::string SERVICE_NIGHT_LIGHT_UUID = "0009" + BASE_UUID;

#endif
