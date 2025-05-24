package com.brycepillwein.task8_2hd.network

import com.brycepillwein.task8_2hd.viewmodel.QuizResponse
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object QuizApi {
  private val client = HttpClient(CIO) {
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
        isLenient = true
      })
    }
  }

  suspend fun getQuiz(topic: String): QuizResponse {
    val response: HttpResponse = client.get("http://10.0.2.2:5000/getQuiz") {
      parameter("topic", topic)
    }
    return response.body()
  }
}
