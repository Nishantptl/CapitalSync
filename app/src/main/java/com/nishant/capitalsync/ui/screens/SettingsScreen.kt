package com.nishant.capitalsync.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nishant.capitalsync.R
import com.nishant.capitalsync.ui.components.CapitalSyncPrimaryButton
import com.nishant.capitalsync.ui.viewmodel.ProfileViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showEditBalanceSheet by remember { mutableStateOf(false) }
    var newBalanceValue by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showEditBalanceSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEditBalanceSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 48.dp, top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.update_total_balance),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(R.string.balance_adjustment_message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 32.dp, horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "$",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        BasicTextField(
                            value = newBalanceValue,
                            onValueChange = { input ->
                                if (input.isEmpty() || (input.count { it == '.' } <= 1 && input.all { it.isDigit() || it == '.' })) {
                                    newBalanceValue = input
                                }
                            },
                            textStyle = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Start
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier
                                .width(IntrinsicSize.Min)
                                .defaultMinSize(minWidth = 50.dp),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                        )
                        if (newBalanceValue.isEmpty()) {
                            Text(
                                "0",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                CapitalSyncPrimaryButton(
                    text = stringResource(R.string.confirm_new_balance),
                    onClick = {
                        val value = newBalanceValue.toDoubleOrNull()
                        if (value != null) {
                            viewModel.updateBalance(value)
                            showEditBalanceSheet = false
                        }
                    },
                    enabled = newBalanceValue.isNotEmpty()
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.account_settings), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_ios_24px),
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        AnimatedContent(targetState = uiState.isLoading, modifier = Modifier.padding(padding), label = "SettingsLoadingAnimation") { isLoading ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // 1. Balance Management Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.secondary,
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                                        )
                                    )
                                )
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            stringResource(R.string.balance_management),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            stringResource(R.string.balance_manual_override),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                                        )
                                    }
                                    Surface(
                                        onClick = {
                                            newBalanceValue = uiState.currentBalance.toString()
                                            showEditBalanceSheet = true
                                        },
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.2f),
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.edit_24px),
                                            contentDescription = stringResource(R.string.edit_button),
                                            modifier = Modifier.padding(8.dp).size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(32.dp))

                                Column {
                                    Text(
                                        stringResource(R.string.current_total),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        if (uiState.isBalanceVisible) String.format(Locale.US, "$%,.2f", uiState.currentBalance) else "$ ••••",
                                        style = MaterialTheme.typography.displaySmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }
                        }
                    }

                    // 2. Settings Section
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(stringResource(R.string.app_preferences), style = MaterialTheme.typography.labelLarge, color = Color.Gray)

                        // Privacy Mode Toggle
                        SettingsTile(
                            title = "Privacy Mode",
                            description = "Hide balances on the dashboard",
                            checked = !uiState.isBalanceVisible,
                            onCheckedChange = { viewModel.toggleBalanceVisible() },
                            icon = R.drawable.shield_lock_24px
                        )

                        // Swipe Actions Toggle
                        SettingsTile(
                            title = "Enable Swipe Actions",
                            description = "Swipe list items to edit or delete",
                            checked = uiState.isSwipeEnabled,
                            onCheckedChange = { viewModel.toggleSwipeEnabled() },
                            icon = R.drawable.list_24px
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Data & Privacy", style = MaterialTheme.typography.labelLarge, color = Color.Gray)

                        var csvToSave by remember { mutableStateOf<String?>(null) }
                        val context = LocalContext.current

                        val createDocumentLauncher = rememberLauncherForActivityResult(
                            ActivityResultContracts.CreateDocument("text/csv")
                        ) { uri ->
                            uri?.let {
                                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                                    outputStream.write(csvToSave?.toByteArray() ?: ByteArray(0))
                                }
                                Toast.makeText(context, "Exported successfully!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        LaunchedEffect(Unit) {
                            viewModel.exportEvent.collect { csv ->
                                csvToSave = csv
                                createDocumentLauncher.launch("capitalsync_transactions_${System.currentTimeMillis()}.csv")
                            }
                        }

                        OutlinedButton(
                            onClick = { viewModel.exportTransactions() },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(painter = painterResource(id = R.drawable.finance_24px), contentDescription = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Export Data to Excel (.csv)")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsTile(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onCheckedChange(!checked) },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        description,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}