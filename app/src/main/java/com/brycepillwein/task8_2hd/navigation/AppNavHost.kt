package com.brycepillwein.task8_2hd.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.brycepillwein.task8_2hd.screens.*


@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
  val navBackStackEntry = navController.currentBackStackEntryAsState().value
  val currentRoute = navBackStackEntry?.destination?.route

  val showBottomBar = currentRoute in listOf("dashboard", "library", "profile")


  Scaffold(
    bottomBar = {
      if (showBottomBar) {
        BottomNavigationBar(navController)
      }
    }
  ) { innerPadding ->
    NavHost(navController = navController, startDestination = "login", modifier = modifier.padding(innerPadding)) {
      composable("login") { LoginScreen(navController) }
      composable("register") { RegisterScreen(navController) }

      composable("dashboard") { DashboardScreen(navController) }
      composable("library") { LibraryScreen(navController) }
      composable("profile") { ProfileScreen(navController) }

      composable("settings") { SettingsScreen(navController) }

      composable("upload") {
        UploadScreen(navController,
          onBookSaved = {
          navController.popBackStack()
        })
      }

      composable("reader/{bookId}") { backStackEntry ->
        val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull()
        if (bookId != null) {
          ReadingScreen(bookId = bookId, navController = navController)
        }
      }

    }
  }

}
