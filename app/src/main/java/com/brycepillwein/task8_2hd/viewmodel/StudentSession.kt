package com.brycepillwein.task8_2hd.viewmodel


object StudentSession {
  var username: String = ""
  var email: String = ""
  var interests: List<String> = emptyList()

  fun reset() {
    username = ""
    email = ""
    interests = emptyList()
  }
}
