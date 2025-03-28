package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE =
    "com.bignerdranch.android.geoquiz.answer_is_true"
private const val HAS_CHEATED_KEY = "has_cheated"

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding

    private var answerIsTrue = false

    private var hasCheated = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(HAS_CHEATED_KEY, hasCheated)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        //Based on saved instance state, check if the user has cheated through hasCheated variable
        //If null set to false
        hasCheated = savedInstanceState?.getBoolean(HAS_CHEATED_KEY, false) ?: false

        //Move answer text outside of onClickListener
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }

        //If user has cheated run setAnswerShownResult
        //Text is updated properly
        if (hasCheated) {
            setAnswerShownResult(true)
            binding.answerTextView.setText(answerText)
        }

        binding.showAnswerButton.setOnClickListener {
            binding.answerTextView.setText(answerText)
            //set has cheated to true to save after screen rotation
            hasCheated = true
            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}
