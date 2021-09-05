package ch.subri.morninglight.ui.material

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.R
import ch.subri.morninglight.ui.navigation.Alarms
import ch.subri.morninglight.ui.navigation.Destination
import ch.subri.morninglight.ui.navigation.Light
import ch.subri.morninglight.ui.navigation.Settings
import ch.subri.morninglight.ui.theme.MorningLightColors

private data class NavigationItem(
    val destination: Destination,
    val painterResource: Int,
    val labelResource: Int,
)

@Composable
fun NavigationBottomBar(
    current: Destination,
    navigateTo: (Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        NavigationItem(Light, R.drawable.ic_lightbulb_outline, R.string.navigation_bar_light),
        NavigationItem(Alarms, R.drawable.ic_alarms, R.string.navigation_bar_alarms),
        NavigationItem(Settings, R.drawable.ic_cog_outline, R.string.navigation_bar_settings),
    )

    BottomNavigation(
        modifier = modifier,
        elevation = 2.dp,
    ) {
        items.forEach {
            BottomNavigationItem(
                selected = current == it.destination,
                onClick = { navigateTo(it.destination) },
                icon = { Icon(painterResource(it.painterResource), null) },
                label = { Text(stringResource(it.labelResource)) },
                selectedContentColor = MaterialTheme.colors.primaryVariant,
                unselectedContentColor = MorningLightColors.PolarNight100,
            )
        }
    }
}
