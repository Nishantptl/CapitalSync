package com.nishant.capitalsync.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nishant.capitalsync.R

val Nunito = FontFamily(
    Font(R.font.nunito_extralight, FontWeight.ExtraLight),
    Font(R.font.nunito_light, FontWeight.Light),
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_semibold, FontWeight.SemiBold),
    Font(R.font.nunito_bold, FontWeight.Bold)
)

private val defaultTypography = Typography()

val AppTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = Nunito),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = Nunito),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = Nunito),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = Nunito),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = Nunito),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = Nunito),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = Nunito),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = Nunito),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = Nunito),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = Nunito),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = Nunito),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = Nunito),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = Nunito),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = Nunito),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = Nunito)
)

@Preview(showBackground = true)
@Composable
fun TypographyPreview() {
    MaterialTheme(typography = AppTypography) {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Weights Preview:", style = MaterialTheme.typography.titleLarge)
                Text("ExtraLight (200)", fontFamily = Nunito, fontWeight = FontWeight.ExtraLight)
                Text("Light (300)", fontFamily = Nunito, fontWeight = FontWeight.Light)
                Text("Normal (400)", fontFamily = Nunito, fontWeight = FontWeight.Normal)
                Text("Medium (500)", fontFamily = Nunito, fontWeight = FontWeight.Medium)
                Text("SemiBold (600)", fontFamily = Nunito, fontWeight = FontWeight.SemiBold)
                Text("Bold (700)", fontFamily = Nunito, fontWeight = FontWeight.Bold)

                Text("\nMaterial Typography:", style = MaterialTheme.typography.titleLarge)
                Text("displayLarge", style = MaterialTheme.typography.displayLarge)
                Text("displayMedium", style = MaterialTheme.typography.displayMedium)
                Text("displaySmall", style = MaterialTheme.typography.displaySmall)
                Text("headlineLarge", style = MaterialTheme.typography.headlineLarge)
                Text("headlineMedium", style = MaterialTheme.typography.headlineMedium)
                Text("headlineSmall", style = MaterialTheme.typography.headlineSmall)
                Text("titleLarge", style = MaterialTheme.typography.titleLarge)
                Text("titleMedium", style = MaterialTheme.typography.titleMedium)
                Text("titleSmall", style = MaterialTheme.typography.titleSmall)
                Text("bodyLarge", style = MaterialTheme.typography.bodyLarge)
                Text("bodyMedium", style = MaterialTheme.typography.bodyMedium)
                Text("bodySmall", style = MaterialTheme.typography.bodySmall)
                Text("labelLarge", style = MaterialTheme.typography.labelLarge)
                Text("labelMedium", style = MaterialTheme.typography.labelMedium)
                Text("labelSmall", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}