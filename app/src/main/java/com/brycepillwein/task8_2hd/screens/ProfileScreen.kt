package com.brycepillwein.task8_2hd.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.brycepillwein.library.components.ScreenWrapper
import com.brycepillwein.task8_2hd.viewmodel.StudentSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
  val context = LocalContext.current

  ScreenWrapper {
    Scaffold(
      containerColor = Color.Transparent,
      topBar = {
        TopAppBar(
          title = { Text("Profile") },
          colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
      }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .padding(innerPadding)
          .padding(horizontal = 24.dp, vertical = 32.dp)
          .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Profile Icon
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = "User",
          modifier = Modifier.size(64.dp),
          tint = Color(90, 200, 240)
        )

        // User Info
        Text(StudentSession.username, style = MaterialTheme.typography.headlineSmall)
        Text(StudentSession.email, style = MaterialTheme.typography.bodyMedium)

        HorizontalDivider()

        // Action Buttons
        Button(
          onClick = {
            Toast.makeText(context, "Edit profile not implemented", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = Color(90, 200, 240))
        ) {
          Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.padding(end = 8.dp))
          Text("Edit Profile")
        }

        Button(
          onClick = {
            StudentSession.username = ""
            StudentSession.email = ""
            StudentSession.interests = emptyList()
            navController.navigate("login") {
              popUpTo("login") { inclusive = true }
            }
          },
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = Color(230, 100, 100))
        ) {
          Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", modifier = Modifier.padding(end = 8.dp))
          Text("Sign Out")
        }
      }
    }
  }
}
