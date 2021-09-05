package ch.subri.morninglight

import ch.subri.morninglight.data.api.ble.toByteArray
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import java.util.*

class BLEApiTest {

    private fun String.fromHEX() = this
        .chunked(2)
        .map {  it.toInt(16).toByte()}
        .toByteArray()

    @Test
    fun calendar_to_byteArray_birthday_is_correct() {
        val expected = "C9070A1D101E00050000".fromHEX()

        val calendar = Calendar.getInstance()
        calendar.set( 1993, 9, 29, 16, 30, 0)

        val actual = calendar.toByteArray()

        assertArrayEquals(expected, actual)
    }
}