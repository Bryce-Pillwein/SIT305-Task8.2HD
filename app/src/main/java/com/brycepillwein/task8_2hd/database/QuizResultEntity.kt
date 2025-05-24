package com.brycepillwein.task8_2hd.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val topic: String,
  val score: Int,
  val total: Int,
  val userAnswersJson: String,
  val quizJson: String,
  val timestamp: Long
)
