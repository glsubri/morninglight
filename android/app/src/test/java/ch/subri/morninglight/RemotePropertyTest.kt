package ch.subri.morninglight

import app.cash.turbine.test
import ch.subri.morninglight.ui.entity.RemoteProperty
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.junit.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class RemotePropertyTest {

    @Test
    fun canReadSetValue() {
        val remote: Flow<Int> = flow { }

        val remoteProperty = RemoteProperty(remote = remote)

        CoroutineScope(Dispatchers.Unconfined).launch {
            remoteProperty.setValue(1)
            delay(100)
            remoteProperty.setValue(2)
        }

        runBlocking {
            remoteProperty.value.test {
                assertEquals(1, awaitItem())
                assertEquals(2, awaitItem())
            }
        }
    }

    @Test
    fun canReadRemote() {
        val remote: Flow<Int> = flowOf(1, 2)
            .map {
                delay(100)
                it
            }

        val remoteProperty = RemoteProperty(
            remote = remote,
            invalidateRemoteFor = 0L,
        )

        runBlocking {
            remoteProperty.value.test {
                assertEquals(1, awaitItem())
                assertEquals(2, awaitItem())
            }
        }
    }

    @Test
    fun canReadValueAndRemote() {
        val remote: Flow<Int> = flowOf(1, 2)
            .map {
                delay(200)
                it
            }

        val remoteProperty = RemoteProperty(
            remote = remote,
            invalidateRemoteFor = 0L,
        )

        CoroutineScope(Dispatchers.Unconfined).launch {
            remoteProperty.setValue(1)
            delay(150)
            remoteProperty.setValue(2)
        }

        runBlocking {
            remoteProperty.value.test {
                assertEquals(1, awaitItem())
                assertEquals(2, awaitItem())
                assertEquals(1, awaitItem())
                assertEquals(2, awaitItem())
            }
        }
    }

    @Test
    fun canDebounceToRemote() {
        val remote: Flow<Int> = flow {}

        val remoteProperty = RemoteProperty(
            remote = remote,
            debounceLocalFor = 200L,
        )

        CoroutineScope(Dispatchers.Unconfined).launch {
            delay(100)
            remoteProperty.setValue(1)
            delay(210)
            remoteProperty.setValue(2)
            delay(100)
            remoteProperty.setValue(3)
            delay(100)
            remoteProperty.setValue(4)
            delay(100)
            remoteProperty.setValue(5)
        }

        runBlocking {
            remoteProperty.toRemote.test {
                assertEquals(1, awaitItem())
                assertEquals(5, awaitItem())
            }
        }
    }

    @Test
    fun canInvalidateRemote() {
        val remote: Flow<Int> = flowOf(2, 4, 5)
            .map {
                delay(200)
                it
            }

        val remoteProperty = RemoteProperty(
            remote = remote,
            invalidateRemoteFor = 150L,
            debounceLocalFor = 0L,
        )

        CoroutineScope(Dispatchers.Unconfined).launch {
            remoteProperty.setValue(1)
            delay(350)
            remoteProperty.setValue(3)
        }

        runBlocking {
            remoteProperty.value.test {
                assertEquals(1, awaitItem())
                assertEquals(2, awaitItem())
                assertEquals(3, awaitItem())
                assertEquals(5, awaitItem())
            }
        }
    }
}