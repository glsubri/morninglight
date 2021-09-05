package ch.subri.morninglight.ui.screens.setup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.subri.morninglight.data.entity.MCU
import ch.subri.morninglight.ui.theme.MorningLightTypography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceItem(
    mcu: MCU,
    onMCUClick: (MCU) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = 4.dp,
        onClick = { onMCUClick(mcu) },
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (mcu.name.isNullOrBlank()) {
                Text(
                    text = "No name",
                    style = MorningLightTypography.overline.copy(
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Italic
                    ),
                )
            } else {
                Text(
                    text = mcu.name,
                    style = MorningLightTypography.overline.copy(fontSize = 24.sp),
                )
            }
            Text(
                text = mcu.address,
                style = MorningLightTypography.overline,
            )
        }
    }
}

@Preview
@Composable
fun DeviceItemPreview() {
    val mcu = MCU(name = "My device", address = "00:00:xx:23")
    DeviceItem(
        mcu = mcu,
        onMCUClick = {},
    )
}