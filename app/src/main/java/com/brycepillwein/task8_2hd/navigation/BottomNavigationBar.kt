package com.brycepillwein.task8_2hd.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.brycepillwein.library.theme.LocalAppColors

@Composable
fun BottomNavigationBar(navController: NavController) {
  val navBackStackEntry = navController.currentBackStackEntryAsState().value
  val currentRoute = navBackStackEntry?.destination?.route

  NavigationBar(
    tonalElevation = 0.dp,
    containerColor = LocalAppColors.current.hsl95
  ) {

    NavigationBarItem(
      selected = currentRoute == "dashboard",
      onClick = {
        if (currentRoute != "dashboard") {
          navController.navigate("dashboard") {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
          }
        }
      },
      icon = {
        Icon(
          imageVector = Icons.Default.Home,
          contentDescription = "Home",
          modifier = Modifier.size(20.dp)
        )
      },
      label = {
        Text(
          "Home",
          modifier = Modifier.padding(top = 0.dp),
          style = MaterialTheme.typography.labelSmall
        )
      },
      alwaysShowLabel = true,
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0, 120, 150),
        selectedTextColor = Color(0, 120, 150),
        indicatorColor =  LocalAppColors.current.hsl95,
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Gray
      )
    )

    NavigationBarItem(
      selected = currentRoute == "library",
      onClick = {
        if (currentRoute != "library") {
          navController.navigate("library") {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
          }
        }
      },
      icon = {
        Icon(
          imageVector = Icons.Default.CollectionsBookmark,
          contentDescription = "Library",
          modifier = Modifier.size(20.dp)
        )
      },
      label = {
        Text(
          "Library",
          modifier = Modifier.padding(top = 0.dp),
          style = MaterialTheme.typography.labelSmall
        )
      },
      alwaysShowLabel = true,
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0, 120, 150),
        selectedTextColor = Color(0, 120, 150),
        indicatorColor =  LocalAppColors.current.hsl95,
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Gray
      )
    )


    NavigationBarItem(
      selected = currentRoute == "profile",
      onClick = {
        if (currentRoute != "profile") {
          navController.navigate("profile") {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
          }
        }
      },
      icon = {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = "Profile",
          modifier = Modifier.size(20.dp)
        )
      },
      label = {
        Text(
          "Profile",
          modifier = Modifier.padding(top = 0.dp),
          style = MaterialTheme.typography.labelSmall
        )
      },
      alwaysShowLabel = true,
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0, 120, 150),
        selectedTextColor = Color(0, 120, 150),
        indicatorColor =  LocalAppColors.current.hsl95,
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Gray
      )
    )




  }
}
