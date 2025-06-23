package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.app.TimePickerDialog
import android.widget.Button
import android.widget.ImageView
import java.util.*
import android.widget.Toast

class Alarmset : AppCompatActivity() {

    private lateinit var tvAlarmStatus: TextView
    private lateinit var alarmManager: AlarmManager
    private val ALARM_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        tvAlarmStatus = findViewById(R.id.tvAlarmStatus)
        val fabSetAlarm: FloatingActionButton = findViewById(R.id.fabSetAlarm)
        val btnDeleteAlarm: Button = findViewById(R.id.btnDeleteAlarm)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Load saved alarm from SharedPreferences
        loadSavedAlarm()

        // On clock icon click, open the TimePicker to set an alarm
        fabSetAlarm.setOnClickListener {
            openTimePickerDialog()
        }

        // Delete alarm on delete button click
        btnDeleteAlarm.setOnClickListener {
            deleteAlarm()
        }

        val imageView11 = findViewById<ImageView>(R.id.imageView11)
        imageView11.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        val imageView6 = findViewById<ImageView>(R.id.imageView6)
        imageView6.setOnClickListener{
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        val imageView7 = findViewById<ImageView>(R.id.imageView7)
        imageView7.setOnClickListener{
            val intent = Intent(this, TaskList::class.java)
            startActivity(intent)
        }

        val imageView4 = findViewById<ImageView>(R.id.imageView4)
        imageView4.setOnClickListener{
            val intent = Intent(this, Alarmset::class.java)
            startActivity(intent)
        }
    }

    private fun openTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // TimePickerDialog to allow the user to pick time
        TimePickerDialog(this, { _, hourOfDay, minute ->
            // Set the selected time
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            // Set the alarm
            setAlarm(calendar.timeInMillis)

            // Save alarm to SharedPreferences
            saveAlarmToPreferences(hourOfDay, minute)

            // Update UI
            updateAlarmStatus(hourOfDay, minute)

            Toast.makeText(this, "Alarm set for ${String.format("%02d:%02d", hourOfDay, minute)}", Toast.LENGTH_SHORT).show()

        }, currentHour, currentMinute, false).show()
    }

    private fun setAlarm(timeInMillis: Long) {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Set an exact alarm to wake the device when timeInMillis is reached
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

    private fun saveAlarmToPreferences(hour: Int, minute: Int) {
        val sharedPref = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("alarm_hour", hour)
            putInt("alarm_minute", minute)
            apply()
        }
    }

    private fun loadSavedAlarm() {
        val sharedPref = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        val savedHour = sharedPref.getInt("alarm_hour", -1)
        val savedMinute = sharedPref.getInt("alarm_minute", -1)

        if (savedHour != -1 && savedMinute != -1) {
            // If there is a saved alarm, update the status
            updateAlarmStatus(savedHour, savedMinute)
        } else {
            tvAlarmStatus.text = "No alarm set"
        }
    }

    private fun updateAlarmStatus(hour: Int, minute: Int) {
        val formattedTime = String.format("%02d:%02d", hour, minute)
        tvAlarmStatus.text = "Alarm set for $formattedTime"
    }

    // Delete the alarm
    private fun deleteAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Cancel the alarm
        alarmManager.cancel(pendingIntent)

        // Remove the alarm data from SharedPreferences
        val sharedPref = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("alarm_hour")
            remove("alarm_minute")
            apply()
        }

        // Update UI to reflect no alarm set
        tvAlarmStatus.text = "No alarm set"
        Toast.makeText(this, "Alarm deleted", Toast.LENGTH_SHORT).show()
    }
}
