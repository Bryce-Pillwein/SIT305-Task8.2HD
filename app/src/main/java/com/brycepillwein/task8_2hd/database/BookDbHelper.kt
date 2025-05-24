package com.brycepillwein.task8_2hd.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.brycepillwein.task8_2hd.model.StoredBook

private const val DB_NAME = "books.db"
private const val DB_VERSION = 1

private const val TABLE_NAME = "books"
private const val COL_ID = "id"
private const val COL_TITLE = "title"
private const val COL_AUTHOR = "author"
private const val COL_CONTENT = "content"
private const val COL_PROGRESS = "progress"
private const val COL_LAST_INDEX = "last_read_index"

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
                $COL_LAST_INDEX INTEGER NOT NULL
            )
        """.trimIndent()
    db.execSQL(createStmt)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    onCreate(db)
  }

  fun insertBook(book: StoredBook) {
    writableDatabase.use { db ->
      val cv = ContentValues().apply {
        put(COL_TITLE, book.title)
        put(COL_AUTHOR, book.author)
        put(COL_CONTENT, book.content)
        put(COL_PROGRESS, book.progress)
        put(COL_LAST_INDEX, book.lastReadWordIndex)
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
              lastReadWordIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LAST_INDEX))
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
            lastReadWordIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LAST_INDEX))
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
        put("last_read_index", wordIndex)
        put("progress", progress)
      }
      db.update("books", cv, "id=?", arrayOf(bookId.toString()))
    }
  }


}
