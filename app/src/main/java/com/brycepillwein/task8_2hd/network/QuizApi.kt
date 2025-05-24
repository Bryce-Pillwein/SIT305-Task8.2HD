package com.brycepillwein.task8_2hd.network

import com.brycepillwein.task8_2hd.model.QuizRequest
import com.brycepillwein.task8_2hd.model.QuizResponse
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object QuizApi {
  private const val BASE_URL = "http://10.0.2.2:5000"

  private val client = HttpClient(CIO) {
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
        isLenient = true
      })
    }
    install(Logging) {
      logger = Logger.SIMPLE
      level = LogLevel.ALL       // log headers, bodies, etc
    }
  }

  /**
   * Sends the full text slice to the backend and retrieves a QuizResponse.
   * Logs request and response details.
   */
  suspend fun getQuiz(textSlice: String): QuizResponse {
    println("QuizApi ► getQuiz() textSlice length=${textSlice.length}")
    try {
      val response = client.post("$BASE_URL/getQuiz") {
        contentType(ContentType.Application.Json)
        setBody(QuizRequest(text = textSlice))
      }
      // You should see the raw JSON in your Logcat now.
      val quizResp: QuizResponse = response.body()
      println("QuizApi ◀ Success: received quiz with ${quizResp.quiz.size} items")
      return quizResp
    } catch (t: Throwable) {
      println("QuizApi ✖ Error fetching quiz: ${t::class.simpleName}: ${t.message}")
      t.printStackTrace()
      throw t
    }
  }
}
