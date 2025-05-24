package com.brycepillwein.task8_2hd.model

data class QuizResult(
  val question: String,
  val options: List<String>,
  val correctAnswer: String,
  val userAnswer: String
)