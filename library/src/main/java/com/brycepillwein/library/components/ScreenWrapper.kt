package com.brycepillwein.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brycepillwein.library.theme.LocalAppColors

@Composable
fun ScreenWrapper(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val colors = LocalAppColors.current

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(colors.hsl95)
      .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.systemBars) // Safe area
  ) {
    content()
  }
}
