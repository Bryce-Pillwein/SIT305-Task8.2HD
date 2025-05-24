package com.brycepillwein.task8_2hd.model

data class StoredBook(
  val id: Int = 0,
  val title: String,
  val author: String,
  val content: String,
  val progress: Float = 0f,
  val lastReadWordIndex: Int = 0,

  var lastQuizScore: Int = -1,  // -1 if no quiz taken
  var totalQuizzesTaken: Int = 0,
  var totalCorrectAnswers: Int = 0
)
