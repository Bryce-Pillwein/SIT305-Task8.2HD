package com.brycepillwein.task8_2hd.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.brycepillwein.task8_2hd.model.QuizResult
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

      composable(
        route = "quiz/{bookId}",
        arguments = listOf(navArgument("bookId") { type = NavType.IntType })
      ) { backStack ->
        val bookId = backStack.arguments!!.getInt("bookId")

        // ✅ Get the quizText passed from ReadingScreen
        val quizText = navController
          .previousBackStackEntry
          ?.savedStateHandle
          ?.get<String>("quizText") ?: ""

        QuizScreen(
          bookId = bookId,
          navController = navController
        )
      }

      composable(
        "quizResults/{bookId}",
        arguments = listOf(navArgument("bookId") { type = NavType.IntType })
      ) { backStackEntry ->
        val results = navController.previousBackStackEntry
          ?.savedStateHandle
          ?.get<List<QuizResult>>("quizResults")

        val bookId = backStackEntry.arguments?.getInt("bookId")

        if (results != null && bookId != null) {
          QuizResultsScreen(
            bookId = bookId,
            results = results,
            navController = navController
          )
        }
      }



    }
  }

}
