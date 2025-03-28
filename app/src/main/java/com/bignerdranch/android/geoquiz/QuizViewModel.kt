package com.bignerdranch.android.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var cheatedQuestions: MutableMap<Int, Boolean> // idea taken from savedStateHandle definition
        get() = savedStateHandle.get<Map<Int, Boolean>>(IS_CHEATER_KEY)?.toMutableMap() ?: mutableMapOf()
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value) // Set to savedState

    val isCheaterForCurrentQuestion: Boolean // Checks if the user is a cheater for the question
        get() = cheatedQuestions[currentIndex] == true // Default is false, true if cheated

    fun markCurrentQuestionAsCheated() { // Marks the question as cheated on
        val updatedMap = cheatedQuestions
        updatedMap[currentIndex] = true // Updates the question to show it is cheated on
        cheatedQuestions = updatedMap
        savedStateHandle.set(IS_CHEATER_KEY, cheatedQuestions) // Persist the change
    }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}
