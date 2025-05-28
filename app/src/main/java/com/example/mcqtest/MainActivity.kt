package com.example.mcqtest

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val questions = listOf(
        Question("1. What creates a magnetic field?", listOf("Stationary charges", "Heat energy", "Moving electric charges", "Light waves"), 2),
        Question("2. What is the unit of magnetic field strength?", listOf( "Volt (V)", "Newton (N)", "Tesla (T)", "Ohm (Ω)"), 2),
        Question("3. Which law states that a changing magnetic field induces an electric field?", listOf("Gauss's Law", "Ampère’s Law", "Faraday’s Law", "Coulomb’s Law"), 2),
        Question("4. The direction of an electromagnetic wave is perpendicular to:", listOf("The electric field only", "The magnetic field only", "Both electric and magnetic fields", "The electric field but parallel to the magnetic field"), 2),
        Question("5. What is the speed of electromagnetic waves in a vacuum?", listOf("3 × 10⁶ m/s", "3 × 10⁸ m/s", "1.5 × 10⁸ m/s", "3 × 10⁵ m/s"), 1),
        Question("6. Which of the following is NOT an electromagnetic wave?", listOf( "Radio wave", "Sound wave", "X-ray", "Light wave"), 1),
        Question("7. Which equation describes how electric field lines originate and terminate?", listOf("Faraday’s Law", "Gauss’s Law for electricity", "Gauss’s Law for magnetism", "Ampère’s Law"), 1),
        Question("8. What does Maxwell’s correction to Ampère’s Law include?", listOf("Magnetic monopoles", "Displacement current", "Coulomb force", "Resistance"), 1),
        Question("9. Electromagnetic waves are classified as:", listOf("Longitudinal waves", "Transverse waves", "Mechanical waves", "Standing waves"), 1),
        Question("10. What happens when an electric field changes with time?", listOf("It produces static electricity", "It generates a magnetic field", "It stops the magnetic field", "Nothing happens"), 1)
    )

    private var currentQuestionIndex = 0
    private var score = 0

    private lateinit var questionTextView: TextView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var scoreTextView: TextView
    private lateinit var backbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.questionTextView)
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup)
        submitButton = findViewById(R.id.submitButton)
        scoreTextView = findViewById(R.id.scoreTextView)
        backbutton = findViewById(R.id.backButton)

        loadQuestion()

        submitButton.setOnClickListener {
            val selectedOptionId = optionsRadioGroup.checkedRadioButtonId

            if (selectedOptionId != -1) {
                val selectedIndex = optionsRadioGroup.indexOfChild(findViewById(selectedOptionId))

                if (selectedIndex == questions[currentQuestionIndex].correctAnswerIndex) {
                    score++
                }

                currentQuestionIndex++

                if (currentQuestionIndex < questions.size) {
                    loadQuestion()
                } else {
                    showFinalScore()
                }
            } else {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
            }
        }
        backbutton.setOnClickListener {
            currentQuestionIndex = 0
            score = 0
            submitButton.isEnabled = true
            scoreTextView.text = ""
            loadQuestion()
        }

    }

    private fun loadQuestion() {
        val question = questions[currentQuestionIndex]
        questionTextView.text = question.questionText
        optionsRadioGroup.removeAllViews()
        backbutton.isEnabled = false

        for (option in question.options) {
            val radioButton = RadioButton(this)
            radioButton.text = option
            optionsRadioGroup.addView(radioButton)
        }
    }

    private fun showFinalScore() {
        questionTextView.text = "Test Completed!"
        optionsRadioGroup.removeAllViews()
        submitButton.isEnabled = false
        scoreTextView.text = "Your score: $score/${questions.size}"
        backbutton.isEnabled = true
    }
}
