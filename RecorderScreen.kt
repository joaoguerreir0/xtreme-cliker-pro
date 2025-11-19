package com.xtremeclicker.pro.ui.recorder

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.xtremeclicker.pro.services.ClickerAccessibilityService
import com.xtremeclicker.pro.model.Action
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecorderScreen(onRecordingFinished: (List<Action>) -> Unit) {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var recordedActions by remember { mutableStateOf<List<Action>>(emptyList()) }
    val service = ClickerAccessibilityService.instance

    // Overlay que cobre tudo (transparente com controles flutuantes)
    Box(modifier = Modifier.fillMaxSize()) {
        // Fundo semi-transparente para destacar controles
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // Controles centrais
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isRecording) "GRAVANDO..." else "Pronto para gravar",
                fontSize = 24.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = if (isRecording) Color.Red else MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            // Botão gigante de gravação
            Button(
                onClick = {
                    if (!isRecording) {
                        service?.startRecording()
                        isRecording = true
                        Toast.makeText(context, "Gravação iniciada – toque na tela!", Toast.LENGTH_SHORT).show()
                    } else {
                        recordedActions = service?.stopRecording() ?: emptyList()
                        isRecording = false
                        onRecordingFinished(recordedActions)
                    }
                },
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecording) Color.Red else Color(0xFF00C853)
                )
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "${recordedActions.size} ações capturadas",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Instrução no topo
        Text(
            text = "Toque e deslize em qualquer lugar da tela",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 24.dp, vertical = 12.dp)
        )
    }

    // Volta automaticamente ao parar
    BackHandler(enabled = true) {
        if (isRecording) {
            service?.stopRecording()
        }
    }

    // Intercepta toques reais na tela durante a gravação (overlay transparente)
    LaunchedEffect(isRecording) {
        if (isRecording && service != null) {
            // Simula detecção de toques (em produção, usa MotionEvent listener)
            while (isRecording) {
                delay(1) // Loop para capturar
                // Aqui tu podes integrar com MotionEvent para capturar toques reais
            }
        }
    }
}
