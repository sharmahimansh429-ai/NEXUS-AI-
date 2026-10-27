package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberViolet
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NexusBorderHairline
import com.example.ui.theme.NexusCardDark
import com.example.ui.theme.NexusDeepBlack
import com.example.ui.theme.NexusObsidian
import com.example.ui.theme.NexusPureBlack
import com.example.ui.theme.NexusSilver
import com.example.ui.theme.NexusSilverBright
import com.example.ui.theme.NexusSilverDark
import com.example.ui.theme.NexusSilverMuted
import com.example.ui.theme.NexusSurfaceDark
import com.example.ui.theme.NexusSurfaceElevated
import com.example.ui.theme.NexusTitanium
import com.example.BuildConfig
import com.example.data.update.UpdateState
import com.example.viewmodel.ConnectionState
import com.example.viewmodel.NexusViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onOpenMenu: () -> Unit = {}
) {
    val context = LocalContext.current
    val isApiKeyConfigured by viewModel.isApiKeyConfigured.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()
    val languageMode by viewModel.languageMode.collectAsState()
    val personaStyle by viewModel.personaStyle.collectAsState()
    val isAutoSpeak by viewModel.isAutoSpeak.collectAsState()
    val ttsSpeed by viewModel.ttsSpeed.collectAsState()
    val ttsPitch by viewModel.ttsPitch.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()

    var customKeyInput by remember { mutableStateOf(viewModel.repository.getCustomApiKey()) }
    var isKeyVisible by remember { mutableStateOf(false) }

    val models = listOf("gemini-3.5-flash", "gemini-3.8-flash", "gemini-3.1-pro-preview")
    var modelDropdownExpanded by remember { mutableStateOf(false) }

    val languages = listOf("Bilingual (Auto)", "English Primary", "Hindi Primary (हिंदी)")
    var langDropdownExpanded by remember { mutableStateOf(false) }

    val personas = listOf("Futuristic AI", "Jarvis Companion", "Concise Professional", "Creative Thinker")
    var personaDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NexusPureBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NexusSilverBright,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "SETTINGS",
                        color = NexusSilverBright,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "SYSTEM CONFIGURATION",
                        color = NexusSilver,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            IconButton(
                onClick = onOpenMenu,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = NexusSilver,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Section 1: Gemini API Configuration & Real Diagnostics
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = NexusTitanium,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gemini API Connection",
                            color = NexusSilverBright,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Live Status Badge
                    val (badgeText, badgeColor) = when (connectionState) {
                        is ConnectionState.Online -> "ONLINE" to NeonEmerald
                        is ConnectionState.Checking -> "TESTING..." to NexusSilver
                        is ConnectionState.NoKey -> "KEY MISSING" to NeonAmber
                        is ConnectionState.Error -> "ALERT" to NeonRed
                    }

                    Surface(
                        color = badgeColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = badgeColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                when (val state = connectionState) {
                    is ConnectionState.Online -> {
                        Text(
                            text = "Connected to Google Gemini API using '${state.model}'. Real-time generative responses are operational.",
                            color = NexusSilver,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                    is ConnectionState.Checking -> {
                        Text(
                            text = "Verifying Gemini API credentials and testing connection...",
                            color = NexusSilver,
                            fontSize = 12.sp
                        )
                    }
                    is ConnectionState.NoKey -> {
                        Text(
                            text = "No Gemini API key configured. Enter your API key below or set GEMINI_API_KEY in the AI Studio Secrets panel.",
                            color = NeonAmber,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                    is ConnectionState.Error -> {
                        Text(
                            text = "Diagnostic alert: ${state.message}",
                            color = NeonRed,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Custom API Key Input Field
                Text(
                    text = if (viewModel.repository.hasManagedApiKey()) "API Key (Managed key active from environment, override below if desired):" else "Gemini API Key:",
                    color = NexusSilverMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = customKeyInput,
                    onValueChange = { customKeyInput = it },
                    placeholder = {
                        Text(
                            if (viewModel.repository.hasManagedApiKey()) "Managed key active in environment" else "Enter Gemini API Key",
                            color = NexusSilverDark,
                            fontSize = 13.sp
                        )
                    },
                    visualTransformation = if (isKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isKeyVisible = !isKeyVisible }) {
                            Text(
                                if (isKeyVisible) "HIDE" else "SHOW",
                                color = NexusTitanium,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NexusSilverBright,
                        unfocusedTextColor = NexusSilverBright,
                        focusedBorderColor = NexusSilver,
                        unfocusedBorderColor = NexusBorderHairline,
                        focusedContainerColor = NexusSurfaceDark,
                        unfocusedContainerColor = NexusSurfaceDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.updateCustomApiKey(customKeyInput)
                            Toast.makeText(context, "API Key saved. Verifying...", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NexusTitanium,
                            contentColor = NexusDeepBlack
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Key", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    if (customKeyInput.isNotBlank()) {
                        OutlinedButton(
                            onClick = {
                                customKeyInput = ""
                                viewModel.updateCustomApiKey("")
                                Toast.makeText(context, "Custom key cleared", Toast.LENGTH_SHORT).show()
                            },
                            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Clear", color = NexusSilver, fontSize = 12.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.checkConnection()
                            Toast.makeText(context, "Testing connection...", Toast.LENGTH_SHORT).show()
                        },
                        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NexusTitanium,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Test", color = NexusTitanium, fontSize = 12.sp)
                    }
                }
            }
        }

        // Section 2: AI Model Selection
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        tint = NexusTitanium,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Model Engine",
                        color = NexusSilverBright,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Select the Gemini neural model running Nexus AI.",
                    color = NexusSilver,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = modelDropdownExpanded,
                    onExpandedChange = { modelDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedModel,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelDropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NexusSilverBright,
                            unfocusedTextColor = NexusSilverBright,
                            focusedBorderColor = NexusSilver,
                            unfocusedBorderColor = NexusBorderHairline,
                            focusedContainerColor = NexusSurfaceDark,
                            unfocusedContainerColor = NexusSurfaceDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = modelDropdownExpanded,
                        onDismissRequest = { modelDropdownExpanded = false }
                    ) {
                        models.forEach { m ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(m, fontWeight = FontWeight.Bold)
                                        Text(
                                            when (m) {
                                                "gemini-3.5-flash" -> "Recommended • Fast, ultra-reliable multimodal chat"
                                                "gemini-3.8-flash" -> "Deep reasoning model with auto-fallback"
                                                else -> "Deep analytical thinking"
                                            },
                                            fontSize = 11.sp,
                                            color = NexusSilver
                                        )
                                    }
                                },
                                onClick = {
                                    viewModel.updateModel(m)
                                    modelDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Bilingual Support & Language Mode
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = NexusTitanium,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Language Mode (हिंदी / English)",
                        color = NexusSilverBright,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Nexus seamlessly understands and responds in both Hindi and English. Choose primary conversational bias.",
                    color = NexusSilver,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = langDropdownExpanded,
                    onExpandedChange = { langDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = languageMode,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = langDropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NexusSilverBright,
                            unfocusedTextColor = NexusSilverBright,
                            focusedBorderColor = NexusSilver,
                            unfocusedBorderColor = NexusBorderHairline,
                            focusedContainerColor = NexusSurfaceDark,
                            unfocusedContainerColor = NexusSurfaceDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = langDropdownExpanded,
                        onDismissRequest = { langDropdownExpanded = false }
                    ) {
                        languages.forEach { l ->
                            DropdownMenuItem(
                                text = { Text(l) },
                                onClick = {
                                    viewModel.updateLanguageMode(l)
                                    langDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Section 4: Voice & Speech Engine (TTS)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = NexusTitanium,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Auto-Read Responses",
                            color = NexusSilverBright,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Switch(
                        checked = isAutoSpeak,
                        onCheckedChange = { viewModel.updateAutoSpeak(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NexusTitanium,
                            checkedTrackColor = NexusSurfaceElevated,
                            uncheckedThumbColor = NexusSilverDark,
                            uncheckedTrackColor = NexusDeepBlack
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Speech Speed
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Speech Rate", color = NexusSilver, fontSize = 13.sp)
                    Text(String.format(java.util.Locale.US, "%.1fx", ttsSpeed), color = NexusTitanium, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                }
                Slider(
                    value = ttsSpeed,
                    onValueChange = { viewModel.updateTtsSettings(it, ttsPitch) },
                    valueRange = 0.6f..1.6f,
                    colors = SliderDefaults.colors(
                        thumbColor = NexusTitanium,
                        activeTrackColor = NexusSilver,
                        inactiveTrackColor = NexusSurfaceElevated
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Speech Pitch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Speech Pitch", color = NexusSilver, fontSize = 13.sp)
                    Text(String.format(java.util.Locale.US, "%.1fx", ttsPitch), color = NexusTitanium, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                }
                Slider(
                    value = ttsPitch,
                    onValueChange = { viewModel.updateTtsSettings(ttsSpeed, it) },
                    valueRange = 0.6f..1.4f,
                    colors = SliderDefaults.colors(
                        thumbColor = NexusTitanium,
                        activeTrackColor = NexusSilver,
                        inactiveTrackColor = NexusSurfaceElevated
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.ttsManager.speak(
                            text = "Hello! I am Nexus AI. नमस्ते, मैं नेक्सस एआई हूँ। How may I assist you today?",
                            speed = ttsSpeed,
                            pitch = ttsPitch
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusTitanium)
                ) {
                    Text("Test Bilingual Voice Output", color = NexusTitanium, fontSize = 13.sp)
                }
            }
        }

        // Section 5: AI Persona Style
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = NexusTitanium,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Persona Archetype",
                        color = NexusSilverBright,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = personaDropdownExpanded,
                    onExpandedChange = { personaDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = personaStyle,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = personaDropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NexusSilverBright,
                            unfocusedTextColor = NexusSilverBright,
                            focusedBorderColor = NexusSilver,
                            unfocusedBorderColor = NexusBorderHairline,
                            focusedContainerColor = NexusSurfaceDark,
                            unfocusedContainerColor = NexusSurfaceDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = personaDropdownExpanded,
                        onDismissRequest = { personaDropdownExpanded = false }
                    ) {
                        personas.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p) },
                                onClick = {
                                    viewModel.updatePersonaStyle(p)
                                    personaDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Section 6: NEXUS Updates (GitHub Releases Channel)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("nexus_updates_section"),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SystemUpdate,
                            contentDescription = null,
                            tint = NexusTitanium,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NEXUS Updates",
                            color = NexusSilverBright,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Release status badge
                    val (badgeText, badgeColor) = when (val s = updateState) {
                        is UpdateState.Idle -> "CURRENT v${BuildConfig.VERSION_NAME}" to NexusSilverMuted
                        is UpdateState.Checking -> "CHECKING..." to NexusTitanium
                        is UpdateState.UpdateAvailable -> "NEW UPDATE AVAILABLE" to ElectricCyan
                        is UpdateState.UpToDate -> "UP TO DATE" to NeonEmerald
                        is UpdateState.Downloading -> "${s.progressPercent}%" to ElectricCyan
                        is UpdateState.ReadyToInstall -> "READY TO INSTALL" to NeonEmerald
                        is UpdateState.Error -> "UPDATE CHECK ERROR" to NeonAmber
                    }

                    Surface(
                        color = badgeColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = badgeColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Channel: GitHub Releases (sharmahimansh429-ai/NEXUS-AI-)",
                    color = NexusSilverMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(8.dp))

                when (val state = updateState) {
                    is UpdateState.Idle -> {
                        Text(
                            text = "Installed Version: v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE}). Check for official APK updates published to the GitHub repository.",
                            color = NexusSilver,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                    is UpdateState.Checking -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Querying latest GitHub release tags and assets...",
                                color = NexusSilverBright,
                                fontSize = 12.sp
                            )
                        }
                    }
                    is UpdateState.UpdateAvailable -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = NexusDeepBlack,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.NewReleases,
                                        contentDescription = null,
                                        tint = ElectricCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Version ${state.release.tagName} Available!",
                                        color = ElectricCyan,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                if (state.release.releaseTitle.isNotBlank() && state.release.releaseTitle != state.release.tagName) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = state.release.releaseTitle,
                                        color = NexusSilverBright,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (state.release.releaseNotes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Changelog:",
                                        color = NexusSilverMuted,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = state.release.releaseNotes.take(300) + if (state.release.releaseNotes.length > 300) "..." else "",
                                        color = NexusSilver,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp,
                                        maxLines = 5
                                    )
                                }

                                state.release.apkAsset?.let { apk ->
                                    val sizeMb = if (apk.sizeBytes > 0) String.format(Locale.US, " (%.1f MB)", apk.sizeBytes / (1024.0 * 1024.0)) else ""
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Package: ${apk.name}$sizeMb",
                                        color = NexusSilverMuted,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                    is UpdateState.UpToDate -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = NexusSurfaceDark,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = NeonEmerald,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "NEXUS is up to date (v${state.currentVersion}). You have the latest official release installed.",
                                    color = NexusSilver,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                    is UpdateState.Downloading -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Downloading ${state.release.tagName} APK...",
                                    color = NexusSilverBright,
                                    fontSize = 12.sp
                                )
                                val downloadedMb = state.bytesDownloaded / (1024.0 * 1024.0)
                                val totalMb = state.totalBytes / (1024.0 * 1024.0)
                                Text(
                                    text = if (state.totalBytes > 0) String.format(Locale.US, "%.1f / %.1f MB", downloadedMb, totalMb) else String.format(Locale.US, "%.1f MB", downloadedMb),
                                    color = ElectricCyan,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { state.progressPercent / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = ElectricCyan,
                                trackColor = NexusSurfaceElevated
                            )
                        }
                    }
                    is UpdateState.ReadyToInstall -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = NexusSurfaceDark,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Update downloaded: ${state.apkFile.name} (${String.format(Locale.US, "%.1f MB", state.apkFile.length() / (1024.0 * 1024.0))}).",
                                    color = NexusSilverBright,
                                    fontSize = 12.sp
                                )
                                if (!viewModel.canInstallPackages()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Note: Android requires permission to install APK packages from unknown sources.",
                                        color = NeonAmber,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                    is UpdateState.Error -> {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = NeonAmber.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonAmber.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = state.message,
                                    color = NeonAmber,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                                if (state.isNetworkIssue) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Please check your internet connection or verify the repository.",
                                        color = NexusSilverMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (val state = updateState) {
                        is UpdateState.UpdateAvailable -> {
                            Button(
                                onClick = {
                                    if (state.release.apkAsset != null) {
                                        viewModel.downloadAndInstallUpdate(state.release)
                                    } else {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(state.release.htmlUrl)).apply {
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }
                                        context.startActivity(intent)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("download_install_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ElectricCyan,
                                    contentColor = NexusDeepBlack
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudDownload,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (state.release.apkAsset != null) "Download & Install" else "View Release Page",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        is UpdateState.ReadyToInstall -> {
                            if (!viewModel.canInstallPackages()) {
                                OutlinedButton(
                                    onClick = { viewModel.openInstallPermissionSettings() },
                                    modifier = Modifier.weight(1f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonAmber),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Grant Permission", color = NeonAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Button(
                                onClick = { viewModel.installDownloadedApk(state.apkFile) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("install_apk_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonEmerald,
                                    contentColor = NexusDeepBlack
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Install APK Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                        is UpdateState.Downloading -> {
                            OutlinedButton(
                                onClick = {},
                                enabled = false,
                                modifier = Modifier.weight(1f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Downloading... ${state.progressPercent}%", color = NexusSilverMuted, fontSize = 12.sp)
                            }
                        }
                        else -> {
                            Button(
                                onClick = { viewModel.checkForUpdates() },
                                enabled = updateState !is UpdateState.Checking,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("check_updates_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NexusTitanium,
                                    contentColor = NexusDeepBlack
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (updateState is UpdateState.Checking) "Checking..." else "Check for Updates",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Open GitHub Releases link in browser
                    OutlinedButton(
                        onClick = {
                            val url = when (val s = updateState) {
                                is UpdateState.UpdateAvailable -> s.release.htmlUrl
                                is UpdateState.Error -> s.releaseUrl ?: "https://github.com/sharmahimansh429-ai/NEXUS-AI-/releases"
                                else -> "https://github.com/sharmahimansh429-ai/NEXUS-AI-/releases"
                            }
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        },
                        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = null,
                            tint = NexusSilver,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Releases", color = NexusSilver, fontSize = 12.sp)
                    }
                }
            }
        }

        // About Info
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusSurfaceDark,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NEXUS AI v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})",
                    color = NexusSilverBright,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Futuristic Personal Intelligence • Built with Jetpack Compose & Gemini",
                    color = NexusSilverMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}
