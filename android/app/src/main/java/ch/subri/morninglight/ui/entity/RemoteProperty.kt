package ch.subri.morninglight.ui.entity

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private const val invalidateRemoteForDefault = 500L
private const val debounceLocalForDefault = 300L

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class RemoteProperty<T>(
    remote: Flow<T>,
    invalidateRemoteFor: Long = invalidateRemoteForDefault,
    debounceLocalFor: Long = debounceLocalForDefault,
) {
    private var lastLocalUpdate: Long = System.currentTimeMillis()

    private val local: MutableStateFlow<T?> = MutableStateFlow(null)

    // We need to have a separate flow for the initial retrieve, otherwise, we would send
    // the value we got back to the MCU
    private val initial: MutableStateFlow<T?> = MutableStateFlow(null)
    private val debouncedRemote =
        remote.filter { System.currentTimeMillis() - lastLocalUpdate > invalidateRemoteFor }

    val toRemote: Flow<T> = local
        .filterNotNull()
        .debounce(debounceLocalFor)

    val value: Flow<T> = merge(local, initial, debouncedRemote)
        .distinctUntilChanged()
        .filterNotNull()

    fun setInitialValue(initialValue: T) {
        initial.value = initialValue
    }

    fun setValue(newValue: T) {
        lastLocalUpdate = System.currentTimeMillis()
        local.value = newValue
    }

    @Composable
    operator fun component1(): State<T?> = value.collectAsState(initial = null)

    operator fun component2(): (T) -> Unit = ::setValue
}

@OptIn(FlowPreview::class)
fun <T> CoroutineScope.remoteProperty(
    remote: Flow<T>,
    initialRetrieve: suspend () -> T?,
    updateRemote: suspend (T) -> Unit,
    invalidateRemoteFor: Long = invalidateRemoteForDefault,
    debounceLocalFor: Long = debounceLocalForDefault,
): RemoteProperty<T> {
    return RemoteProperty(
        remote,
        invalidateRemoteFor,
        debounceLocalFor
    ).also { remoteProperty ->
        launch(Dispatchers.IO) {
            // Send new values to remote
            launch {
                val toRemoteChan = remoteProperty.toRemote.produceIn(this)

                for (command in toRemoteChan) {
                    updateRemote(command)
                }
            }

            // Retrieve initial value
            initialRetrieve()?.let { remoteProperty.setInitialValue(it) }
        }
    }
}

@Composable
fun <T> rememberRemoteProperty(
    remote: Flow<T>,
    initialRetrieve: suspend () -> T?,
    updateRemote: suspend (T) -> Unit,
    invalidateRemoteFor: Long = invalidateRemoteForDefault,
    debounceLocalFor: Long = debounceLocalForDefault,
): RemoteProperty<T> {
    val scope = rememberCoroutineScope()
    return remember(scope) {
        scope.remoteProperty(
            remote,
            initialRetrieve,
            updateRemote,
            invalidateRemoteFor,
            debounceLocalFor,
        )
    }
}