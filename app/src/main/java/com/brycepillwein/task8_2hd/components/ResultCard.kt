package com.brycepillwein.task8_2hd.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.brycepillwein.task8_2hd.model.QuizResult

@Composable
fun ResultCard(index: Int, result: QuizResult) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White, shape = RoundedCornerShape(8.dp))
      .padding(16.dp)
  ) {
    Text("Q${index + 1}: ${result.question}", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))

    result.options.forEach { option ->
      val isCorrect = option == result.correctAnswer
      val isUser = option == result.userAnswer

      val backgroundColor = when {
        isCorrect && isUser -> Color(0xFFB2DFDB)  // ✅ both
        isCorrect -> Color(0xFFC8E6C9)            // ✅ correct
        isUser -> Color(0xFFFFCDD2)               // ❌ incorrect
        else -> Color(0xFFF5F5F5)                 // neutral
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(backgroundColor, shape = RoundedCornerShape(6.dp))
          .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(option, color = Color.Black)
      }
      Spacer(Modifier.height(6.dp))
    }

    if (result.userAnswer != result.correctAnswer) {
      Spacer(Modifier.height(8.dp))
      Text(
        text = "Correct Answer: ${result.correctAnswer}",
        color = Color(0xFF388E3C),
        style = MaterialTheme.typography.labelMedium
      )
    }
  }
}
