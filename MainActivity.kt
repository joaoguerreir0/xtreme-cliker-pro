package com.xtremeclicker.pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.xtremeclicker.pro.ui.theme.XtremeClickerProTheme
import com.xtremeclicker.pro.ui.theme.PurplePro
import com.xtremeclicker.pro.ui.theme.GradientStart
import com.xtremeclicker.pro.ui.theme.GradientEnd
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.xtremeclicker.pro.ui.scripts.ScriptsListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            XtremeClickerProTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Xtreme Clicker Pro",
                        fontWeight = FontWeight.Bold,
                        color = PurplePro
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Abre o gravador ou inicia o bot */ },
                containerColor = PurplePro,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Iniciar",
                    modifier = Modifier.size(42.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(GradientStart, GradientEnd)
                    )
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título animado
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000)) + scaleIn()
            ) {
                Text(
                    text = "Clique Automático 1ms\nMulti-Touch • Swipe • Scripts Ilimitados",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                        .padding(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Botões rápidos
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Abre tela de scripts salvos */ },
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Default.FolderOpen, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Meus Scripts")
                }

                ElevatedButton(
                    onClick = { /* Inicia gravação imediata */ },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = PurplePro
                    ),
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Default.Videocam, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Gravar Agora", color = androidx.compose.ui.graphics.Color.White)
                }
            }
        }
    }
}
