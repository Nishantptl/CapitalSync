package com.nishant.capitalsync.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nishant.capitalsync.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapitalSyncTopAppBar(
    title: String,
    onNavigateToProfile: () -> Unit,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column(verticalArrangement = Arrangement.Center) {
                if (subtitle != null) {
                    CapitalSyncText(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    // Empty spacer text to maintain the same title baseline as dual-line headers
                    CapitalSyncText(
                        text = " ",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                CapitalSyncText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        actions = {
            IconButton(
                onClick = onNavigateToProfile,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.settings_24px),
                    contentDescription = stringResource(R.string.profile_icon_desc),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier
    )
}