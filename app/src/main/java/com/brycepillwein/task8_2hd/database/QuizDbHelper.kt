package com.brycepillwein.task8_2hd.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.brycepillwein.task8_2hd.viewmodel.QuizResult

private const val DB_NAME = "quiz_results.db"
private const val DB_VERSION = 1

private const val TABLE_NAME = "quiz_results"
private const val COL_ID = "id"
private const val COL_TOPIC = "topic"
private const val COL_SCORE = "score"
private const val COL_TOTAL = "total"
private const val COL_USER_ANSWERS = "user_answers_json"
private const val COL_QUIZ = "quiz_json"
private const val COL_TIMESTAMP = "timestamp"

class QuizDbHelper(context: Context) :
  SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

  override fun onCreate(db: SQLiteDatabase) {
    val createStmt = """
      CREATE TABLE $TABLE_NAME (
        $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COL_TOPIC TEXT NOT NULL,
        $COL_SCORE INTEGER NOT NULL,
        $COL_TOTAL INTEGER NOT NULL,
        $COL_USER_ANSWERS TEXT NOT NULL,
        $COL_QUIZ TEXT NOT NULL,
        $COL_TIMESTAMP INTEGER NOT NULL
      )
    """.trimIndent()
    db.execSQL(createStmt)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    onCreate(db)
  }

  fun insertQuizResult(result: QuizResult, quizJson: String, userAnswersJson: String, timestamp: Long) {
    writableDatabase.use { db ->
      val cv = ContentValues().apply {
        put(COL_TOPIC, result.title)
        put(COL_SCORE, result.score)
        put(COL_TOTAL, result.total)
        put(COL_USER_ANSWERS, userAnswersJson)
        put(COL_QUIZ, quizJson)
        put(COL_TIMESTAMP, timestamp)
      }
      db.insert(TABLE_NAME, null, cv)
    }
  }

  fun getAllResults(): List<StoredQuizResult> {
    val results = mutableListOf<StoredQuizResult>()
    readableDatabase.use { db ->
      db.query(TABLE_NAME, null, null, null, null, null, "$COL_TIMESTAMP DESC").use { cursor ->
        while (cursor.moveToNext()) {
          results.add(
            StoredQuizResult(
              topic = cursor.getString(cursor.getColumnIndexOrThrow(COL_TOPIC)),
              score = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCORE)),
              total = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TOTAL)),
              userAnswersJson = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ANSWERS)),
              quizJson = cursor.getString(cursor.getColumnIndexOrThrow(COL_QUIZ)),
              timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIMESTAMP))
            )
          )
        }
      }
    }
    return results
  }
}

data class StoredQuizResult(
  val topic: String,
  val score: Int,
  val total: Int,
  val userAnswersJson: String,
  val quizJson: String,
  val timestamp: Long
)
