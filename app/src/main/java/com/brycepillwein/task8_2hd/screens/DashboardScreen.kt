package com.brycepillwein.task8_2hd.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.brycepillwein.library.components.ScreenWrapper
import com.brycepillwein.library.theme.LocalAppColors
import com.brycepillwein.task8_2hd.components.StatCard
import com.brycepillwein.task8_2hd.database.BookDbHelper
import com.brycepillwein.task8_2hd.model.StoredBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
  val context = LocalContext.current
  val dbHelper = remember { BookDbHelper(context) }
  var books by remember { mutableStateOf<List<StoredBook>>(emptyList()) }

  LaunchedEffect(true) {
    withContext(Dispatchers.IO) {
      books = dbHelper.getAllBooks()
    }
  }

  val totalBooks = books.size
  val totalWords = books.sumOf { it.content.split(" ").size }
  val totalWordsRead = books.sumOf { it.lastReadWordIndex }
  val booksCompleted = books.count {
    val wordCount = it.content.split(" ").size
    it.lastReadWordIndex >= wordCount
  }

  ScreenWrapper {
    Scaffold(
      containerColor = Color.Transparent,

      topBar = {
        TopAppBar(
          title = { Text("Dashboard") },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
          )
        )
      }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .padding(24.dp)
          .fillMaxSize()
      ) {
        val items = listOf(
          Triple("Books Completed", booksCompleted.toString(), Icons.Default.LibraryAddCheck),
          Triple("Books Added", totalBooks.toString(), Icons.Default.CollectionsBookmark),
          Triple("Words Read", totalWordsRead.toString(), Icons.Default.RemoveRedEye),
          Triple("Total Words", totalWords.toString(), Icons.Default.TextFields)
        )

        // 2-column grid layout
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          for (row in items.chunked(2)) {
            Row(
              horizontalArrangement = Arrangement.spacedBy(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              row.forEach { (label, value, icon) ->
                StatCard(label = label, value = value, icon = icon, modifier = Modifier.weight(1f))
              }
              if (row.size == 1) Spacer(Modifier.weight(1f)) // fill empty column
            }
          }
        }
      }
    }
  }
}