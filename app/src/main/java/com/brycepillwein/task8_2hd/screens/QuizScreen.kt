package com.brycepillwein.task8_2hd.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.brycepillwein.library.components.ScreenWrapper
import com.brycepillwein.task8_2hd.components.QuizList
import com.brycepillwein.task8_2hd.database.BookDbHelper
import com.brycepillwein.task8_2hd.viewModel.QuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
  bookId: Int,
  navController: NavController,
  quizViewModel: QuizViewModel = viewModel()
) {
  val context = LocalContext.current
  val dbHelper = remember { BookDbHelper(context) }

  val quizText = navController.previousBackStackEntry
    ?.savedStateHandle
    ?.get<String>("quizText") ?: ""

  LaunchedEffect(quizText) {
    if (quizText.length >= 50) {
      quizViewModel.loadQuiz(quizText)
    }
  }

  val isLoading by quizViewModel.isLoading.collectAsState()
  val error by quizViewModel.error.collectAsState()
  val questions by quizViewModel.questions.collectAsState()

  ScreenWrapper {
    Scaffold(
      containerColor = Color.Transparent,
      topBar = {
        TopAppBar(
          title = { Text("Comprehension Quiz") },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
          )
        )
      }
    ) { innerPadding ->
      when {
        isLoading -> Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
          contentAlignment = Alignment.Center
        ) {
          CircularProgressIndicator()
        }

        error != null -> Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
          contentAlignment = Alignment.Center
        ) {
          Text("Error: $error")
        }

        else -> {
          QuizList(questions, navController, Modifier.padding(innerPadding))
        }
      }
    }
  }
}
