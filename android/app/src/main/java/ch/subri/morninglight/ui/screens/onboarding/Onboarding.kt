package ch.subri.morninglight.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.R
import ch.subri.morninglight.ui.theme.MorningLightColors
import ch.subri.morninglight.ui.theme.MorningLightTheme

@Composable
fun Onboarding(
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MorningLightColors.AuroraYellow,
        contentColor = MorningLightColors.PolarNight500,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Morning Light Icon",
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
            )
        }
    }
}

@Preview
@Composable
fun OnboardingPreview() = MorningLightTheme {
    Onboarding()
}