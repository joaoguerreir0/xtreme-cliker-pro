package com.xtremeclicker.pro.ui.scripts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xtremeclicker.pro.database.SavedScript
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScriptsListScreen(
    viewModel: ScriptsViewModel
) {
    val scripts by viewModel.allScripts.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Meus Scripts", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* Abre tela de gravação nova */ },
                containerColor = PurplePro,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Novo Script") }
            )
        }
    ) { padding ->
        if (scripts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Nenhum script salvo ainda",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Toque no botão + para gravar o primeiro",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(scripts, key = { it.id }) { script ->
                    var showDialog by remember { mutableStateOf(false) }
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToStart) {
                                coroutineScope.launch {
                                    viewModel.deleteScript(script)
                                    snackbarHostState.showSnackbar("Script excluído")
                                }
                            }
                            true
                        }
                    )

                    SwipeToDismiss(
                        state = dismissState,
                        background = {
                            val color = if (dismissState.dismissDirection == DismissDirection.EndToStart)
                                Color.Red else MaterialTheme.colorScheme.surface
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                            }
                        },
                        dismissContent = {
                            ElevatedCard(
                                onClick = { /* Abre editor do script */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .combinedClickable(
                                        onClick = { /* play */ },
                                        onLongClick = { showDialog = true }
                                    )
                            ) {
                                ListItem(
                                    headlineContent = { Text(script.name, fontWeight = FontWeight.SemiBold) },
                                    supportingContent = {
                                        Column {
                                            Text("${script.actionsJson.split(",").size} ações")
                                            Text(
                                                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                                    .format(Date(script.createdAt))
                                            )
                                        }
                                    },
                                    leadingContent = {
                                        Icon(Icons.Default.PlayCircle, contentDescription = null)
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
