package com.brycepillwein.task8_2hd.screens

import android.widget.Toast
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
import com.brycepillwein.library.components.Btn
import com.brycepillwein.library.components.ScreenWrapper
import com.brycepillwein.library.components.Tt
import com.brycepillwein.task8_2hd.database.UserStorage
import com.brycepillwein.task8_2hd.viewmodel.StudentSession
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) {
  val context = LocalContext.current

  var username by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }

  ScreenWrapper {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Tt(
        text = "Create Account",
        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
        fontWeight = MaterialTheme.typography.headlineMedium.fontWeight ?: FontWeight.Bold
      )

      Spacer(Modifier.height(24.dp))

      OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
      Spacer(Modifier.height(12.dp))

      OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
      Spacer(Modifier.height(12.dp))

      OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
      Spacer(Modifier.height(12.dp))

      OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation())

      Spacer(Modifier.height(24.dp))

      Button(
        onClick = {
          if (username.isNotBlank() && email.isNotBlank() && password == confirmPassword) {
            StudentSession.username = username
            StudentSession.email = email
            UserStorage.saveUser(context, username, email)
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
        Text("Create Account")
      }


      Spacer(Modifier.height(16.dp))

      TextButton(onClick = { navController.popBackStack() }) {
        Tt("Already have an account? Login")
      }
    }
  }
}
