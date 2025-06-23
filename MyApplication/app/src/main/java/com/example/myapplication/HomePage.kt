// HomeActivity.kt
package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)

        val taskListButton = findViewById<Button>(R.id.taskListButton)

        taskListButton.setOnClickListener {
            val intent = Intent(this, TaskList::class.java)
            startActivity(intent)
        }

        val alarmButton = findViewById<Button>(R.id.alarmButton)
        alarmButton.setOnClickListener {
            val intent = Intent(this, Alarmset::class.java)
            startActivity(intent)
        }

        val timerButton = findViewById<Button>(R.id.timerButton)
        timerButton.setOnClickListener{
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        val imageView9 = findViewById<ImageView>(R.id.imageView9)
        imageView9.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        val imageView10 = findViewById<ImageView>(R.id.imageView10)
        imageView10.setOnClickListener{
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        val imageView5 = findViewById<ImageView>(R.id.imageView5)
        imageView5.setOnClickListener{
            val intent = Intent(this, TaskList::class.java)
            startActivity(intent)
        }

        val imageView8 =findViewById<ImageView>(R.id.imageView8)
        imageView8.setOnClickListener{
            val intent = Intent(this, Alarmset::class.java)
            startActivity(intent)
        }
    }
}
