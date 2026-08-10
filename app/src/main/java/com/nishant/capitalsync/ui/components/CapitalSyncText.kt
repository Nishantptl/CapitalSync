package com.nishant.capitalsync.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nishant.capitalsync.ui.theme.AppTheme
import com.nishant.capitalsync.ui.theme.Nunito

@Composable
fun CapitalSyncText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontFamily: FontFamily = Nunito
) {
    val finalColor = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.onBackground

    Text(
        text = text,
        modifier = modifier,
        color = finalColor,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow,
        style = style
    )
}

@Composable
fun CapitalSyncHeading(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null
) {
    CapitalSyncText(
        text = text,
        modifier = modifier,
        color = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        textAlign = textAlign,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Preview(showBackground = true)
@Composable
fun CapitalSyncTextPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CapitalSyncHeading(text = "This is a Heading")
            CapitalSyncText(
                text = "This is some standard body text that demonstrates how normal text looks inside the application.",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}