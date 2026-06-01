package com.tonyxlab.pagekeeper.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tonyxlab.pagekeeper.R

private val InterFontFamily = FontFamily(
        Font(R.font.inter_18_regular, FontWeight.Normal),
        Font(R.font.inter_18_medium, FontWeight.Medium)
)

private val LoraFontFamily = FontFamily(
        Font(R.font.lora_medium, FontWeight.Medium),
        Font(R.font.lora_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
        bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
        ),
        /*Other default text styles to override
        titleLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp
        )*/

)

object ExtendedTypography {

    val TitleLargeBold = TextStyle(
            fontFamily = LoraFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            lineHeight = 30.sp
    )

    val TitleMediumMedium = TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp
    )

    val TitleSmallMedium = TextStyle(
            fontFamily = LoraFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp,
            lineHeight = 20.sp
    )

    val BodyLargeRegular = TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
    )

    val BodyMediumMedium = TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 18.sp
    )

    val BodyMediumRegular = TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 18.sp
    )

    val BodySmallRegular = TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            lineHeight = 16.sp
    )
}

val Typography.TitleLargeBold
    get() = ExtendedTypography.TitleLargeBold

val Typography.TitleMediumMedium
    get() = ExtendedTypography.TitleMediumMedium

val Typography.TitleSmallMedium
    get() = ExtendedTypography.TitleSmallMedium

val Typography.BodyLargeRegular
    get() = ExtendedTypography.BodyLargeRegular

val Typography.BodyMediumMedium
    get() = ExtendedTypography.BodyMediumMedium

val Typography.BodyMediumRegular
    get() = ExtendedTypography.BodyMediumRegular

val Typography.BodySmallRegular
    get() = ExtendedTypography.BodySmallRegular
