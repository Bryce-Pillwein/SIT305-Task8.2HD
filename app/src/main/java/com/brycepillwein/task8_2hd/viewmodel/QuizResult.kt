package com.brycepillwein.task8_2hd.viewmodel

import com.brycepillwein.task8_2hd.viewmodel.QuizQuestion

data class QuizResult(
  val title: String,
  val score: Int,
  val total: Int,
  val questions: List<QuizQuestion>,
  val userAnswers: List<String>
)