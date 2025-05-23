package com.moviles.clothingapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.moviles.clothingapp.R

/* Here are retrieved the fonts installed in res to use in views. */
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

)

val dmSansFamily = FontFamily(
    Font(R.font.dmsans_bold, FontWeight.Bold),
    Font(R.font.dmsans_light, FontWeight.Light),
    Font(R.font.dmsans_medium, FontWeight.Medium),
    Font(R.font.dmsans_regular, FontWeight.Normal),
    Font(R.font.dmsans_semibold, FontWeight.SemiBold)
)


val figtreeFamily = FontFamily(
    Font(R.font.figtree_bold, FontWeight.Bold),
    Font(R.font.figtree_light, FontWeight.Light),
    Font(R.font.figtree_medium, FontWeight.Medium),
    Font(R.font.figtree_regular, FontWeight.Normal),
    Font(R.font.figtree_semibold, FontWeight.SemiBold)
)