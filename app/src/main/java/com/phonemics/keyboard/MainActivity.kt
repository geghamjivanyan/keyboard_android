package com.phonemics.keyboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setupUI()
    }
    
    private fun setupUI() {
        val titleText = findViewById<TextView>(R.id.titleText)
        val enableButton = findViewById<Button>(R.id.enableButton)
        val selectButton = findViewById<Button>(R.id.selectButton)
        val testButton = findViewById<Button>(R.id.testButton)
        
        titleText.text = "Phonemics Arabic Keyboard"
        
        enableButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        }
        
        selectButton.setOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showInputMethodPicker()
        }
        
        testButton.setOnClickListener {
            // Open a simple test activity with an EditText
            startActivity(Intent(this, TestActivity::class.java))
        }
    }
}