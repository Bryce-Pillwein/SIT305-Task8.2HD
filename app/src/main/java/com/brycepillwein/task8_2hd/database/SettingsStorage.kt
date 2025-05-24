package com.brycepillwein.task8_2hd.database

import android.content.Context
import android.content.SharedPreferences
import com.brycepillwein.task8_2hd.model.ReadingSettings
import com.brycepillwein.task8_2hd.model.TextAlignment

object SettingsStorage {
  private const val PREF_NAME = "reading_settings"
  private const val KEY_SIZE = "text_size"
  private const val KEY_WPM = "wpm"
  private const val KEY_FONT = "font"
  private const val KEY_ALIGN = "alignment"
  private const val KEY_WORDS_PER_PAGE = "words_per_page"

  fun save(context: Context, settings: ReadingSettings) {
    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().apply {
      putFloat(KEY_SIZE, settings.textSizeSp)
      putInt(KEY_WPM, settings.wpm)
      putString(KEY_FONT, settings.fontFamily)
      putString(KEY_ALIGN, settings.alignment.name)
      putInt(KEY_WORDS_PER_PAGE, settings.wordsPerPage)
      apply()
    }
  }

  fun load(context: Context): ReadingSettings {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return ReadingSettings(
      textSizeSp = prefs.getFloat(KEY_SIZE, 28f),
      wpm = prefs.getInt(KEY_WPM, 300),
      fontFamily = prefs.getString(KEY_FONT, "default") ?: "default",
      alignment = TextAlignment.valueOf(
        prefs.getString(KEY_ALIGN, TextAlignment.LEFT.name) ?: TextAlignment.LEFT.name
      ),
      wordsPerPage = prefs.getInt(KEY_WORDS_PER_PAGE, 120)

    )
  }
}
