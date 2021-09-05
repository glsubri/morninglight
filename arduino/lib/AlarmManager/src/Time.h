#ifndef START_TIME_H
#define START_TIME_H

#include <stdint.h>

/**
 * @brief Sime data class used to store the time
 *
 */
class Time {
   public:
    /**
     * @brief Construct a new Time object with default values
     *
     */
    Time();

    /**
     * @brief Construct a new Time object with specific values
     *
     * Note: If either parameter is outside the boudaries, a default value of 0 will be set.
     *
     * @param hour The hour of the time. Bounded by [0-23].
     * @param minute The minute of the time. Bounded by [0-59].
     */
    Time(unsigned short hour, unsigned short minute);

    // Definition of == operator
    bool operator==(const Time& other) const { return this->equals(other); }

    // Definition of != operator
    bool operator!=(const Time& other) const { return !this->equals(other); }

    /**
     * @brief The hour of Time instance.
     *
     * Is public because Time is just a dataclass
     *
     */
    unsigned short hour;

    /**
     * @brief The minutes of Time instance.
     *
     * Is public because Time is just a dataclass
     *
     */
    unsigned short minute;

    /**
     * @brief Used to get a Time from a byte array
     *
     * @param bytes The bytes to convert to a Time. Should have a length of 2 bytes
     * @return Time The Time represented by the given bytes
     */
    static Time fromBytes(const uint8_t* bytes);

   private:
    /**
     * @brief Static method used to check that parameters in constructor are correct.
     *
     * @param n The value to check
     * @param upperBound The upper bound
     * @param defaultValue The default value to return if n > upperBound
     * @return unsigned short n or default
     */
    static unsigned short inBoundsOrDefault(unsigned short n, unsigned short upperBound,
                                            unsigned short defaultValue = 0);

    /**
     * @brief Defines equality between current Time instance and another Time instance
     *
     * @param other The other Time instance to compare with
     * @return true If both instances are equal
     * @return false If they are not equal
     */
    bool equals(const Time& other) const;
};

#endif