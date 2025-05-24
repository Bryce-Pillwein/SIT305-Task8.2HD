package com.brycepillwein.task8_2hd.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.brycepillwein.library.components.ScreenWrapper
import com.brycepillwein.task8_2hd.database.BookDbHelper
import com.brycepillwein.task8_2hd.model.StoredBook

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(navController: NavController) {
  val context = LocalContext.current
  val dbHelper = remember { BookDbHelper(context) }
  var books by remember { mutableStateOf(listOf<StoredBook>()) }

  LaunchedEffect(true) {
    books = dbHelper.getAllBooks()
  }

  ScreenWrapper {
    Scaffold(
      containerColor = Color.Transparent,

      topBar = {
        TopAppBar(
          title = { Text("My Library") },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
          )
        )
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = { navController.navigate("upload") },
          containerColor = Color(255, 255, 255)
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add Book")
        }
      }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .padding(16.dp)
      ) {
        if (books.isEmpty()) {
          Text("No books added yet.")
        } else {
          LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(books) { book ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    navController.navigate("reader/${book.id}")
                  }
                  .border(
                    width = 1.dp,
                    color = Color(240, 240, 240), // subtle gray border
                    shape = MaterialTheme.shapes.medium
                  ),
                colors = CardDefaults.cardColors(containerColor = Color(255, 255, 255))
              ) {
                Row(
                  modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Book",
                    tint = Color(90, 200, 240),
                    modifier = Modifier.size(32.dp)
                  )

                  Column(modifier = Modifier.weight(1f)) {
                    Text(book.title, style = MaterialTheme.typography.titleMedium)
                    Text("by ${book.author}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                    LinearProgressIndicator(
                      progress = { book.progress },
                      color = Color(90, 200, 240)
                    )

                    Text(
                      "${(book.progress * 100).toInt()}% read",
                      style = MaterialTheme.typography.labelSmall
                    )

                    Text(
                      text = if (book.lastQuizScore >= 0) {
                        "Quiz: ${book.lastQuizScore}/3"
                      } else {
                        "No quiz taken"
                      },
                      style = MaterialTheme.typography.labelSmall
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(Modifier.height(32.dp))

      }
    }
  }
}