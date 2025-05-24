package com.brycepillwein.task8_2hd.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.brycepillwein.library.components.ScreenWrapper
import com.brycepillwein.library.components.Tt
import com.brycepillwein.task8_2hd.database.UserStorage
import com.brycepillwein.task8_2hd.viewModel.StudentSession


@Composable
fun LoginScreen(navController: NavController) {
  val context = LocalContext.current

  // Get saved user on launch
  val savedUser = remember { UserStorage.getUser(context) }
  var showQuickLogin by remember { mutableStateOf(savedUser != null) }

  var username by remember { mutableStateOf(savedUser?.name ?: "") }
  var password by remember { mutableStateOf("") }

  ScreenWrapper {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      Tt("HermesQuest", fontSize = 32.sp)
      Spacer(Modifier.height(32.dp))

      Tt("Login", fontSize = 20.sp, fontWeight = FontWeight.Bold)

      // Show "Continue as [user]" button
      if (showQuickLogin && savedUser != null) {
        TextButton(
          onClick = {
            StudentSession.username = savedUser.name
            StudentSession.email = savedUser.email
            navController.navigate("dashboard")
          },
          colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color(90, 200, 240)
          ),
        ) {
          Text("Continue as ${savedUser.name}")
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))
      }

      OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Username") }
      )
      Spacer(Modifier.height(12.dp))

      OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation()
      )

      Spacer(Modifier.height(24.dp))

      Button(
        onClick = {
          val user = UserStorage.getUser(context)
          if (user != null && user.name == username) {
            StudentSession.username = user.name
            StudentSession.email = user.email
            navController.navigate("dashboard")
          }
        },
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(90, 200, 240),
          contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 6.dp),
        modifier = Modifier.height(36.dp)
      ) {
        Tt("Login")
      }

      Spacer(Modifier.height(16.dp))
      TextButton(onClick = { navController.navigate("register") }) {
        Tt("Don't have an account? Register")
      }
    }
  }
}

