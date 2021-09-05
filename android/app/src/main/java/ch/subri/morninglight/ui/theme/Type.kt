package ch.subri.morninglight.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ch.subri.morninglight.R

val PlexSans = FontFamily(
    Font(R.font.ibmplexsans_bold, FontWeight.Bold),
    Font(R.font.ibmplexsans_semibold, FontWeight.SemiBold),
    Font(R.font.ibmplexsans_medium, FontWeight.Medium),
    Font(R.font.ibmplexsans_regular, FontWeight.Normal),
    Font(R.font.ibmplexsans_light, FontWeight.Light),
    Font(R.font.ibmplexsans_extralight, FontWeight.ExtraLight),
    Font(R.font.ibmplexsans_thin, FontWeight.Thin),
)

val PlexMono = FontFamily(
    Font(R.font.ibmplexmono_bold, FontWeight.Bold),
    Font(R.font.ibmplexmono_semibold, FontWeight.SemiBold),
    Font(R.font.ibmplexmono_medium, FontWeight.Medium),
    Font(R.font.ibmplexmono_regular, FontWeight.Normal),
    Font(R.font.ibmplexmono_light, FontWeight.Light),
    Font(R.font.ibmplexmono_extralight, FontWeight.ExtraLight),
    Font(R.font.ibmplexmono_thin, FontWeight.Thin),
)

val MorningLightTypography = Typography(
    defaultFontFamily = PlexSans,
    h1 = TextStyle(fontFamily = PlexSans, fontSize = 96.sp, fontWeight = FontWeight.Medium),
    h2 = TextStyle(fontFamily = PlexSans, fontSize = 60.sp, fontWeight = FontWeight.SemiBold),
    h3 = TextStyle(fontFamily = PlexSans, fontSize = 48.sp, fontWeight = FontWeight.Bold),
    h4 = TextStyle(fontFamily = PlexSans, fontSize = 34.sp, fontWeight = FontWeight.Bold),
    h5 = TextStyle(fontFamily = PlexSans, fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
    h6 = TextStyle(fontFamily = PlexSans, fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    subtitle1 = TextStyle(
        fontFamily = PlexSans,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    subtitle2 = TextStyle(
        fontFamily = PlexSans,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    body1 = TextStyle(fontFamily = PlexSans, fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    body2 = TextStyle(fontFamily = PlexSans, fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
    button = TextStyle(
        fontFamily = PlexMono,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
    ),
    caption = TextStyle(
        fontFamily = PlexSans,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
    ),
    overline = TextStyle(
        fontFamily = PlexMono,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.025.sp
    )
)