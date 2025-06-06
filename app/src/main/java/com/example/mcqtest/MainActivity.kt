package com.example.mcqtest

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    data class Question(
        val text: String,
        val options: List<String>,
        val correctIndex: Int
    )

    private val questions = listOf(
        Question("What is the value of x in the equation 2x = 16?", listOf("x = 4", "x = 6", "x = 8", "x = 10"), 2),
        Question("Capital of France?", listOf("London", "Berlin", "Madrid", "Paris"), 3),
        Question("Which number is prime?", listOf("6", "8", "11", "9"), 2),
        Question("What is the square root of 81?", listOf("7", "8", "9", "10"), 2),
        Question("Largest planet?", listOf("Earth", "Mars", "Jupiter", "Venus"), 2),
    )

    private var currentIndex = 0
    private var score = 0
    private var timeLeftInMillis = 1 * 60 * 1000L // 1 minute

    private lateinit var questionText: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var questionProgress: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var timerText: TextView

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var submitButton: Button
    private lateinit var backButton: Button

    private lateinit var countDownTimer: CountDownTimer
    private val selectedAnswers = mutableMapOf<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        questionText = findViewById(R.id.questionText)
        optionsGroup = findViewById(R.id.optionsGroup)
        questionProgress = findViewById(R.id.questionProgress)
        progressBar = findViewById(R.id.progressBar)
        timerText = findViewById(R.id.timerText)

        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)
        submitButton = findViewById(R.id.submitButton)
        backButton = findViewById(R.id.backButton)

        // Initial visibility
        submitButton.isVisible = false
        backButton.isVisible = false

        startTimer()
        loadQuestion()

        nextButton.setOnClickListener {
            saveSelectedAnswer()
            if (currentIndex < questions.size - 1) {
                currentIndex++
                loadQuestion()
            }
        }

        prevButton.setOnClickListener {
            saveSelectedAnswer()
            if (currentIndex > 0) {
                currentIndex--
                loadQuestion()
            }
        }

        submitButton.setOnClickListener {
            saveSelectedAnswer()
            score = calculateScore()
            Toast.makeText(this, "Your Score: $score / ${questions.size}", Toast.LENGTH_LONG).show()
            disableAllControls()
        }

        backButton.setOnClickListener {
            resetQuiz()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                score = calculateScore()
                Toast.makeText(this@MainActivity, "Your Score: $score / ${questions.size}", Toast.LENGTH_LONG).show()
                disableAllControls()
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
            if (selectedAnswers[currentIndex] == i) {
                radioButton.isChecked = true
            }
        }

        prevButton.isEnabled = currentIndex > 0
        nextButton.isVisible = currentIndex < questions.size - 1
        submitButton.isVisible = currentIndex == questions.size - 1
        backButton.isVisible = false
    }

    private fun saveSelectedAnswer() {
        val selectedId = optionsGroup.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedId)
            val selectedIndex = optionsGroup.indexOfChild(selectedRadioButton)
            selectedAnswers[currentIndex] = selectedIndex
        }
    }

    private fun calculateScore(): Int {
        var total = 0
        for ((index, question) in questions.withIndex()) {
            if (selectedAnswers[index] == question.correctIndex) {
                total++
            }
        }
        return total
    }

    private fun disableAllControls() {
        nextButton.isEnabled = false
        prevButton.isEnabled = false
        submitButton.isEnabled = false
        optionsGroup.isEnabled = false
        backButton.isVisible = true
    }

    private fun resetQuiz() {
        currentIndex = 0
        score = 0
        selectedAnswers.clear()
        nextButton.isEnabled = true
        prevButton.isEnabled = true
        submitButton.isEnabled = true
        backButton.isVisible = false
        optionsGroup.isEnabled = true
        timeLeftInMillis = 1 * 60 * 1000L
        countDownTimer.cancel()
        startTimer()
        loadQuestion()
    }
}
