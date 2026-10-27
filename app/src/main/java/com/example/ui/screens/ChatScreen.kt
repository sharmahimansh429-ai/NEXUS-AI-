package com.example.ui.screens

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.components.NexusConnectionIndicator
import com.example.ui.components.NexusOceanParticleBackground
import com.example.ui.components.NexusOrb
import com.example.ui.components.OrbState
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NexusBorderHairline
import com.example.ui.theme.NexusCardDark
import com.example.ui.theme.NexusPureWhite
import com.example.ui.theme.NexusSilver
import com.example.ui.theme.NexusSilverBright
import com.example.ui.theme.NexusSilverDark
import com.example.ui.theme.NexusSilverMuted
import com.example.ui.theme.NexusSurfaceDark
import com.example.ui.theme.NexusTitanium
import com.example.ui.theme.ParticleLightBlue
import com.example.viewmodel.ConnectionState
import com.example.viewmodel.NexusViewModel
import com.example.voice.SpeechManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    viewModel: NexusViewModel,
    onOpenMenu: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val messages by viewModel.currentMessages.collectAsState()
    val orbState by viewModel.orbState.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    val soundLevel by viewModel.speechManager.soundLevel.collectAsState()
    val speechState by viewModel.speechManager.speechState.collectAsState()
    val voiceFeedback by viewModel.voiceFeedback.collectAsState()
    val speakingId by viewModel.ttsManager.currentlySpeakingId.collectAsState()
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsState()
    val isAutoSpeak by viewModel.isAutoSpeak.collectAsState()

    var showMicRationale by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Permission launcher for RECORD_AUDIO
    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startListening()
        } else {
            Toast.makeText(
                context,
                "Microphone permission is required for voice input.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Auto scroll on new message
    LaunchedEffect(messages.size, isGenerating) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Static Pure White Base Background (#FFFFFF)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NexusPureWhite)
    ) {
        // Continuous, subtle nanotech digital node matrix and optical wave refraction
        NexusOceanParticleBackground(
            modifier = Modifier.fillMaxSize(),
            dimAroundCenter = messages.isEmpty()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // =========================================================================
            // Top Bar: Clean, Apple-restrained minimalist header
            // =========================================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenMenu,
                        modifier = Modifier
                            .size(36.dp)
                            .background(NexusCardDark, CircleShape)
                            .border(0.5.dp, NexusBorderHairline, CircleShape)
                            .testTag("menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = NexusSilverBright,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "NEXUS",
                        color = NexusSilverBright,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 3.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Mute / Auto-Read Audio Toggle
                    IconButton(
                        onClick = { viewModel.toggleAutoSpeak() },
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                if (isAutoSpeak) NexusSurfaceDark else NexusCardDark,
                                CircleShape
                            )
                            .border(
                                0.5.dp,
                                if (isAutoSpeak) ParticleLightBlue.copy(alpha = 0.4f) else NexusBorderHairline,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isAutoSpeak) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = if (isAutoSpeak) "Auto-Read Active (Tap to Mute)" else "Muted (Tap to Read Aloud)",
                            tint = if (isAutoSpeak) ParticleLightBlue else NexusSilverDark,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (isSpeaking) {
                        IconButton(
                            onClick = { viewModel.stopSpeaking() },
                            modifier = Modifier
                                .size(34.dp)
                                .background(Color(0xFFFFF1F2), CircleShape)
                                .border(0.5.dp, NeonRed.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop Speaking",
                                tint = NeonRed,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Precision glass connection lens
                    NexusConnectionIndicator(
                        state = connectionState,
                        onIndicatorClick = {
                            if (connectionState is ConnectionState.Error || connectionState is ConnectionState.NoKey) {
                                onNavigateToSettings()
                            } else {
                                viewModel.checkConnection()
                            }
                        },
                        modifier = Modifier.testTag("connection_indicator")
                    )
                }
            }

            // Unconfigured API Key Warning Banner (Clean champagne card on pure white)
            if (connectionState is ConnectionState.NoKey) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { onNavigateToSettings() },
                    color = Color(0xFFFFFBEB),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.5.dp, NeonAmber.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = NeonAmber,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gemini API key not configured. Tap to configure in Settings.",
                            color = NexusSilver,
                            fontSize = 11.5.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Retry banner if the last message was an error (Clean rose card)
            val lastMessage = messages.lastOrNull()
            if (lastMessage?.isError == true && !isGenerating) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    color = Color(0xFFFFF1F2),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.5.dp, NeonRed.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Last transmission interrupted. Tap to retry.",
                            color = Color(0xFF9F1239),
                            fontSize = 11.5.sp
                        )
                        OutlinedButton(
                            onClick = { viewModel.retryLastMessage() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF9F1239)),
                            border = BorderStroke(0.5.dp, NeonRed.copy(alpha = 0.4f)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = Color(0xFF9F1239)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Retry", fontSize = 11.sp, color = Color(0xFF9F1239))
                        }
                    }
                }
            }

            // =========================================================================
            // Center Stage: 3D Core or Messages Flow
            // =========================================================================
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (messages.isEmpty()) {
                    // Spacious, uncluttered, intelligent focal presentation
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        NexusOrb(
                            orbState = orbState,
                            soundLevel = soundLevel,
                            orbSize = 320.dp,
                            isTyping = inputText.isNotBlank() && !isGenerating,
                            onClick = {
                                if (orbState == OrbState.LISTENING) {
                                    viewModel.stopListening()
                                } else {
                                    showMicRationale = true
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Understated, engineered status subtitle
                        val statusText = when (orbState) {
                            OrbState.LISTENING -> "TRANSCRIBING AUDIO SUBROUTINES"
                            OrbState.THINKING -> "NEURAL SYNTHESIS IN PROGRESS"
                            OrbState.SPEAKING -> "AUDIO MODULATION ACTIVE"
                            OrbState.ERROR -> "TRANSMISSION FAULT DETECTED"
                            OrbState.IDLE -> "SYSTEM READY // 120HZ ADAPTIVE"
                        }

                        Text(
                            text = statusText,
                            color = NexusSilverMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.8.sp
                        )
                    }
                } else {
                    // Conversation list with compact top status bar
                    Column(modifier = Modifier.fillMaxSize()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (orbState == OrbState.LISTENING) {
                                        viewModel.stopListening()
                                    } else {
                                        showMicRationale = true
                                    }
                                },
                            color = NexusCardDark.copy(alpha = 0.90f),
                            border = BorderStroke(0.5.dp, NexusBorderHairline)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                NexusOrb(
                                    orbState = orbState,
                                    soundLevel = soundLevel,
                                    orbSize = 46.dp,
                                    isTyping = inputText.isNotBlank() && !isGenerating
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "NEXUS CORE",
                                    color = NexusSilverBright,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { viewModel.createNewSession() },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(NexusSurfaceDark, CircleShape)
                                        .border(0.5.dp, NexusBorderHairline, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "New Session",
                                        tint = NexusSilverBright,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(messages, key = { it.id }) { message ->
                                ChatMessageCard(
                                    message = message,
                                    isSpeaking = speakingId == message.id,
                                    onSpeakClick = { viewModel.speakMessage(message) },
                                    onDeleteClick = { viewModel.deleteMessage(message.id) },
                                    onRetryClick = { viewModel.retryLastMessage() }
                                )
                            }

                            if (isGenerating) {
                                item {
                                    NexusThinkingBubble()
                                }
                            }
                        }
                    }
                }
            }

            // Real-Time Voice Transcription Feedback Strip
            AnimatedVisibility(
                visible = voiceFeedback != null || speechState is SpeechManager.SpeechState.Listening,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    color = NexusCardDark.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.5.dp, NexusBorderHairline)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (speechState is SpeechManager.SpeechState.Listening) Icons.Default.GraphicEq else Icons.Default.Mic,
                            contentDescription = null,
                            tint = ParticleLightBlue,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = voiceFeedback ?: "Listening...",
                            color = NexusSilver,
                            fontSize = 11.5.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // =========================================================================
            // Bottom Input Controls: Apple-Restrained Frosted Console
            // =========================================================================
            val isListening = speechState is SpeechManager.SpeechState.Listening
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color(0x0F090D16),
                        ambientColor = Color(0x05090D16)
                    ),
                color = NexusCardDark.copy(alpha = 0.95f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(0.5.dp, NexusBorderHairline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Microphone Voice Input Button
                    IconButton(
                        onClick = {
                            if (isListening) {
                                viewModel.stopListening()
                            } else {
                                showMicRationale = true
                            }
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                if (isListening) ParticleLightBlue.copy(alpha = 0.15f) else NexusSurfaceDark,
                                RoundedCornerShape(10.dp)
                            )
                            .border(
                                0.5.dp,
                                if (isListening) ParticleLightBlue.copy(alpha = 0.4f) else NexusBorderHairline,
                                RoundedCornerShape(10.dp)
                            )
                            .testTag("mic_button")
                    ) {
                        Icon(
                            imageVector = if (isListening) Icons.Default.GraphicEq else Icons.Default.Mic,
                            contentDescription = if (isListening) "Stop Listening" else "Start Voice Input",
                            tint = if (isListening) ParticleLightBlue else NexusSilverBright,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Seamless Message Input Field
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { viewModel.inputText.value = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        placeholder = {
                            Text(
                                "Ask NEXUS (English or हिंदी)...",
                                color = NexusSilverDark,
                                fontSize = 13.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NexusSilverBright,
                            unfocusedTextColor = NexusSilverBright,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Send Button (Crisp Obsidian & Pure White Icon)
                    val canSend = inputText.isNotBlank() && !isGenerating
                    IconButton(
                        onClick = { viewModel.sendMessage() },
                        enabled = canSend,
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                if (canSend) NexusTitanium else Color(0xFFF1F5F9),
                                RoundedCornerShape(10.dp)
                            )
                            .border(
                                0.5.dp,
                                if (canSend) Color.Transparent else NexusBorderHairline,
                                RoundedCornerShape(10.dp)
                            )
                            .testTag("send_button")
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = NexusSilverBright,
                                strokeWidth = 1.8.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Message",
                                tint = if (canSend) Color.White else NexusSilverDark,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Microphone Permission Rationale Dialog
        if (showMicRationale) {
            AlertDialog(
                onDismissRequest = { showMicRationale = false },
                containerColor = NexusCardDark,
                shape = RoundedCornerShape(16.dp),
                title = {
                    Text(
                        text = "MICROPHONE ACCESS",
                        color = NexusSilverBright,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                },
                text = {
                    Text(
                        text = "NEXUS requires microphone permission to listen and transcribe speech in Hindi and English on your Android device.",
                        color = NexusSilver,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showMicRationale = false
                            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NexusTitanium,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Grant Permission", fontWeight = FontWeight.Medium, fontSize = 12.sp)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showMicRationale = false },
                        border = BorderStroke(0.5.dp, NexusBorderHairline),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel", color = NexusSilverBright, fontSize = 12.sp)
                    }
                }
            )
        }
    }
}

@Composable
private fun ChatMessageCard(
    message: ChatMessage,
    isSpeaking: Boolean,
    onSpeakClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onRetryClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val isUser = message.sender == "user"
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val timeString = remember(message.timestamp) { timeFormat.format(Date(message.timestamp)) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 310.dp)
                .testTag(if (isUser) "user_message_bubble" else "nexus_message_bubble"),
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 3.dp,
                bottomEnd = if (isUser) 3.dp else 14.dp
            ),
            color = when {
                isUser -> Color(0xFF0F172A)     // Deep obsidian container for user
                message.isError -> Color(0xFFFFF1F2) // Clean rose card for system alerts
                else -> Color(0xF8FFFFFF)       // Translucent milky frosted card for NEXUS
            },
            border = BorderStroke(
                0.5.dp,
                when {
                    isUser -> Color(0x1F000000)
                    message.isError -> Color(0x40E11D48)
                    else -> NexusBorderHairline
                }
            ),
            shadowElevation = if (isUser) 1.dp else 0.5.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Header inside bubble
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUser) "YOU" else if (message.isError) "SYSTEM NOTICE" else "NEXUS",
                        color = when {
                            isUser -> Color(0xFF94A3B8)
                            message.isError -> Color(0xFFE11D48)
                            else -> NexusSilverMuted
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = timeString,
                        color = if (isUser) Color(0xFF64748B) else NexusSilverDark,
                        fontSize = 9.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                SelectionContainer {
                    Text(
                        text = message.text,
                        color = when {
                            isUser -> Color(0xFFF8FAFC)
                            message.isError -> Color(0xFF881337)
                            else -> NexusSilverBright
                        },
                        fontSize = 13.5.sp,
                        lineHeight = 19.5.sp
                    )
                }

                // Retry action button if message failed
                if (message.isError) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = onRetryClick,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF881337)),
                            border = BorderStroke(0.5.dp, Color(0x66E11D48)),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(26.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = Color(0xFF881337)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Retry", fontSize = 11.sp, color = Color(0xFF881337))
                        }
                    }
                }

                // Action Bar for Assistant Messages (TTS Speak, Copy)
                if (!isUser && !message.isError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Nexus Message", message.text)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(26.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy message",
                                tint = NexusSilverDark,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = onSpeakClick,
                            modifier = Modifier
                                .size(26.dp)
                                .background(
                                    if (isSpeaking) ParticleLightBlue.copy(alpha = 0.15f) else Color.Transparent,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = if (isSpeaking) "Stop Speech" else "Read Aloud",
                                tint = if (isSpeaking) ParticleLightBlue else NexusSilverDark,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NexusThinkingBubble() {
    val transition = rememberInfiniteTransition(label = "thinking")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "thinkingAlpha"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xF8FFFFFF),
            border = BorderStroke(0.5.dp, NexusBorderHairline.copy(alpha = alpha))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    color = NexusSilverMuted.copy(alpha = alpha),
                    strokeWidth = 1.5.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "NEXUS SYNTHESIS...",
                    color = NexusSilverMuted.copy(alpha = alpha),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
