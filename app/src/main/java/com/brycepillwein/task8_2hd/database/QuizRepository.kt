package com.brycepillwein.task8_2hd.database

import com.brycepillwein.task8_2hd.database.QuizResultDao
import com.brycepillwein.task8_2hd.database.QuizResultEntity

class QuizRepository(private val dao: QuizResultDao) {
  suspend fun insert(result: QuizResultEntity) = dao.insertResult(result)
  suspend fun getAll() = dao.getAllResults()
}
