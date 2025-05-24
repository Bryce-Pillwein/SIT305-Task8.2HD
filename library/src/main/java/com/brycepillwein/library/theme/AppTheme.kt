package com.brycepillwein.library.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.brycepillwein.library.theme.AppColors

val LocalAppColors = staticCompositionLocalOf { AppColors }

@Composable
fun AppTheme(content: @Composable () -> Unit) {
  CompositionLocalProvider(LocalAppColors provides AppColors) {
    // MaterialTheme still needed for typography etc., but colourScheme unused
    MaterialTheme(
      typography = Typography,
      content = content
    )
  }
}
