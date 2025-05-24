package com.brycepillwein.task8_2hd.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow

@Composable
fun ReadingToolbar(
  isPlaying: Boolean,
  onTogglePlay: () -> Unit,
  onSettingsClick: () -> Unit
) {
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

    IconButton(onClick = onSettingsClick) {
      Icon(Icons.Default.Settings, contentDescription = "Settings")
    }
  }
}
