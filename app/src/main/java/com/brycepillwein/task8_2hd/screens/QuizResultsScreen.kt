package com.brycepillwein.task8_2hd.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.brycepillwein.library.components.ScreenWrapper
import com.brycepillwein.task8_2hd.components.ResultCard
import com.brycepillwein.task8_2hd.database.BookDbHelper
import com.brycepillwein.task8_2hd.model.QuizResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultsScreen(bookId: Int, results: List<QuizResult>, navController: NavController) {
  val context = LocalContext.current

  val correctCount = results.count {
    it.correctAnswer.trim().equals(it.userAnswer.trim(), ignoreCase = true)
  }

  LaunchedEffect(Unit) {
    withContext(Dispatchers.IO) {
      val dbHelper = BookDbHelper(context)
      dbHelper.updateQuizStats(bookId, correctAnswers = correctCount, totalQuestions = results.size)
    }
  }

  ScreenWrapper {
    Scaffold(
      containerColor = Color.Transparent,
      topBar = {
        TopAppBar(
          title = { Text("Quiz Results") },
          colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
      }
    ) { innerPadding ->
      LazyColumn(
        modifier = Modifier
          .padding(innerPadding)
          .padding(horizontal = 24.dp, vertical = 16.dp)
          .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
      ) {
        item {
          Text(
            "You scored $correctCount out of ${results.size}",
            style = MaterialTheme.typography.titleLarge
          )
        }

        itemsIndexed(results) { index, result ->
          ResultCard(index, result)
        }

        item {
          Spacer(Modifier.height(24.dp))
          Button(
            onClick = { navController.navigate("library") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(90, 200, 240),
              contentColor = Color.White
            )
          ) {
            Text("Back to Library")
          }
        }
      }
    }
  }
}
