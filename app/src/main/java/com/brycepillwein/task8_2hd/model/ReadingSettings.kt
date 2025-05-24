package com.brycepillwein.task8_2hd.model

enum class TextAlignment {
  LEFT, CENTER, RIGHT, JUSTIFY
}

data class ReadingSettings(
  val textSizeSp: Float = 24f,
  val wpm: Int = 300,
  val fontFamily: String = "default",
  val alignment: TextAlignment = TextAlignment.LEFT,
  val wordsPerPage: Int = 120
)
