package com.brycepillwein.task8_2hd.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.zip.ZipInputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
  navController: NavController,
  onBookSaved: () -> Unit = { }
) {
  val context = LocalContext.current
  val dbHelper = remember { BookDbHelper(context) }
  val scope = rememberCoroutineScope()

  var title by remember { mutableStateOf("") }
  var author by remember { mutableStateOf("") }
  var fileUri by remember { mutableStateOf<Uri?>(null) }
  var fileContent by remember { mutableStateOf("") }
  var rawText by remember { mutableStateOf("") }

  val filePicker = rememberLauncherForActivityResult(
    ActivityResultContracts.GetContent()
  ) { uri ->
    fileUri = uri
    if (uri != null) {
      scope.launch {
        // 1) Try MIME type first
        val mime = context.contentResolver.getType(uri) ?: ""
        val pathSegment = uri.lastPathSegment ?: ""
        val isEpub = mime.contains("epub", ignoreCase = true)
                || pathSegment.endsWith(".epub", ignoreCase = true)

        Log.d("UPLOAD", "Picked URI=$uri; MIME=$mime; lastPath=$pathSegment; isEpub=$isEpub")

        fileContent = if (isEpub) {
          readEpubFromUri(context, uri)
        } else {
          readTextFromUri(context, uri)
        }
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
          colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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

        Divider()

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
                val book = StoredBook(
                  title = title,
                  author = author,
                  content = finalContent
                )
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

/** Read plain‐text files */
suspend fun readTextFromUri(context: android.content.Context, uri: Uri): String {
  return withContext(Dispatchers.IO) {
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
      BufferedReader(InputStreamReader(inputStream)).readText()
    } ?: ""
  }
}

/** Read and extract all XHTML/HTML from an EPUB into one plain‐text string */
suspend fun readEpubFromUri(context: android.content.Context, uri: Uri): String {
  return withContext(Dispatchers.IO) {
    // 1) Log entry so we know it's being called
    Log.d("EPUB_EXTRACT", "readEpubFromUri: starting EPUB extraction for URI: $uri")

    val extracted = buildString {
      context.contentResolver.openInputStream(uri)?.use { inputStream ->
        ZipInputStream(inputStream).use { zip ->
          var entry = zip.nextEntry
          while (entry != null) {
            if (!entry.isDirectory &&
              (entry.name.endsWith(".xhtml", true) || entry.name.endsWith(".html", true))) {
              // 2) Read as UTF-8 explicitly
              val html = BufferedReader(InputStreamReader(zip, Charsets.UTF_8)).readText()
              // 3) Strip tags via Jsoup
              val text = Jsoup.parse(html).text()
              append(text).append("\n\n")
            }
            zip.closeEntry()
            entry = zip.nextEntry
          }
        }
      } ?: Log.w("EPUB_EXTRACT", "readEpubFromUri: could not open InputStream for $uri")
    }

    // 4) Log a preview so you can inspect in Logcat
    val preview = if (extracted.length > 2000) {
      extracted.take(2000) + "\n…(truncated)"
    } else extracted

    Log.d(
      "EPUB_EXTRACT",
      "\n=== EPUB TEXT START ===\n$preview\n=== EPUB TEXT END ==="
    )
    extracted
  }
}
