package com.brycepillwein.task8_2hd.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brycepillwein.task8_2hd.model.QuizItem
import com.brycepillwein.task8_2hd.network.QuizApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
  private val _questions = MutableStateFlow<List<QuizItem>>(emptyList())
  val questions: StateFlow<List<QuizItem>> = _questions

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading

  private val _error = MutableStateFlow<String?>(null)
  val error: StateFlow<String?> = _error


  fun loadQuiz(textSlice: String) {
    viewModelScope.launch {
      _isLoading.value = true
      _error.value = null
      try {
        val resp = QuizApi.getQuiz(textSlice)
        _questions.value = resp.quiz
      } catch (t: Throwable) {
        _error.value = t.localizedMessage
      } finally {
        _isLoading.value = false
      }
    }
  }
}
