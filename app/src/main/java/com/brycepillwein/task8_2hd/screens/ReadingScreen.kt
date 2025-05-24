package com.brycepillwein.task8_2hd.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.brycepillwein.library.components.ScreenWrapper
import com.brycepillwein.task8_2hd.components.SpeedReader
import com.brycepillwein.task8_2hd.database.BookDbHelper
import com.brycepillwein.task8_2hd.database.SettingsStorage
import com.brycepillwein.task8_2hd.model.StoredBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(bookId: Int, navController: NavController) {
  val context = LocalContext.current
  val dbHelper = remember { BookDbHelper(context) }

  var book by remember { mutableStateOf<StoredBook?>(null) }
  var isPlaying by remember { mutableStateOf(false) }
  var currentWordIndex by remember { mutableStateOf(0) }
  var sessionStartIndex by remember { mutableStateOf(0) }
  var showQuizDialog by remember { mutableStateOf(false) }
  var showResetConfirm by remember { mutableStateOf(false) }
  val settings = SettingsStorage.load(context)
  var readyToRender by remember { mutableStateOf(false) }

  LaunchedEffect(bookId) {
    withContext(Dispatchers.IO) {
      book = dbHelper.getBookById(bookId)
    }
  }

  LaunchedEffect(book) {
    book?.let {
      currentWordIndex = it.lastReadWordIndex
      sessionStartIndex = it.lastReadWordIndex
    }
    readyToRender = true
  }

  LaunchedEffect(currentWordIndex) {
    if (book != null && currentWordIndex % 10 == 0) {
      withContext(Dispatchers.IO) {
        dbHelper.updateProgress(book!!.id, currentWordIndex, book!!.content.split(" ").size)
      }
    }
  }

  ScreenWrapper {
    Scaffold(
      containerColor = Color.Transparent,

      topBar = {
        TopAppBar(title = { Text(book?.title ?: "Speed Reader") },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
          ))
      },

      bottomBar = {
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
          color = Color.Transparent,
          shadowElevation = 0.dp
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(onClick = { isPlaying = !isPlaying }) {
              Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play"
              )
            }

            IconButton(onClick = {
              navController.navigate("settings")
            }) {
              Icon(Icons.Default.Settings, contentDescription = "Settings")
            }

            IconButton(onClick = { showResetConfirm = true }) {
              Icon(Icons.Default.RestartAlt, contentDescription = "Reset Progress")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
              onClick = {
                book?.let {
                  val totalWords = it.content.split(" ").size
                  dbHelper.updateProgress(it.id, currentWordIndex, totalWords)

                  val words = it.content.split(" ")
                  val readSlice = words
                    .subList(sessionStartIndex, currentWordIndex.coerceAtMost(words.size))
                    .joinToString(" ")

                  if (readSlice.split(" ").size < 50) {
                    Toast.makeText(context, "Read more before taking a quiz", Toast.LENGTH_SHORT).show()
                    navController.navigate("library")
                  } else {
                    navController.currentBackStackEntry
                      ?.savedStateHandle
                      ?.set("quizText", readSlice)
                    showQuizDialog = true
                  }
                }
              },
              shape = RoundedCornerShape(4.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(90, 200, 240),
                contentColor = Color.White
              ),
              contentPadding = PaddingValues(horizontal = 32.dp, vertical = 6.dp),
              modifier = Modifier.height(36.dp)
            ) {
              Text("Done Reading", style = MaterialTheme.typography.labelLarge)
            }
          }
        }
      },


    ) { innerPadding ->
      book?.let { book ->
        Column(
          modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          if (readyToRender) {
            SpeedReader(
              text = book.content,
              wpm = settings.wpm,
              textSizeSp = settings.textSizeSp,
              fontFamily = settings.fontFamily,
              alignment = settings.alignment,
              isPlaying = isPlaying,
              startIndex = currentWordIndex,
              onWordIndexChange = { currentWordIndex = it },
              wordsPerPage = settings.wordsPerPage
            )
          }
        }
      } ?: Box(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator()
      }
    }
  }

  /**
   * TAKE QUIZ
   */
  if (showQuizDialog) {
    AlertDialog(
      onDismissRequest = { showQuizDialog = false },
      title = { Text("Take a Quiz?") },
      text = { Text("You’ve finished reading. Would you like to take a quiz to test your comprehension?") },
      confirmButton = {
        TextButton(onClick = {
          showQuizDialog = false
          navController.navigate("quiz/${book!!.id}")
        }) {
          Text("Take Quiz", color =  Color(90, 200, 240))
        }
      },
      dismissButton = {
        TextButton(onClick = {
          showQuizDialog = false
          navController.popBackStack("dashboard", inclusive = false)
        }) {
          Text("Not Now",color =  Color(90, 200, 240))
        }
      },
      containerColor = Color.White,
      shape = RoundedCornerShape(12.dp)
    )
  }

  /**
   * RESET READING PROGRESS
   */
  if (showResetConfirm) {
    AlertDialog(
      onDismissRequest = { showResetConfirm = false },
      confirmButton = {
        TextButton(onClick = {
          currentWordIndex = 0
          sessionStartIndex = 0
          book?.let {
            dbHelper.updateProgress(it.id, 0, it.content.split(" ").size)
          }
          showResetConfirm = false
        }) {
          Text("Reset", color =  Color(90, 200, 240))
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetConfirm = false }) {
          Text("Cancel", color =  Color(90, 200, 240))
        }
      },
      title = { Text("Reset Progress") },
      text = { Text("Are you sure you want to reset your reading progress for this book?") },
      containerColor = Color.White,
      shape = RoundedCornerShape(12.dp)
    )
  }

}
