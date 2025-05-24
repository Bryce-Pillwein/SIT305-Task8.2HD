package com.brycepillwein.task8_2hd.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.brycepillwein.library.theme.LocalAppColors

@Composable
fun StatCard(
  label: String,
  value: String,
  icon: ImageVector,
  modifier: Modifier = Modifier
) {
  val colors = LocalAppColors.current

  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = colors.hsl100),
    elevation = CardDefaults.cardElevation(2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp, horizontal = 8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = Color(90, 200, 240),
        modifier = Modifier.size(24.dp)
      )
      Text(
        text = value,
        style = MaterialTheme.typography.headlineLarge
      )
      Text(
        text = label,
        style = MaterialTheme.typography.labelMedium
      )
    }
  }
}
