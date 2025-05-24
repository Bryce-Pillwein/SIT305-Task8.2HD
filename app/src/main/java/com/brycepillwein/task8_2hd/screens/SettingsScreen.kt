package com.brycepillwein.task8_2hd.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.brycepillwein.library.components.ScreenWrapper
import com.brycepillwein.task8_2hd.database.SettingsStorage
import com.brycepillwein.task8_2hd.model.ReadingSettings
import com.brycepillwein.task8_2hd.model.TextAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
  val context = LocalContext.current
  val initialSettings = remember { SettingsStorage.load(context) }

  var textSize by remember { mutableStateOf(initialSettings.textSizeSp) }
  var wpm by remember { mutableStateOf(initialSettings.wpm) }
  var fontFamily by remember { mutableStateOf(initialSettings.fontFamily) }
  var alignment by remember { mutableStateOf(initialSettings.alignment) }
  var wordsPerPage by remember { mutableStateOf(initialSettings.wordsPerPage) }

  val availableFonts = listOf("default", "serif", "sans", "monospace")
  val availableAlignments = TextAlignment.values()

  ScreenWrapper {
    Scaffold(
      containerColor = Color.Transparent,

      topBar = {
        TopAppBar(
          title = { Text("Reader Settings") },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
          ))
      }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
      ) {
        // === TEXT SIZE ===
        Text("Text Size: ${textSize.toInt()}sp", style = MaterialTheme.typography.labelLarge)
        Slider(
          value = textSize,
          onValueChange = { textSize = it },
          valueRange = 8f..40f,
          modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
          colors = SliderDefaults.colors(
            activeTrackColor = Color(90, 200, 240),
            thumbColor = Color(90, 200, 240)
          )
        )

        HorizontalDivider()

        // === WPM ===
        Text("Words Per Minute: $wpm", style = MaterialTheme.typography.labelLarge)
        Slider(
          value = wpm.toFloat(),
          onValueChange = { wpm = it.toInt() },
          valueRange = 50f..1000f,
          modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
          colors = SliderDefaults.colors(
            activeTrackColor = Color(90, 200, 240),
            thumbColor = Color(90, 200, 240)
          )
        )

        HorizontalDivider()

        // === Words per Page ===
        Text("Words Per Page: $wordsPerPage", style = MaterialTheme.typography.labelLarge)
        Slider(
          value = wordsPerPage.toFloat(),
          onValueChange = { wordsPerPage = it.toInt() },
          valueRange = 10f..400f,
          modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
          colors = SliderDefaults.colors(
            activeTrackColor = Color(90, 200, 240),
            thumbColor = Color(90, 200, 240)
          )
        )

        HorizontalDivider()

        // === Font Family ===
        Text("Font Family", style = MaterialTheme.typography.labelLarge)
        var fontExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
          expanded = fontExpanded,
          onExpandedChange = { fontExpanded = !fontExpanded }
        ) {
          TextField(
            value = fontFamily,
            onValueChange = {},
            readOnly = true,
            label = { Text("Font") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fontExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
          )
          ExposedDropdownMenu(
            expanded = fontExpanded,
            onDismissRequest = { fontExpanded = false }
          ) {
            availableFonts.forEach {
              DropdownMenuItem(
                text = { Text(it) },
                onClick = {
                  fontFamily = it
                  fontExpanded = false
                }
              )
            }
          }
        }

        HorizontalDivider()

        // === Alignment ===
        Text("Text Alignment", style = MaterialTheme.typography.labelLarge)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          availableAlignments.forEach {
            FilterChip(
              selected = it == alignment,
              onClick = { alignment = it },
              label = {
                Text(it.name.lowercase().replaceFirstChar(Char::uppercase))
              },
              modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
            )
          }
        }

        Spacer(Modifier.height(24.dp))

        // === Save Button ===
        Button(
          onClick = {
            SettingsStorage.save(
              context,
              ReadingSettings(
                textSizeSp = textSize,
                wpm = wpm,
                fontFamily = fontFamily,
                alignment = alignment,
                wordsPerPage = wordsPerPage
              )
            )
            Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
          },
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(90, 200, 240),
            contentColor = Color.White
          )
        ) {
          Text("Save Settings")
        }
      }
    }
  }
}
