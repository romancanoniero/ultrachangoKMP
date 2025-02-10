package com.iyr.ultrachango.ui.theme


import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import androidx.compose.material3.Typography
import ultrachango2.composeapp.generated.resources.FontsFree_Net_SFProText_Bold_1
import ultrachango2.composeapp.generated.resources.FontsFree_Net_SFProText_Heavy_1
import ultrachango2.composeapp.generated.resources.FontsFree_Net_SFProText_Medium_1
import ultrachango2.composeapp.generated.resources.FontsFree_Net_SFProText_Regular_1
import ultrachango2.composeapp.generated.resources.FontsFree_Net_SFProText_Semibold
import ultrachango2.composeapp.generated.resources.FontsFree_Net_SF_Pro_Rounded_Thin
import ultrachango2.composeapp.generated.resources.Res


@OptIn(ExperimentalResourceApi::class)
@Composable
fun SFProMediumFontFamily() = FontFamily(
    Font(Res.font.FontsFree_Net_SF_Pro_Rounded_Thin, weight = FontWeight.Light),
    Font(Res.font.FontsFree_Net_SFProText_Regular_1, weight = FontWeight.Normal),
    Font(Res.font.FontsFree_Net_SFProText_Medium_1, weight = FontWeight.Medium),
    Font(Res.font.FontsFree_Net_SFProText_Semibold, weight = FontWeight.SemiBold),
    Font(Res.font.FontsFree_Net_SFProText_Bold_1, weight = FontWeight.Bold),
    Font(Res.font.FontsFree_Net_SFProText_Heavy_1, weight = FontWeight.Black)

)

@Composable
fun SFProMediumTypography() = Typography().run {

    val fontFamily = SFProMediumFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily =  fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}