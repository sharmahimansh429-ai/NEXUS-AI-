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
import androidx.compose.material.icons.filled.SmartToy
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
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NexusBorderHairline
import com.example.ui.theme.NexusCardDark
import com.example.ui.theme.NexusCharcoal
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
import com.example.ui.theme.ParticleCyan
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NexusPureBlack)
    ) {
        // Full-screen, continuously animated ocean-like wave surface with 4-8dp dot-matrix particles
        NexusOceanParticleBackground(
            modifier = Modifier.fillMaxSize(),
            dimAroundCenter = messages.isEmpty()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // Clean Minimal Header with Dark Frosted Glass Touch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenMenu,
                        modifier = Modifier
                            .size(42.dp)
                            .background(NexusCardDark, CircleShape)
                            .border(1.dp, NexusBorderHairline, CircleShape)
                            .testTag("menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = NexusSilverBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "NEXUS",
                        color = NexusSilverBright,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Mute / Auto-Read Audio Toggle
                    IconButton(
                        onClick = { viewModel.toggleAutoSpeak() },
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                if (isAutoSpeak) NexusSurfaceDark else NexusCardDark,
                                CircleShape
                            )
                            .border(
                                1.dp,
                                if (isAutoSpeak) ParticleLightBlue.copy(alpha = 0.5f) else NexusBorderHairline,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isAutoSpeak) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = if (isAutoSpeak) "Auto-Read Active (Tap to Mute)" else "Muted (Tap to Read Aloud)",
                            tint = if (isAutoSpeak) ParticleLightBlue else NexusSilverDark,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (isSpeaking) {
                        IconButton(
                            onClick = { viewModel.stopSpeaking() },
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFF2B1115), CircleShape)
                                .border(1.dp, NeonRed.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop Speaking",
                                tint = NeonRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Status indicator container with live model / state indication
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

            // Unconfigured API Key Warning Banner
            if (connectionState is ConnectionState.NoKey) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { onNavigateToSettings() },
                    color = Color(0xFF1E170C),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, NeonAmber.copy(alpha = 0.5f))
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
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gemini API key not configured. Tap to configure in Settings.",
                            color = NeonAmber,
                            fontSize = 11.5.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Retry banner if the last message was an error
            val lastMessage = messages.lastOrNull()
            if (lastMessage?.isError == true && !isGenerating) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    color = Color(0xFF241014),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, NeonRed.copy(alpha = 0.45f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Last transmission failed. Tap to retry.",
                            color = Color(0xFFFCA5A5),
                            fontSize = 12.sp
                        )
                        OutlinedButton(
                            onClick = { viewModel.retryLastMessage() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusSilverBright),
                            border = BorderStroke(1.dp, NeonRed.copy(alpha = 0.5f)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = NexusSilverBright
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Retry", fontSize = 11.5.sp, color = NexusSilverBright)
                        }
                    }
                }
            }

            // Center Messages & 3D Particle Core area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (messages.isEmpty()) {
                    // Clean initial screen: authentic central 3D dot-matrix particle core
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        NexusOrb(
                            orbState = orbState,
                            soundLevel = soundLevel,
                            orbSize = 325.dp,
                            isTyping = inputText.isNotBlank() && !isGenerating,
                            onClick = {
                                if (orbState == OrbState.LISTENING) {
                                    viewModel.stopListening()
                                } else {
                                    showMicRationale = true
                                }
                            }
                        )
                    }
                } else {
                    // Conversation list with compact top particle core bar
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
                            color = NexusCardDark.copy(alpha = 0.85f),
                            border = BorderStroke(1.dp, NexusBorderHairline)
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
                                    orbSize = 54.dp,
                                    isTyping = inputText.isNotBlank() && !isGenerating
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "NEXUS CORE",
                                    color = NexusSilverBright,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { viewModel.createNewSession() },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(NexusSurfaceDark, CircleShape)
                                        .border(1.dp, NexusBorderHairline, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "New Session",
                                        tint = NexusSilverBright,
                                        modifier = Modifier.size(18.dp)
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
                            verticalArrangement = Arrangement.spacedBy(14.dp)
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

            // Real-Time Voice Transcription / Diagnostic Feedback Strip
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
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ParticleLightBlue.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (speechState is SpeechManager.SpeechState.Listening) Icons.Default.GraphicEq else Icons.Default.Mic,
                            contentDescription = null,
                            tint = ParticleLightBlue,
                            modifier = Modifier.size(16.dp)
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

            // Bottom Input Controls Row (Sleek Dark Glassmorphism)
            val isListening = speechState is SpeechManager.SpeechState.Listening
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                color = NexusCardDark.copy(alpha = 0.92f),
                shape = RoundedCornerShape(26.dp),
                border = BorderStroke(1.dp, NexusBorderHairline)
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
                            .size(44.dp)
                            .background(
                                if (isListening) ParticleLightBlue.copy(alpha = 0.25f) else NexusSurfaceDark,
                                CircleShape
                            )
                            .border(
                                1.dp,
                                if (isListening) ParticleLightBlue else NexusBorderHairline,
                                CircleShape
                            )
                            .testTag("mic_button")
                    ) {
                        Icon(
                            imageVector = if (isListening) Icons.Default.GraphicEq else Icons.Default.Mic,
                            contentDescription = if (isListening) "Stop Listening" else "Start Voice Input",
                            tint = if (isListening) ParticleLightBlue else NexusSilverBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Message Input Field
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(NexusSurfaceDark.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                            .border(1.dp, NexusBorderHairline, RoundedCornerShape(20.dp))
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { viewModel.inputText.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("chat_input_field"),
                            placeholder = {
                                Text(
                                    "Ask NEXUS (English or हिंदी)...",
                                    color = NexusSilverDark,
                                    fontSize = 13.5.sp
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
                            shape = RoundedCornerShape(20.dp),
                            maxLines = 4
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Send Button
                    IconButton(
                        onClick = { viewModel.sendMessage() },
                        enabled = inputText.isNotBlank() && !isGenerating,
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                if (inputText.isNotBlank() && !isGenerating) NexusTitanium else NexusSurfaceDark,
                                CircleShape
                            )
                            .border(1.dp, NexusBorderHairline, CircleShape)
                            .testTag("send_button")
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = NexusSilverBright,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Message",
                                tint = if (inputText.isNotBlank() && !isGenerating) NexusDeepBlack else NexusSilverDark,
                                modifier = Modifier.size(18.dp)
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
                title = {
                    Text(
                        text = "MICROPHONE ACCESS",
                        color = NexusSilverBright,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
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
                            contentColor = NexusDeepBlack
                        )
                    ) {
                        Text("Grant Permission", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showMicRationale = false },
                        border = BorderStroke(1.dp, NexusBorderHairline)
                    ) {
                        Text("Cancel", color = NexusSilverBright)
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
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (message.isError) Color(0xFF2B1115) else NexusSurfaceDark)
                    .border(
                        1.dp,
                        if (message.isError) NeonRed else NexusBorderHairline,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = if (message.isError) NeonRed else NexusSilverBright,
                    modifier = Modifier.size(17.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            modifier = Modifier
                .widthIn(max = 310.dp)
                .testTag(if (isUser) "user_message_bubble" else "nexus_message_bubble"),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) Color(0xFF1E2633)
            else if (message.isError) Color(0xFF261114)
            else NexusCardDark,
            border = BorderStroke(
                1.dp,
                if (isUser) Color(0x55475569)
                else if (message.isError) NeonRed.copy(alpha = 0.5f)
                else NexusBorderHairline
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Header inside bubble
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUser) "YOU" else if (message.isError) "SYSTEM ALERT" else "NEXUS",
                        color = if (isUser) NexusSilver
                        else if (message.isError) NeonRed
                        else NexusSilverBright,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = timeString,
                        color = NexusSilverDark,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                SelectionContainer {
                    Text(
                        text = message.text,
                        color = if (message.isError) Color(0xFFFCA5A5) else NexusSilverBright,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
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
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusSilverBright),
                            border = BorderStroke(1.dp, NeonRed.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = NexusSilverBright
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Retry", fontSize = 11.5.sp, color = NexusSilverBright)
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
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy message",
                                tint = NexusSilverDark,
                                modifier = Modifier.size(15.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = onSpeakClick,
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    if (isSpeaking) ParticleLightBlue.copy(alpha = 0.25f) else Color.Transparent,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = if (isSpeaking) "Stop Speech" else "Read Aloud",
                                tint = if (isSpeaking) ParticleLightBlue else NexusSilverDark,
                                modifier = Modifier.size(16.dp)
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
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(NexusSurfaceDark)
                .border(1.dp, NexusBorderHairline, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SmartToy,
                contentDescription = null,
                tint = NexusSilverBright,
                modifier = Modifier.size(17.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = NexusCardDark,
            border = BorderStroke(1.dp, NexusBorderHairline.copy(alpha = alpha))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    color = ParticleLightBlue.copy(alpha = alpha),
                    strokeWidth = 1.8.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "NEXUS reasoning...",
                    color = NexusSilver.copy(alpha = alpha),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
