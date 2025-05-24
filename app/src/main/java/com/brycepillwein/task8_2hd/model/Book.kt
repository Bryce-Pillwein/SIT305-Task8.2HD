package com.brycepillwein.task8_2hd.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val title: String,
  val author: String,
  val content: String, // Raw extracted text from EPUB or manual input
  val progress: Float = 0f, // 0.0 to 1.0
  val lastReadWordIndex: Int = 0
)
