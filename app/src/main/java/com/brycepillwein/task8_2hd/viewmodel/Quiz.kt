package com.brycepillwein.task8_2hd.viewmodel

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class QuizResponse(
  val quiz: List<QuizQuestion>
)

@Serializable
data class QuizQuestion(
  val question: String,
  val options: List<String>,
  val correct_answer: String
)
