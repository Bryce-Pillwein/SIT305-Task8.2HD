package com.brycepillwein.task8_2hd.database

import android.content.Context
import android.content.SharedPreferences
import com.brycepillwein.task8_2hd.model.User

object UserStorage {
  private const val PREFS_NAME = "user_prefs"
  private const val KEY_NAME = "username"
  private const val KEY_EMAIL = "email"

  fun saveUser(context: Context, name: String, email: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit()
      .putString(KEY_NAME, name)
      .putString(KEY_EMAIL, email)
      .apply()
  }

  fun getUser(context: Context): User? {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val name = prefs.getString(KEY_NAME, null)
    val email = prefs.getString(KEY_EMAIL, null)
    return if (name != null && email != null) User(name, email) else null
  }

  fun clearUser(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().clear().apply()
  }
}
