package ch.subri.morninglight.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.subri.morninglight.ui.compositionLocal.LocalAlarmDAO
import ch.subri.morninglight.ui.compositionLocal.LocalBLEApi
import ch.subri.morninglight.ui.compositionLocal.LocalDeviceScanner
import ch.subri.morninglight.ui.compositionLocal.LocalPreferences
import ch.subri.morninglight.ui.navigation.MainNavigation
import ch.subri.morninglight.ui.screens.onboarding.Onboarding
import ch.subri.morninglight.ui.screens.setup.Setup
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MorningLightApp(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = viewModel(),
) {
    val stage = viewModel.stage.collectAsState(initial = ApplicationStage.Onboarding).value

    val locationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    // TODO: This does not follow android permission guidelines
    // https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions
    LaunchedEffect(!locationPermissionState.hasPermission) {
        locationPermissionState.launchPermissionRequest()
    }

    when {
        locationPermissionState.hasPermission ->
            CompositionLocalProvider(
                LocalPreferences provides viewModel.preferences,
                LocalDeviceScanner provides viewModel.deviceScanner,
            ) {
                Crossfade(targetState = stage) {
                    when (it) {
                        // Minimal BLE support
                        ApplicationStage.Onboarding -> Onboarding(modifier = modifier)
                        ApplicationStage.Configuration -> Setup(modifier = modifier)

                        // Full BLE support
                        ApplicationStage.Main -> {
                            CompositionLocalProvider(
                                LocalBLEApi provides viewModel.bleApi.value!!,
                                LocalAlarmDAO provides viewModel.alarmDao,
                            ) {
                                MainNavigation(modifier = modifier)
                            }
                        }
                    }
                }
            }
        else -> Onboarding()
    }
}
