package com.brycepillwein.task8_2hd.model

data class StoredBook(
  val id: Int = 0,
  val title: String,
  val author: String,
  val content: String,
  val progress: Float = 0f,
  val lastReadWordIndex: Int = 0,
  val lastQuizScore: Int = -1,
  val totalQuizzesTaken: Int = 0,
  val totalCorrectAnswers: Int = 0,
  val totalQuestions: Int = 0
)
