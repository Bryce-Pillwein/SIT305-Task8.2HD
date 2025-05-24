package com.brycepillwein.task8_2hd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.brycepillwein.library.theme.AppTheme
import com.brycepillwein.task8_2hd.navigation.AppNavHost

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AppTheme {
        val navController = rememberNavController()
        AppNavHost(navController)
      }
    }
  }
}
