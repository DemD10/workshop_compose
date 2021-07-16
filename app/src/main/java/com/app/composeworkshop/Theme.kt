package com.app.composeworkshop

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {

    val shapes = Shapes(
        small = RoundedCornerShape(16.dp)
    )

    val typography = Typography(
        h1 = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = 12.sp
        ),
        h2 = TextStyle(
            fontWeight = FontWeight.W600,
            fontSize = 22.sp
        )
    )

    MaterialTheme(
        colors = darkColors(),
        shapes = shapes,
        typography = typography,
        content = content
    )
}
