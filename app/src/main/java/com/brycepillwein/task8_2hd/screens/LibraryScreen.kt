package com.brycepillwein.task8_2hd.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
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
  var bookToDelete by remember { mutableStateOf<StoredBook?>(null) }

  // Load initial list
  LaunchedEffect(Unit) {
    books = dbHelper.getAllBooks()
  }
  fun refresh() { books = dbHelper.getAllBooks() }

  ScreenWrapper {
    Scaffold(
      containerColor = Color.Transparent,
      topBar = {
        TopAppBar(
          title = { Text("My Library") },
          colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = { navController.navigate("upload") },
          containerColor = Color.White
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
                  .clickable { navController.navigate("reader/${book.id}") }
                  .border(1.dp, Color(0xFFF0F0F0), MaterialTheme.shapes.medium),
                colors = CardDefaults.cardColors(containerColor = Color.White)
              ) {
                Column(modifier = Modifier.padding(16.dp)) {
                  // Top row: Icon, Title/Author, Delete
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    Icon(
                      imageVector = Icons.Default.Book,
                      contentDescription = null,
                      tint = Color(0xFF5AC8F0),
                      modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                      Text(book.title, style = MaterialTheme.typography.titleMedium)
                      Text("by ${book.author}", style = MaterialTheme.typography.bodyMedium)
                    }
                    Icon(
                      imageVector = Icons.Default.Delete,
                      contentDescription = "Delete book",
                      tint = Color.Gray,
                      modifier = Modifier
                        .size(20.dp)
                        .clickable { bookToDelete = book }
                    )
                  }

                  Spacer(Modifier.height(8.dp))

                  // Progress & quiz info
                  LinearProgressIndicator(
                    progress = book.progress,
                    color = Color(0xFF5AC8F0),
                    modifier = Modifier.fillMaxWidth()
                  )
                  Spacer(Modifier.height(4.dp))
                  Text(
                    "${(book.progress * 100).toInt()}% read",
                    style = MaterialTheme.typography.labelSmall
                  )
                  Text(
                    text = if (book.lastQuizScore >= 0)
                      "Quiz: ${book.lastQuizScore}/3"
                    else "No quiz taken",
                    style = MaterialTheme.typography.labelSmall
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // Delete confirmation dialog
  bookToDelete?.let { book ->
    AlertDialog(
      onDismissRequest = { bookToDelete = null },
      title = { Text("Delete \"${book.title}\"?") },
      text = { Text("This action cannot be undone.") },
      confirmButton = {
        TextButton(onClick = {
          dbHelper.deleteBook(book.id)
          bookToDelete = null
          refresh()
        }) { Text("Delete", color = Color.Red) }
      },
      dismissButton = {
        TextButton(onClick = { bookToDelete = null }) { Text("Cancel") }
      }
    )
  }
}
