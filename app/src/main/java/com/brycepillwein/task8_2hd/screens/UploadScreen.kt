package com.brycepillwein.task8_2hd.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.brycepillwein.library.components.ScreenWrapper
import com.brycepillwein.task8_2hd.database.BookDbHelper
import com.brycepillwein.task8_2hd.model.StoredBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(navController: NavController, onBookSaved: () -> Unit = { }) {
  val context = LocalContext.current
  val dbHelper = remember { BookDbHelper(context) }
  val scope = rememberCoroutineScope()

  var title by remember { mutableStateOf("") }
  var author by remember { mutableStateOf("") }
  var fileUri by remember { mutableStateOf<Uri?>(null) }
  var rawText by remember { mutableStateOf("") }
  var fileContent by remember { mutableStateOf("") }

  val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    fileUri = uri
    if (uri != null) {
      scope.launch {
        fileContent = readTextFromUri(context, uri)
      }
    }
  }

  ScreenWrapper {
    Scaffold(
      containerColor = Color.Transparent,

      topBar = {
        TopAppBar(
          title = { Text("Add New Book") },
          navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
              Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
          },
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
          .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Title") },
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = author,
          onValueChange = { author = it },
          label = { Text("Author") },
          modifier = Modifier.fillMaxWidth()
        )

        Button(
          onClick = { filePicker.launch("*/*") },
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = Color(90, 200, 240))
        ) {
          Text(if (fileUri != null) "File Selected" else "Choose EPUB or Text File")
        }

        HorizontalDivider()

        Text("Or paste raw text:")

        OutlinedTextField(
          value = rawText,
          onValueChange = { rawText = it },
          label = { Text("Raw Text") },
          modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
        )

        Button(
          onClick = {
            val finalContent = if (fileContent.isNotBlank()) fileContent else rawText
            if (title.isNotBlank() && author.isNotBlank() && finalContent.isNotBlank()) {
              scope.launch {
                val book = StoredBook(title = title, author = author, content = finalContent)
                dbHelper.insertBook(book)
                onBookSaved()
              }
            }
          },
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = Color(90, 200, 240))
        ) {
          Text("Save Book")
        }
      }
    }
  }
}

suspend fun readTextFromUri(context: android.content.Context, uri: Uri): String {
  return withContext(Dispatchers.IO) {
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
      BufferedReader(InputStreamReader(inputStream)).readText()
    } ?: ""
  }
}
