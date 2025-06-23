package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TimerActivity : AppCompatActivity() {

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var progressBar: ProgressBar
    private lateinit var timerTextView: TextView
    private lateinit var minutesInput: EditText
    private lateinit var secondsInput: EditText
    private var isTimerRunning = false
    private var timeLeftInMillis: Long = 900000 // Default: 15 minutes in milliseconds
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        progressBar = findViewById(R.id.progressBar)
        timerTextView = findViewById(R.id.timerTextView)
        minutesInput = findViewById(R.id.minutesInput)
        secondsInput = findViewById(R.id.secondsInput)
        val pauseButton = findViewById<Button>(R.id.pauseButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        loadTimerState()  // Load saved timer state

        if (isTimerRunning) {
            updateTimerUI()
        }

        pauseButton.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
            } else {
                startTimerFromInput() // Start using user input
            }
        }

        cancelButton.setOnClickListener {
            cancelTimer()
        }

        val imageView19 = findViewById<ImageView>(R.id.imageView19)
        imageView19.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        val imageView18 = findViewById<ImageView>(R.id.imageView18)
        imageView18.setOnClickListener{
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        val imageView17 = findViewById<ImageView>(R.id.imageView17)
        imageView17.setOnClickListener{
            val intent = Intent(this, TaskList::class.java)
            startActivity(intent)
        }

        val imageView16 = findViewById<ImageView>(R.id.imageView16)
        imageView16.setOnClickListener{
            val intent = Intent(this, Alarmset::class.java)
            startActivity(intent)
        }
    }

    // Start the timer based on user input
    private fun startTimerFromInput() {
        val minutes = minutesInput.text.toString().toIntOrNull() ?: 0
        val seconds = secondsInput.text.toString().toIntOrNull() ?: 0

        // Convert the input into milliseconds
        timeLeftInMillis = (minutes * 60 + seconds) * 1000L

        if (timeLeftInMillis <= 0) {
            Toast.makeText(this, "Please enter a valid time!", Toast.LENGTH_SHORT).show()
            return
        }

        endTime = System.currentTimeMillis() + timeLeftInMillis
        startTimer()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerUI()
            }

            override fun onFinish() {
                isTimerRunning = false
                progressBar.progress = 100
            }
        }.start()

        isTimerRunning = true
        saveTimerState()
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        saveTimerState()
    }

    private fun cancelTimer() {
        countDownTimer.cancel()
        timeLeftInMillis = 0
        updateTimerUI()
        isTimerRunning = false
        saveTimerState()
    }

    private fun updateTimerUI() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = timeFormatted

        val progress = (timeLeftInMillis.toFloat() / (minutes * 60 + seconds) * 100).toInt()
        progressBar.progress = progress
    }

    private fun saveTimerState() {
        val prefs = getSharedPreferences("timerPrefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong("timeLeftInMillis", timeLeftInMillis)
        editor.putBoolean("isTimerRunning", isTimerRunning)
        editor.putLong("endTime", endTime)
        editor.apply()
    }

    private fun loadTimerState() {
        val prefs = getSharedPreferences("timerPrefs", Context.MODE_PRIVATE)
        timeLeftInMillis = prefs.getLong("timeLeftInMillis", 900000)
        isTimerRunning = prefs.getBoolean("isTimerRunning", false)
        endTime = prefs.getLong("endTime", 0)

        if (isTimerRunning) {
            timeLeftInMillis = endTime - System.currentTimeMillis()
            if (timeLeftInMillis < 0) {
                timeLeftInMillis = 0
                isTimerRunning = false
            }
        }
    }
}
