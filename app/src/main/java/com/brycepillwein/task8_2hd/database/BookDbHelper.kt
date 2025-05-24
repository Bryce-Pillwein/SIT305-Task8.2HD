package com.brycepillwein.task8_2hd.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.brycepillwein.task8_2hd.model.StoredBook

private const val DB_NAME = "books.db"
private const val DB_VERSION = 2

private const val TABLE_NAME = "books"
private const val COL_ID = "id"
private const val COL_TITLE = "title"
private const val COL_AUTHOR = "author"
private const val COL_CONTENT = "content"
private const val COL_PROGRESS = "progress"
private const val COL_LAST_INDEX = "last_read_index"
private const val COL_QUIZ_SCORE = "last_quiz_score"
private const val COL_TOTAL_QUIZZES = "total_quizzes_taken"
private const val COL_TOTAL_CORRECT = "total_correct_answers"

class BookDbHelper(context: Context) :
  SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

  override fun onCreate(db: SQLiteDatabase) {
    val createStmt = """
      CREATE TABLE $TABLE_NAME (
        $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COL_TITLE TEXT NOT NULL,
        $COL_AUTHOR TEXT NOT NULL,
        $COL_CONTENT TEXT NOT NULL,
        $COL_PROGRESS REAL NOT NULL,
        $COL_LAST_INDEX INTEGER NOT NULL,
        $COL_QUIZ_SCORE INTEGER DEFAULT -1,
        $COL_TOTAL_QUIZZES INTEGER DEFAULT 0,
        $COL_TOTAL_CORRECT INTEGER DEFAULT 0
      )
    """.trimIndent()
    db.execSQL(createStmt)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    if (oldVersion < 2) {
      db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COL_QUIZ_SCORE INTEGER DEFAULT -1")
      db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COL_TOTAL_QUIZZES INTEGER DEFAULT 0")
      db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COL_TOTAL_CORRECT INTEGER DEFAULT 0")
    }
  }

  fun insertBook(book: StoredBook) {
    writableDatabase.use { db ->
      val cv = ContentValues().apply {
        put(COL_TITLE, book.title)
        put(COL_AUTHOR, book.author)
        put(COL_CONTENT, book.content)
        put(COL_PROGRESS, book.progress)
        put(COL_LAST_INDEX, book.lastReadWordIndex)
        put(COL_QUIZ_SCORE, book.lastQuizScore)
        put(COL_TOTAL_QUIZZES, book.totalQuizzesTaken)
        put(COL_TOTAL_CORRECT, book.totalCorrectAnswers)
      }
      db.insert(TABLE_NAME, null, cv)
    }
  }

  fun getAllBooks(): List<StoredBook> {
    val results = mutableListOf<StoredBook>()
    readableDatabase.use { db ->
      db.query(TABLE_NAME, null, null, null, null, null, "$COL_ID DESC").use { cursor ->
        while (cursor.moveToNext()) {
          results.add(
            StoredBook(
              id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
              title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
              author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR)),
              content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT)),
              progress = cursor.getFloat(cursor.getColumnIndexOrThrow(COL_PROGRESS)),
              lastReadWordIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LAST_INDEX)),
              lastQuizScore = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUIZ_SCORE)),
              totalQuizzesTaken = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TOTAL_QUIZZES)),
              totalCorrectAnswers = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TOTAL_CORRECT))
            )
          )
        }
      }
    }
    return results
  }

  fun getBookById(bookId: Int): StoredBook? {
    readableDatabase.use { db ->
      db.query(TABLE_NAME, null, "$COL_ID=?", arrayOf(bookId.toString()), null, null, null).use { cursor ->
        if (cursor.moveToFirst()) {
          return StoredBook(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
            title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
            author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR)),
            content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT)),
            progress = cursor.getFloat(cursor.getColumnIndexOrThrow(COL_PROGRESS)),
            lastReadWordIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LAST_INDEX)),
            lastQuizScore = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUIZ_SCORE)),
            totalQuizzesTaken = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TOTAL_QUIZZES)),
            totalCorrectAnswers = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TOTAL_CORRECT))
          )
        }
      }
    }
    return null
  }

  fun updateProgress(bookId: Int, wordIndex: Int, totalWords: Int) {
    val progress = wordIndex.toFloat() / totalWords
    writableDatabase.use { db ->
      val cv = ContentValues().apply {
        put(COL_LAST_INDEX, wordIndex)
        put(COL_PROGRESS, progress)
      }
      db.update(TABLE_NAME, cv, "$COL_ID=?", arrayOf(bookId.toString()))
    }
  }

  fun updateQuizStats(bookId: Int, score: Int, correctAnswers: Int) {
    writableDatabase.use { db ->
      val cv = ContentValues().apply {
        put(COL_QUIZ_SCORE, score)
        put(COL_TOTAL_QUIZZES, getBookById(bookId)?.totalQuizzesTaken?.plus(1) ?: 1)
        put(COL_TOTAL_CORRECT, getBookById(bookId)?.totalCorrectAnswers?.plus(correctAnswers) ?: correctAnswers)
      }
      db.update(TABLE_NAME, cv, "$COL_ID=?", arrayOf(bookId.toString()))
    }
  }
}
