package ch.subri.morninglight.ui.navigation

import android.content.Intent
import android.os.Parcelable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import ch.subri.morninglight.data.entity.DeviceState
import ch.subri.morninglight.data.services.BLEService
import ch.subri.morninglight.data.services.BoundServiceWrapper
import ch.subri.morninglight.ui.compositionLocal.LocalBLEApi
import ch.subri.morninglight.ui.entity.Alarm
import ch.subri.morninglight.ui.material.NavigationBottomBar
import ch.subri.morninglight.ui.screens.alarmEdition.AlarmEdition
import ch.subri.morninglight.ui.screens.alarms.Alarms
import ch.subri.morninglight.ui.screens.light.Light
import ch.subri.morninglight.ui.screens.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class Destination(val string: String) {
    override fun toString(): String {
        return string
    }
}

object Light : Destination("light")
object Alarms : Destination("alarms")
object Settings : Destination("settings")
object AlarmEdition : Destination("alarmEdition")

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
) {
    // BLE Service
    val bleService = remember { BoundServiceWrapper() }
    val context = LocalContext.current

    DisposableEffect(bleService) {
        val intent = Intent(context, BLEService::class.java)
        bleService.bind(context, intent)

        onDispose {
            bleService.unbind()
        }
    }

    // BLE Api
    val bleApi = LocalBLEApi.current
    val state = bleApi.state.collectAsState(initial = DeviceState.Connecting)
    val scope = rememberCoroutineScope { Dispatchers.IO }

    DisposableEffect(bleApi) {
        scope.launch { bleApi.connect() }

        onDispose { scope.launch { bleApi.disconnect() } }
    }


    // Navigation
    val navController = rememberNavController()

    fun navigateTo(destination: Destination) =
        navController.navigate("$destination") { launchSingleTop }

    /**
     * Returns a parcelable object and removes it from previousBackStackEntry
     *
     * @param T Type of the parcelable object to retrieve
     * @param key Key where the object is stored in the backStackEntry
     * @return The object pointed by key or null if not found
     */
    fun <T : Parcelable> getParcelable(key: String): T? {
        val value: T? = navController.previousBackStackEntry?.arguments?.getParcelable(key)
        navController.previousBackStackEntry?.arguments?.remove(key)

        return value
    }

    NavHost(
        navController = navController,
        startDestination = Light.toString(),
        modifier = modifier,
    ) {
        composable(route = Light.toString()) {
            Light(
                state = state.value,
                modifier = modifier,
                bottomBar = {
                    NavigationBottomBar(
                        current = Light,
                        navigateTo = { navigateTo(it) }
                    )
                }
            )
        }
        composable(route = Alarms.toString()) {
            Alarms(
                onNewAlarmClick = { navigateTo(AlarmEdition) },
                onEditAlarmClick = {
                    navController.currentBackStackEntry?.arguments?.putParcelable("alarm", it)
                    navigateTo(AlarmEdition)
                },
                modifier = modifier,
                bottomBar = {
                    NavigationBottomBar(
                        current = Alarms,
                        navigateTo = { navigateTo(it) },
                    )
                },
            )
        }
        composable(route = Settings.toString()) {
            Settings(
                modifier = modifier,
                bottomBar = {
                    NavigationBottomBar(
                        current = Settings,
                        navigateTo = { navigateTo(it) },
                    )
                },
            )
        }
        composable(
            route = "$AlarmEdition?alarm={alarm}",
            arguments = listOf(navArgument("alarm") { nullable = true }),
        ) {
            val alarm: Alarm? = getParcelable("alarm")

            AlarmEdition(
                onBackClick = { navController.popBackStack() },
                modifier = modifier,
                alarm = alarm,
            )
        }
    }
}