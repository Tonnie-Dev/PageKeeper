package com.tonyxlab.pagekeeper.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val            MaterialShapes = Shapes(
        // Pre-Defined M3 Shapes
        extraSmall = ShapeDefaults.ExtraSmall, //RoundedCornerShape(4.dp)
        small = ShapeDefaults.Small, //RoundedCornerShape(8.dp)
        medium = ShapeDefaults.Medium, //RoundedCornerShape(12.dp)
        large = ShapeDefaults.Large, //RoundedCornerShape(16.dp)
        extraLarge = ShapeDefaults.ExtraLarge //RoundedCornerShape(28.dp)
)

object ExtendedShapes {
    val RoundedCornerShape4 = RoundedCornerShape(4.dp)
    val RoundedCornerShape10 = RoundedCornerShape(10.dp)
    val RoundedCornerShape20 = RoundedCornerShape(20.dp)
    val RoundedCornerShape100 = RoundedCornerShape(percent = 100)


    val StartVerticalRoundedCornerShape100 =
        RoundedCornerShape(topStart = 100.dp, bottomStart = 100.dp)
    val EndVerticalRoundedCornerShape100 =
        RoundedCornerShape(topEnd = 100.dp, bottomEnd = 100.dp)

    val HorizontalRoundedCornerShape28 = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    val EndVerticalRoundedCornerShape16 =
        RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)

}

val Shapes.RoundedCornerShape4
    @Composable
    get() = ExtendedShapes.RoundedCornerShape4

val Shapes.RoundedCornerShape10
    @Composable
    get() = ExtendedShapes.RoundedCornerShape10

val Shapes.RoundedCornerShape20
    @Composable
    get() = ExtendedShapes.RoundedCornerShape20

val Shapes.RoundedCornerShape100
    @Composable
    get() = ExtendedShapes.RoundedCornerShape100






