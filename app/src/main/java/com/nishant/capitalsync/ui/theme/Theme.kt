package com.nishant.capitalsync.ui.theme

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "Light Theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Theme")
@Composable
fun AppThemePreview() {
    AppTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(text = "Material Colors", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                ColorItem("Primary", MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
                ColorItem("Primary Container", MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)

                ColorItem("Secondary", MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
                ColorItem("Secondary Container", MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)

                ColorItem("Tertiary", MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.onTertiary)
                ColorItem("Tertiary Container", MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)

                ColorItem("Error", MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError)
                ColorItem("Error Container", MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)

                ColorItem("Background", MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.onBackground)

                ColorItem("Surface", MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface)
                ColorItem("Surface Variant", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
                ColorItem("Surface Dim", MaterialTheme.colorScheme.surfaceDim, MaterialTheme.colorScheme.onSurface)
                ColorItem("Surface Bright", MaterialTheme.colorScheme.surfaceBright, MaterialTheme.colorScheme.onSurface)

                ColorItem("Surface Container Lowest", MaterialTheme.colorScheme.surfaceContainerLowest, MaterialTheme.colorScheme.onSurface)
                ColorItem("Surface Container Low", MaterialTheme.colorScheme.surfaceContainerLow, MaterialTheme.colorScheme.onSurface)
                ColorItem("Surface Container", MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.colorScheme.onSurface)
                ColorItem("Surface Container High", MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.colorScheme.onSurface)
                ColorItem("Surface Container Highest", MaterialTheme.colorScheme.surfaceContainerHighest, MaterialTheme.colorScheme.onSurface)

                ColorItem("Inverse Surface", MaterialTheme.colorScheme.inverseSurface, MaterialTheme.colorScheme.inverseOnSurface)
                ColorItem("Inverse Primary", MaterialTheme.colorScheme.inversePrimary, MaterialTheme.colorScheme.inverseSurface)

                ColorItem("Outline", MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.surface)
                ColorItem("Outline Variant", MaterialTheme.colorScheme.outlineVariant, MaterialTheme.colorScheme.onSurface)
                ColorItem("Scrim", MaterialTheme.colorScheme.scrim, Color.White)
            }
        }
    }
}

@Composable
private fun ColorItem(name: String, backgroundColor: Color, contentColor: Color) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.labelLarge)
        }
    }
}