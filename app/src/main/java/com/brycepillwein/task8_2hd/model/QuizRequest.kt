package com.brycepillwein.task8_2hd.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizRequest(
  val text: String
)
