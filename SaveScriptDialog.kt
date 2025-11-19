package com.xtremeclicker.pro.ui.recorder

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SaveScriptDialog(
    actions: List<com.xtremeclicker.pro.model.Action>,
    onDismiss: () -> Unit,
    onSave: (name: String, description: String) -> Unit
) {
    var name by remember { mutableStateOf("Script ${System.currentTimeMillis().toString().takeLast(6)}") }
    var description by remember { mutableStateOf("") }
    var speed by remember { mutableStateOf(1f) } // 1ms a 5000ms

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Salvar Script", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome do script") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descrição (opcional)") })

                Column {
                    Text("Velocidade de reprodução")
                    Slider(
                        value = speed,
                        onValueChange = { speed = it },
                        valueRange = 0.1f..5f,
                        steps = 49,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = when {
                            speed <= 0.5f -> "Ultra rápido: ${ (1/speed).toInt() }x (≈1ms)"
                            speed == 1f -> "Velocidade original"
                            else -> "Lento: ${speed}x"
                        },
                        fontSize = 14.sp
                    )
                }

                Text("${actions.size} ações • ${actions.lastOrNull()?.timestamp?.div(1000) ?: 0}s de duração")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(name, description)
                onDismiss()
            }) { Text("SALVAR") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
