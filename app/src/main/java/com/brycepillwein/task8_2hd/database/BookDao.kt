package com.brycepillwein.task8_2hd.database

import androidx.room.*
import com.brycepillwein.task8_2hd.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
  @Query("SELECT * FROM books")
  fun getAllBooks(): Flow<List<Book>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBook(book: Book)

  @Update
  suspend fun updateBook(book: Book)

  @Delete
  suspend fun deleteBook(book: Book)

  @Query("SELECT * FROM books WHERE id = :bookId LIMIT 1")
  suspend fun getBookById(bookId: Int): Book?
}
