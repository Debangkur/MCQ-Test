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
        Question("What creates a magnetic field?", listOf("Stationary charges", "Heat energy", "Moving electric charges", "Light waves"), 2),
        Question("Which law states that a changing magnetic field induces an electric field?", listOf("Gauss's Law", "Ampère’s Law", "Faraday’s Law", "Coulomb’s Law"), 3),
        Question("What is the unit of magnetic field strength?", listOf("Volt (V)", "Newton (N)", "Tesla (T)", "Ohm (Ω"), 3),
        Question("The direction of an electromagnetic wave is perpendicular to:", listOf("The electric field only", "The magnetic field only", "Both electric and magnetic fields", "The electric field but parallel to the magnetic field"), 2),
        Question("What is the speed of electromagnetic waves in a vacuum?", listOf("3 × 10⁶ m/s", "3 × 10⁸ m/s", "1.5 × 10⁸ m/s", "3 × 10⁵ m/s"), 1),
        Question(" Which of the following is NOT an electromagnetic wave?", listOf("Radio wave", "Sound wave", "X-ray", "Light wave"), 1),
        Question("Which equation describes how electric field lines originate and terminate?", listOf("Faraday’s Law", "Gauss’s Law for electricity", "Gauss’s Law for magnetism", "Ampère’s Law"), 1),
        Question("What does Maxwell’s correction to Ampère’s Law include?", listOf("Magnetic monopoles", "Displacement current", "Coulomb force", "Resistance"), 1),
        Question("Electromagnetic waves are classified as:", listOf("Longitudinal waves", "Transverse waves", "Mechanical waves", "Standing waves"), 1),
        Question("What happens when an electric field changes with time?", listOf("It produces static electricity", "It generates a magnetic field", "It stops the magnetic field", "Nothing happens"), 1),
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
