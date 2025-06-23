// TaskListActivity.kt
package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class TaskList : AppCompatActivity() {

    private lateinit var taskList: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private val fileName = "tasks.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_list)

        taskList = loadTasks()

        val taskInput = findViewById<EditText>(R.id.taskInput)
        val addButton = findViewById<Button>(R.id.addButton)
        val listView = findViewById<ListView>(R.id.listView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList)
        listView.adapter = adapter

        // Add Task
        addButton.setOnClickListener {
            val task = taskInput.text.toString()
            if (task.isNotEmpty()) {
                taskList.add(task)
                saveTasks()
                adapter.notifyDataSetChanged()
                taskInput.text.clear()
            }
        }

        // Handle item click for update or delete
        listView.setOnItemClickListener { _, _, position, _ ->
            showOptionsDialog(position)
        }

        val imageView15 = findViewById<ImageView>(R.id.imageView15)
        imageView15.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        val imageView14 = findViewById<ImageView>(R.id.imageView14)
        imageView14.setOnClickListener{
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        val imageView13 = findViewById<ImageView>(R.id.imageView13)
        imageView13.setOnClickListener{
            val intent = Intent(this, TaskList::class.java)
            startActivity(intent)
        }

        val imageView12 = findViewById<ImageView>(R.id.imageView12)
        imageView12.setOnClickListener{
            val intent = Intent(this, Alarmset::class.java)
            startActivity(intent)
        }
    }

    // Load tasks from internal storage
    private fun loadTasks(): MutableList<String> {
        val tasks = mutableListOf<String>()
        try {
            val fileInputStream: FileInputStream = openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var line = bufferedReader.readLine()
            while (line != null) {
                tasks.add(line)
                line = bufferedReader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return tasks
    }

    // Save tasks to internal storage
    private fun saveTasks() {
        try {
            val fileOutputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            for (task in taskList) {
                outputStreamWriter.write("$task\n")
            }
            outputStreamWriter.flush()
            outputStreamWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Show options dialog for update or delete task
    private fun showOptionsDialog(position: Int) {
        val options = arrayOf("Update Task", "Delete Task")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showUpdateTaskDialog(position)  // Update Task
                1 -> confirmDeleteTask(position)    // Delete Task
            }
        }
        builder.show()
    }

    // Show a dialog to update the task
    private fun showUpdateTaskDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Task")

        val input = EditText(this)
        input.setText(taskList[position])
        builder.setView(input)

        builder.setPositiveButton("Update") { _, _ ->
            val updatedTask = input.text.toString()
            if (updatedTask.isNotEmpty()) {
                taskList[position] = updatedTask
                saveTasks()
                adapter.notifyDataSetChanged()
            }
        }
        builder.setNegativeButton("Cancel", null)

        builder.show()
    }

    // Confirm and delete task
    private fun confirmDeleteTask(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Task")
        builder.setMessage("Are you sure you want to delete this task?")

        builder.setPositiveButton("Delete") { _, _ ->
            taskList.removeAt(position)
            saveTasks()
            adapter.notifyDataSetChanged()
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
