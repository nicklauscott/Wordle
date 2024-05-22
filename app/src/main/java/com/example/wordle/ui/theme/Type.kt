package com.example.wordle.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.wordle.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = customFontFamily[0],
        fontSize = 28.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = customFontFamily[1],
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = customFontFamily[2],
        fontSize = 18.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    )

)

val customFontFamily: List<FontFamily>
    get() = listOf(
        FontFamily(Font(R.font.nationale_bold, FontWeight.Bold)),
        FontFamily(Font(R.font.nationale_black, FontWeight.Black)),
        FontFamily(Font(R.font.nationale_demi_bold, FontWeight.SemiBold)),
        FontFamily(Font(R.font.nationale_italic, FontWeight.Thin)),
        FontFamily(Font(R.font.nationale_light, FontWeight.Light)),
        FontFamily(Font(R.font.nationale_medium, FontWeight.Medium)),
        FontFamily(Font(R.font.nationale_regular, FontWeight.Normal))
    )
