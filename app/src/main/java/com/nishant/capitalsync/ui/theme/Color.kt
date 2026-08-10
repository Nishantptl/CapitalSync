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

val primaryLight = Color(0xFF000000)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFF1B1B1B)
val onPrimaryContainerLight = Color(0xFF848484)
val secondaryLight = Color(0xFF5D5F5F)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFFFFFFF)
val onSecondaryContainerLight = Color(0xFF747676)
val tertiaryLight = Color(0xFF5D5E5F)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFCCCCCC)
val onTertiaryContainerLight = Color(0xFF555657)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF93000A)
val backgroundLight = Color(0xFFF9F9F9)
val onBackgroundLight = Color(0xFF1B1B1B)
val surfaceLight = Color(0xFFFCF8F8)
val onSurfaceLight = Color(0xFF1C1B1B)
val surfaceVariantLight = Color(0xFFE0E3E3)
val onSurfaceVariantLight = Color(0xFF444748)
val outlineLight = Color(0xFF747878)
val outlineVariantLight = Color(0xFFC4C7C7)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF313030)
val inverseOnSurfaceLight = Color(0xFFF4F0EF)
val inversePrimaryLight = Color(0xFFC6C6C6)
val surfaceDimLight = Color(0xFFDDD9D9)
val surfaceBrightLight = Color(0xFFFCF8F8)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF7F3F2)
val surfaceContainerLight = Color(0xFFF1EDEC)
val surfaceContainerHighLight = Color(0xFFEBE7E7)
val surfaceContainerHighestLight = Color(0xFFE5E2E1)

val primaryDark = Color(0xFFC6C6C6)
val onPrimaryDark = Color(0xFF303030)
val primaryContainerDark = Color(0xFF000000)
val onPrimaryContainerDark = Color(0xFF757575)
val secondaryDark = Color(0xFFFFFFFF)
val onSecondaryDark = Color(0xFF2F3131)
val secondaryContainerDark = Color(0xFFE2E2E2)
val onSecondaryContainerDark = Color(0xFF636565)
val tertiaryDark = Color(0xFFE8E8E8)
val onTertiaryDark = Color(0xFF2F3131)
val tertiaryContainerDark = Color(0xFFCCCCCC)
val onTertiaryContainerDark = Color(0xFF555657)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF131313)
val onBackgroundDark = Color(0xFFE2E2E2)
val surfaceDark = Color(0xFF141313)
val onSurfaceDark = Color(0xFFE5E2E1)
val surfaceVariantDark = Color(0xFF444748)
val onSurfaceVariantDark = Color(0xFFC4C7C7)
val outlineDark = Color(0xFF8E9192)
val outlineVariantDark = Color(0xFF444748)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE5E2E1)
val inverseOnSurfaceDark = Color(0xFF313030)
val inversePrimaryDark = Color(0xFF5E5E5E)
val surfaceDimDark = Color(0xFF141313)
val surfaceBrightDark = Color(0xFF3A3939)
val surfaceContainerLowestDark = Color(0xFF0E0E0E)
val surfaceContainerLowDark = Color(0xFF1C1B1B)
val surfaceContainerDark = Color(0xFF201F1F)
val surfaceContainerHighDark = Color(0xFF2A2A2A)
val surfaceContainerHighestDark = Color(0xFF353434)

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

    Surface(color = Color(0xFF121212)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(8.dp)
        ) {
            items(darkColors) { (name, color) -> ColorPreviewBox(name, color, isDark = true) }
        }
    }
}