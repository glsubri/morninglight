package ch.subri.morninglight.ui.screens.alarmEdition

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ch.subri.morninglight.ui.theme.MorningLightColors
import ch.subri.morninglight.ui.theme.MorningLightTypography

@Composable
fun BottomBar(
    deleteEnable: Boolean,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (deleteEnable) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onDeleteClick() }
                    .weight(1f)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MorningLightColors.AuroraRed,
                    modifier = Modifier.padding(end = 8.dp),
                )

                Text(
                    text = "Delete".uppercase(),
                    style = MorningLightTypography.button.copy(
                        color = MorningLightColors.AuroraRed,
                    )
                )
            }
        } else {
            Spacer(modifier.weight(1f))
        }

        Row(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onSaveClick() }
                .weight(1f)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
            )

            Text(
                text = "Save".uppercase(),
                style = MaterialTheme.typography.button
            )
        }
    }
}