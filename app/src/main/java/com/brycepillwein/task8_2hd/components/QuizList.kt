package com.brycepillwein.task8_2hd.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.brycepillwein.task8_2hd.model.QuizItem
import com.brycepillwein.task8_2hd.model.QuizResult

@Composable
fun QuizList(
  items: List<QuizItem>,
  navController: NavController,
  modifier: Modifier = Modifier
) {
  // Track user selections
  val selections = remember { mutableStateMapOf<Int, String>() }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color.Transparent)
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    items(items.size) { idx ->
      val qi = items[idx]
      Text(
        text = "Q${idx + 1}: ${qi.question}",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
      )

      qi.options.forEach { option ->
        Row(
          Modifier
            .fillMaxWidth()
            .selectable(
              selected = selections[idx] == option,
              onClick = { selections[idx] = option }
            )
            .padding(4.dp)
        ) {
          RadioButton(
            selected = selections[idx] == option,
            onClick = { selections[idx] = option }
          )
          Text(option, modifier = Modifier.padding(start = 8.dp))
        }
      }

      HorizontalDivider(Modifier.padding(vertical = 12.dp))
    }

    item {
      Button(
        onClick = {
          // Build list of QuizResult objects
          val resultObjects = items.mapIndexed { index, item ->
            val correctIndex = when (item.correctAnswer.trim().uppercase()) {
              "A" -> 0
              "B" -> 1
              "C" -> 2
              "D" -> 3
              else -> -1
            }
            QuizResult(
              question = item.question,
              options = item.options,
              correctAnswer = item.options.getOrNull(correctIndex) ?: "",
              userAnswer = selections[index] ?: ""
            )
          }

          // Grab current NavBackStackEntry and its bookId argument
          val parent = navController.currentBackStackEntry ?: return@Button
          val bookId = parent.arguments?.getInt("bookId") ?: return@Button

          // Store the results in the SavedStateHandle
          parent.savedStateHandle.set("quizResults", resultObjects)

          // Navigate to the parameterized route
          navController.navigate("quizResults/$bookId")
        },
        enabled = selections.size == items.size,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(90, 200, 240),
          contentColor = Color.White
        )
      ) {
        Text("Submit Quiz")
      }
    }
  }
}
