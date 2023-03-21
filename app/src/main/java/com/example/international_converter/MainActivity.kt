package com.example.international_converter

import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputEditText: EditText = findViewById(R.id.inputEditText)
        val fromSpinner: Spinner = findViewById(R.id.fromSpinner)
        val toSpinner: Spinner = findViewById(R.id.toSpinner)
        val convertButton: Button = findViewById(R.id.convertButton)
        val resultTextView: TextView = findViewById(R.id.resultTextView)

        ArrayAdapter.createFromResource(
            this,
            R.array.length_units,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fromSpinner.adapter = adapter
            toSpinner.adapter = adapter
        }

        convertButton.setOnClickListener {
            val inputValue = inputEditText.text.toString().toDoubleOrNull() ?: 0.0
            val fromUnit = fromSpinner.selectedItem.toString()
            val toUnit = toSpinner.selectedItem.toString()
            val convertedValue = convertLength(inputValue, fromUnit, toUnit)
            resultTextView.text = getString(R.string.result_text, convertedValue, toUnit)
        }

        val langButton: Button = findViewById(R.id.langButton)
        langButton.setOnClickListener {
            val currentLanguage = resources.configuration.locale.language
            val newLanguage = if (currentLanguage == "en") "ru" else "en"
            val locale = Locale(newLanguage)
            Locale.setDefault(locale)
            val configuration = resources.configuration
            configuration.setLocale(locale)
            val context = createConfigurationContext(configuration)
            resources.updateConfiguration(configuration, resources.displayMetrics)
            recreate()
        }

    }

    private fun convertLength(value: Double, fromUnit: String, toUnit: String): Double {
        // Определить коэффициент преобразования для каждой единицы измерения
        val coefficients = mapOf(
            "километр" to 1000.0,
            "метр" to 1.0,
            "сантиметр" to 0.01,
            "миля" to 1609.344,
            "фут" to 0.3048,
            "дюйм" to 0.0254
        )

        // Преобразовать значение в метры
        val meters = value * coefficients[fromUnit]!!

        // Преобразовать метры в целевую единицу измерения
        return meters / coefficients[toUnit]!!
    }
}