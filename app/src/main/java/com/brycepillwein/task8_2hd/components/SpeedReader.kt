package com.brycepillwein.task8_2hd.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brycepillwein.task8_2hd.model.TextAlignment
import kotlinx.coroutines.delay

@Composable
fun SpeedReader(
  text: String,
  wpm: Int = 300,
  textSizeSp: Float = 28f,
  fontFamily: String = "default",
  alignment: TextAlignment = TextAlignment.LEFT,
  isPlaying: Boolean,
  startIndex: Int = 0,
  onWordIndexChange: (Int) -> Unit,
  wordsPerPage: Int = 120
) {
  val words = remember(text) { text.split(" ") }
  val pageSize = wordsPerPage
  val pages = remember(words) { words.chunked(pageSize) }

  var currentIndex by remember { mutableStateOf(startIndex) }

  LaunchedEffect(startIndex) {
    currentIndex = startIndex
  }

  val currentPageIndex = currentIndex / pageSize
  val currentPage = pages.getOrNull(currentPageIndex) ?: emptyList()
  val globalStartIndex = currentPageIndex * pageSize

  val resolvedFontFamily = when (fontFamily.lowercase()) {
    "serif" -> FontFamily.Serif
    "monospace" -> FontFamily.Monospace
    "sans" -> FontFamily.SansSerif
    else -> FontFamily.Default
  }

  val textAlign = when (alignment) {
    TextAlignment.LEFT -> TextAlign.Left
    TextAlignment.CENTER -> TextAlign.Center
    TextAlignment.RIGHT -> TextAlign.Right
    TextAlignment.JUSTIFY -> TextAlign.Justify
  }

  val delayMillis = (60_000f / wpm).toLong()

  LaunchedEffect(isPlaying, currentIndex, wpm, text) {
    if (isPlaying) {
      while (currentIndex < words.lastIndex) {
        delay(delayMillis)
        currentIndex++
        onWordIndexChange(currentIndex)
      }
    }
  }

  // ✅ Build styled text for the current page
  val styledText = buildAnnotatedString {
    currentPage.forEachIndexed { localIndex, word ->
      val globalIndex = globalStartIndex + localIndex
      withStyle(
        style = SpanStyle(
          color = Color.Black.copy(alpha = if (globalIndex == currentIndex) 1f else 0.2f),
          fontFamily = resolvedFontFamily,
          fontSize = textSizeSp.sp
        )
      ) {
        append("$word ")
      }
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = styledText,
      textAlign = textAlign,
      lineHeight = (textSizeSp * 1.4f).sp,
      modifier = Modifier.fillMaxWidth()
    )
  }
}
