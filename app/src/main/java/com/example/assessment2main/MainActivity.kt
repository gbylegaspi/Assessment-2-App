package com.example.assessment2main

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var editTextDailySavings: TextInputEditText
    private lateinit var editTextGoal: TextInputEditText
    private lateinit var textViewResults: TextView
    private lateinit var textViewDate: TextView
    private lateinit var spinnerCurrency: Spinner
    private var calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Assessment2MAIN)  // Revert to the app's main theme right away
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()
        showWelcomeDialog()
    }

    private fun initViews() {
        editTextDailySavings = findViewById(R.id.editTextDailySavings)
        editTextGoal = findViewById(R.id.editTextGoal)
        textViewResults = findViewById(R.id.textViewResults)
        textViewDate = findViewById(R.id.textViewDate)
        spinnerCurrency = findViewById(R.id.spinnerCurrency)

        ArrayAdapter.createFromResource(
            this,
            R.array.currency_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCurrency.adapter = adapter
        }
    }

    private fun setupListeners() {
        textViewDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                textViewDate.text = "Date: ${dayOfMonth}/${month + 1}/${year}"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        findViewById<Button>(R.id.buttonCalculate).setOnClickListener {
            calculateDaysToGoal()
        }
    }

    private fun calculateDaysToGoal() {
        val dailySavings = editTextDailySavings.text.toString().toDoubleOrNull() ?: 0.0
        val goalAmount = editTextGoal.text.toString().toDoubleOrNull() ?: 0.0
        val selectedCurrency = spinnerCurrency.selectedItem.toString()

        if (dailySavings > 0) {
            val days = (goalAmount / dailySavings).toInt()
            val goalDate = Calendar.getInstance().apply {
                time = calendar.time
                add(Calendar.DAY_OF_YEAR, days)
            }
            val goalDateStr = "${goalDate.get(Calendar.DAY_OF_MONTH)}/${goalDate.get(Calendar.MONTH) + 1}/${goalDate.get(Calendar.YEAR)}"
            textViewResults.text = "Goal will be reached in $days days, on $goalDateStr, in $selectedCurrency."
        } else {
            textViewResults.text = "Please enter a valid daily savings amount."
        }
    }

    private fun showWelcomeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Welcome to the Savings Calculator")
        builder.setMessage("Set your daily savings and goal, and find out when you will meet your savings target!")

        builder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
