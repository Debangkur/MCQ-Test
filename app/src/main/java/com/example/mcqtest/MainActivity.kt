package com.example.mcqtest

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    data class Question(
        val text: String,
        val options: List<String>,
        val correctIndex: Int
    )

    private val questions = listOf(
        Question("What is the value of x in the equation...", listOf("x = 4", "x = 6", "x = 8", "x = 10"), 2),
        // Add more...
    )

    private var currentIndex = 0
    private var score = 0

    private lateinit var questionText: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var questionProgress: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var timerText: TextView

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button

    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis = 15 * 60 * 1000L // 15 minutes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionText = findViewById(R.id.questionText)
        optionsGroup = findViewById(R.id.optionsGroup)
        questionProgress = findViewById(R.id.questionProgress)
        progressBar = findViewById(R.id.progressBar)
        timerText = findViewById(R.id.timerText)

        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)

        startTimer()
        loadQuestion()

        nextButton.setOnClickListener {
            if (currentIndex < questions.size - 1) {
                currentIndex++
                loadQuestion()
            }
        }

        prevButton.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                loadQuestion()
            }
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val minutes = (timeLeftInMillis / 1000) / 60
                val seconds = (timeLeftInMillis / 1000) % 60
                timerText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Time's up!", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun loadQuestion() {
        val question = questions[currentIndex]
        questionText.text = question.text
        questionProgress.text = "Question ${currentIndex + 1} of ${questions.size}"
        progressBar.progress = ((currentIndex + 1) * 100 / questions.size)

        optionsGroup.removeAllViews()
        for ((i, option) in question.options.withIndex()) {
            val radioButton = RadioButton(this)
            radioButton.text = option
            optionsGroup.addView(radioButton)
        }
    }
}
