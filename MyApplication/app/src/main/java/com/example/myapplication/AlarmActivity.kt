package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stop_alarm)

        val stopButton: Button = findViewById(R.id.btnStopAlarm)

        // Stop the ringtone when the button is pressed
        stopButton.setOnClickListener {
            AlarmReceiver().stopRingtone() // Stop the alarm's ringtone
            finish() // Close the activity
        }
    }
}
