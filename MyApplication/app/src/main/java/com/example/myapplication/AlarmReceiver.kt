package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        // Static Ringtone object to allow stopping it from other components
        var ringtone: Ringtone? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        // Get the default alarm ringtone or use notification if no alarm sound is available
        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Initialize the ringtone object
        if (context != null && ringtone == null) {
            ringtone = RingtoneManager.getRingtone(context, alarmUri)
        }

        // Play the ringtone if not already playing
        ringtone?.let {
            if (!it.isPlaying) {
                it.play()
            }
        }

        // Optionally, start a fullscreen activity (e.g., AlarmActivity) when the alarm goes off
        val alarmIntent = Intent(context, AlarmActivity::class.java)
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context?.startActivity(alarmIntent)

        // Provide feedback for testing or debugging purposes
        Toast.makeText(context, "Alarm ringing...", Toast.LENGTH_SHORT).show()
    }

    // Static method to stop the ringtone from other components
    fun stopRingtone() {
        ringtone?.let {
            if (it.isPlaying) {
                it.stop()
            }
        }
    }
}
