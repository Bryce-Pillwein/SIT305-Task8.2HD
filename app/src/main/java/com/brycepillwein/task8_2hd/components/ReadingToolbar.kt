package com.brycepillwein.task8_2hd.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt

@Composable
fun ReadingToolbar(
  isPlaying: Boolean,
  onTogglePlay: () -> Unit,
  onSettingsClick: () -> Unit,
  onResetProgress: () -> Unit
) {
  var showConfirmReset by remember { mutableStateOf(false) }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(onClick = onTogglePlay) {
      Icon(
        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
        contentDescription = if (isPlaying) "Pause" else "Play"
      )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      IconButton(onClick = onSettingsClick) {
        Icon(Icons.Default.Settings, contentDescription = "Settings")
      }

      IconButton(onClick = { showConfirmReset = true }) {
        Icon(Icons.Default.RestartAlt, contentDescription = "Reset Reading Progress")
      }
    }
  }

  if (showConfirmReset) {
    AlertDialog(
      onDismissRequest = { showConfirmReset = false },
      confirmButton = {
        TextButton(onClick = {
          onResetProgress()
          showConfirmReset = false
        }) {
          Text("Reset", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { showConfirmReset = false }) {
          Text("Cancel")
        }
      },
      title = { Text("Reset Progress", style = MaterialTheme.typography.titleLarge) },
      text = { Text("Are you sure you want to reset your reading progress for this book?") },
      containerColor = Color(90, 200, 240),
      shape = MaterialTheme.shapes.medium
    )
  }
}
