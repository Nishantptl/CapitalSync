package com.nishant.capitalsync.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ExpenseColor = Color(0xFFC10007)
val IncomeColor = Color(0xFF5EA500)

val CategoryFoodDining = Color(0xFFFF6900)
val CategoryTransportation = Color(0xFFF0B100)
val CategoryHouseUtilities = Color(0xFF51A2FF)
val CategoryShopping = Color(0xFFA684FF)
val CategoryEntertainment = Color(0xFFED6AFF)
val CategoryHealthFitness = Color(0xFFFF637E)
val CategoryOthers = Color(0xFF00D5BE)

val CategoryActiveIncome = Color(0xFF009966)
val CategoryPassiveIncome = Color(0xFF00B8DB)

val primaryLight = Color(0xFF006C47)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFF89F8C1)
val onPrimaryContainerLight = Color(0xFF002113)
val secondaryLight = Color(0xFF4D6355)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFCFE9D6)
val onSecondaryContainerLight = Color(0xFF0A1F14)
val tertiaryLight = Color(0xFF3C6473)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFC0E9FA)
val onTertiaryContainerLight = Color(0xFF001F29)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFF5FBF6)
val onBackgroundLight = Color(0xFF171D19)
val surfaceLight = Color(0xFFF5FBF6)
val onSurfaceLight = Color(0xFF171D19)
val surfaceVariantLight = Color(0xFFDCE5DD)
val onSurfaceVariantLight = Color(0xFF404943)
val outlineLight = Color(0xFF707973)
val outlineVariantLight = Color(0xFFC0C9C1)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2C322E)
val inverseOnSurfaceLight = Color(0xFFEDF2EC)
val inversePrimaryLight = Color(0xFF6CDBA6)
val surfaceDimLight = Color(0xFFD5DBD6)
val surfaceBrightLight = Color(0xFFF5FBF6)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFEFF5F0)
val surfaceContainerLight = Color(0xFFE9EFEB)
val surfaceContainerHighLight = Color(0xFFE3E9E5)
val surfaceContainerHighestLight = Color(0xFFDDE4DF)

val primaryDark = Color(0xFF6CDBA6)
val onPrimaryDark = Color(0xFF003823)
val primaryContainerDark = Color(0xFF005234)
val onPrimaryContainerDark = Color(0xFF89F8C1)
val secondaryDark = Color(0xFFB3CCBB)
val onSecondaryDark = Color(0xFF1F3528)
val secondaryContainerDark = Color(0xFF354B3E)
val onSecondaryContainerDark = Color(0xFFCFE9D6)
val tertiaryDark = Color(0xFFA4CDDD)
val onTertiaryDark = Color(0xFF053543)
val tertiaryContainerDark = Color(0xFF234C5A)
val onTertiaryContainerDark = Color(0xFFC0E9FA)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF0E1512)
val onBackgroundDark = Color(0xFFDDE4DF)
val surfaceDark = Color(0xFF0E1512)
val onSurfaceDark = Color(0xFFDDE4DF)
val surfaceVariantDark = Color(0xFF404943)
val onSurfaceVariantDark = Color(0xFFC0C9C1)
val outlineDark = Color(0xFF8A938C)
val outlineVariantDark = Color(0xFF404943)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFDDE4DF)
val inverseOnSurfaceDark = Color(0xFF2C322E)
val inversePrimaryDark = Color(0xFF006C47)
val surfaceDimDark = Color(0xFF0E1512)
val surfaceBrightDark = Color(0xFF343B37)
val surfaceContainerLowestDark = Color(0xFF090F0D)
val surfaceContainerLowDark = Color(0xFF171D1A)
val surfaceContainerDark = Color(0xFF1B211E)
val surfaceContainerHighDark = Color(0xFF252C28)
val surfaceContainerHighestDark = Color(0xFF303733)

@Composable
private fun ColorPreviewBox(name: String, color: Color, isDark: Boolean = false) {
    Column(
        modifier = Modifier.padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color, RoundedCornerShape(8.dp))
        )
        Text(
            text = name.replace("Light", "").replace("Dark", ""),
            fontSize = 11.sp,
            color = if (isDark) Color.White else Color.Black,
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 2,
            lineHeight = 14.sp
        )
    }
}

@Preview(showBackground = true, name = "Light Colors Palette")
@Composable
fun LightColorsPalettePreview() {
    val lightColors = listOf(
        "primaryLight" to primaryLight, "onPrimaryLight" to onPrimaryLight,
        "primaryContainerLight" to primaryContainerLight, "onPrimaryContainerLight" to onPrimaryContainerLight,
        "secondaryLight" to secondaryLight, "onSecondaryLight" to onSecondaryLight,
        "secondaryContainerLight" to secondaryContainerLight, "onSecondaryContainerLight" to onSecondaryContainerLight,
        "tertiaryLight" to tertiaryLight, "onTertiaryLight" to onTertiaryLight,
        "tertiaryContainerLight" to tertiaryContainerLight, "onTertiaryContainerLight" to onTertiaryContainerLight,
        "errorLight" to errorLight, "onErrorLight" to onErrorLight,
        "errorContainerLight" to errorContainerLight, "onErrorContainerLight" to onErrorContainerLight,
        "backgroundLight" to backgroundLight, "onBackgroundLight" to onBackgroundLight,
        "surfaceLight" to surfaceLight, "onSurfaceLight" to onSurfaceLight,
        "surfaceVariantLight" to surfaceVariantLight, "onSurfaceVariantLight" to onSurfaceVariantLight,
        "outlineLight" to outlineLight, "outlineVariantLight" to outlineVariantLight,
        "scrimLight" to scrimLight, "inverseSurfaceLight" to inverseSurfaceLight,
        "inverseOnSurfaceLight" to inverseOnSurfaceLight, "inversePrimaryLight" to inversePrimaryLight,
        "surfaceDimLight" to surfaceDimLight, "surfaceBrightLight" to surfaceBrightLight,
        "surfaceContainerLowestLight" to surfaceContainerLowestLight, "surfaceContainerLowLight" to surfaceContainerLowLight,
        "surfaceContainerLight" to surfaceContainerLight, "surfaceContainerHighLight" to surfaceContainerHighLight,
        "surfaceContainerHighestLight" to surfaceContainerHighestLight
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(8.dp)
    ) {
        items(lightColors) { (name, color) -> ColorPreviewBox(name, color) }
    }
}

@Preview(showBackground = true, name = "Dark Colors Palette")
@Composable
fun DarkColorsPalettePreview() {
    val darkColors = listOf(
        "primaryDark" to primaryDark, "onPrimaryDark" to onPrimaryDark,
        "primaryContainerDark" to primaryContainerDark, "onPrimaryContainerDark" to onPrimaryContainerDark,
        "secondaryDark" to secondaryDark, "onSecondaryDark" to onSecondaryDark,
        "secondaryContainerDark" to secondaryContainerDark, "onSecondaryContainerDark" to onSecondaryContainerDark,
        "tertiaryDark" to tertiaryDark, "onTertiaryDark" to onTertiaryDark,
        "tertiaryContainerDark" to tertiaryContainerDark, "onTertiaryContainerDark" to onTertiaryContainerDark,
        "errorDark" to errorDark, "onErrorDark" to onErrorDark,
        "errorContainerDark" to errorContainerDark, "onErrorContainerDark" to onErrorContainerDark,
        "backgroundDark" to backgroundDark, "onBackgroundDark" to onBackgroundDark,
        "surfaceDark" to surfaceDark, "onSurfaceDark" to onSurfaceDark,
        "surfaceVariantDark" to surfaceVariantDark, "onSurfaceVariantDark" to onSurfaceVariantDark,
        "outlineDark" to outlineDark, "outlineVariantDark" to outlineVariantDark,
        "scrimDark" to scrimDark, "inverseSurfaceDark" to inverseSurfaceDark,
        "inverseOnSurfaceDark" to inverseOnSurfaceDark, "inversePrimaryDark" to inversePrimaryDark,
        "surfaceDimDark" to surfaceDimDark, "surfaceBrightDark" to surfaceBrightDark,
        "surfaceContainerLowestDark" to surfaceContainerLowestDark, "surfaceContainerLowDark" to surfaceContainerLowDark,
        "surfaceContainerDark" to surfaceContainerDark, "surfaceContainerHighDark" to surfaceContainerHighDark,
        "surfaceContainerHighestDark" to surfaceContainerHighestDark
    )

    Surface(color = backgroundDark) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(8.dp)
        ) {
            items(darkColors) { (name, color) -> ColorPreviewBox(name, color, isDark = true) }
        }
    }
}