package com.phonemics.keyboard.service

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import com.phonemics.keyboard.data.KeyboardLayoutManager
import com.phonemics.keyboard.data.TransformationEngine
import com.phonemics.keyboard.model.KeyboardAction
import com.phonemics.keyboard.model.KeyboardKey
import com.phonemics.keyboard.model.OperationType
import com.phonemics.keyboard.network.ApiClient
import com.phonemics.keyboard.ui.KeyboardView
import kotlinx.coroutines.*

class PhonemicsKeyboardService : InputMethodService(), KeyboardView.OnKeyPressListener {
    
    private lateinit var keyboardView: KeyboardView
    private lateinit var transformationEngine: TransformationEngine
    private lateinit var keyboardLayoutManager: KeyboardLayoutManager
    private lateinit var apiClient: ApiClient
    
    private var currentText = ""
    private var lastOperation = OperationType.NONE
    private var isHamza = false
    private var currentKeyboardVersion = 1
    
    // Debouncing for API calls
    private var apiCallJob: Job? = null
    private val apiDebounceDelay = 250L
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    override fun onCreate() {
        super.onCreate()
        transformationEngine = TransformationEngine()
        keyboardLayoutManager = KeyboardLayoutManager()
        apiClient = ApiClient()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onCreateInputView(): View {
        keyboardView = KeyboardView(this).apply {
            setOnKeyPressListener(this@PhonemicsKeyboardService)
            setKeyboard(keyboardLayoutManager.getKeyboard(currentKeyboardVersion))
        }
        return keyboardView
    }

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        // Reset state when starting new input
        currentText = ""
        lastOperation = OperationType.NONE
        isHamza = false
    }

    override fun onKey(key: KeyboardKey) {
        val inputConnection = currentInputConnection ?: return
        
        when (key.action) {
            KeyboardAction.DELETE -> handleDelete(inputConnection)
            KeyboardAction.SPACE -> handleSpace(inputConnection)
            KeyboardAction.ENTER -> handleEnter(inputConnection)
            KeyboardAction.SWITCH_KEYBOARD -> handleSwitchKeyboard()
            KeyboardAction.DOT -> handleDotTransformation(inputConnection)
            else -> handleCharacterInput(inputConnection, key.arabic)
        }
    }
    
    private fun handleDelete(inputConnection: InputConnection) {
        if (currentText.isNotEmpty()) {
            currentText = currentText.dropLast(1)
            inputConnection.deleteSurroundingText(1, 0)
            lastOperation = OperationType.DELETE
            
            // Apply transformation after delete
            if (shouldApplyTransformation()) {
                applyTransformation(inputConnection)
            }
        }
    }
    
    private fun handleSpace(inputConnection: InputConnection) {
        if (currentText.isNotEmpty()) {
            currentText += " "
            inputConnection.commitText(" ", 1)
            lastOperation = OperationType.INSERT
            
            if (shouldApplyTransformation()) {
                applyTransformation(inputConnection)
            }
            
            // Trigger API call with debouncing
            debounceApiCall()
        }
    }
    
    private fun handleEnter(inputConnection: InputConnection) {
        inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
        inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
    }
    
    private fun handleSwitchKeyboard() {
        currentKeyboardVersion = if (currentKeyboardVersion == 1) 2 else 1
        keyboardView.setKeyboard(keyboardLayoutManager.getKeyboard(currentKeyboardVersion))
    }
    
    private fun handleDotTransformation(inputConnection: InputConnection) {
        if (currentText.isNotEmpty()) {
            val lastChar = currentText.last()
            val transformedChar = transformationEngine.getDotTransformation(lastChar.toString())
            
            if (transformedChar != lastChar.toString()) {
                // Replace last character
                currentText = currentText.dropLast(1) + transformedChar
                inputConnection.deleteSurroundingText(1, 0)
                inputConnection.commitText(transformedChar, 1)
                lastOperation = OperationType.REPLACE
            }
        }
    }
    
    private fun handleCharacterInput(inputConnection: InputConnection, character: String) {
        currentText += character
        inputConnection.commitText(character, 1)
        lastOperation = OperationType.INSERT
        
        if (shouldApplyTransformation()) {
            applyTransformation(inputConnection)
        }
        
        // Trigger API call with debouncing
        debounceApiCall()
    }
    
    private fun shouldApplyTransformation(): Boolean {
        return lastOperation == OperationType.INSERT || 
               lastOperation == OperationType.REPLACE || 
               lastOperation == OperationType.DELETE
    }
    
    private fun applyTransformation(inputConnection: InputConnection) {
        val transformedText = transformationEngine.transform(currentText)
        
        if (transformedText != currentText) {
            // Replace current text with transformed text
            val textLength = currentText.length
            inputConnection.deleteSurroundingText(textLength, 0)
            inputConnection.commitText(transformedText, 1)
            currentText = transformedText
            lastOperation = OperationType.TRANSFORM
        }
    }
    
    private fun debounceApiCall() {
        apiCallJob?.cancel()
        apiCallJob = serviceScope.launch {
            delay(apiDebounceDelay)
            makeApiCall()
        }
    }
    
    private suspend fun makeApiCall() {
        if (currentText.isNotEmpty()) {
            try {
                val response = apiClient.searchSuggestions(
                    text = currentText,
                    keyboardVersion = currentKeyboardVersion,
                    rhythms = emptyList() // Will be populated from previous API responses
                )
                
                response?.let { apiResponse ->
                    // Handle API response
                    if (apiResponse.correctedText != currentText && !apiResponse.correctedText.isNullOrEmpty()) {
                        handleApiCorrection(apiResponse.correctedText)
                    }
                    
                    isHamza = apiResponse.isHamza
                    
                    // Update suggestions in keyboard view
                    keyboardView.updateSuggestions(apiResponse.suggestions ?: emptyList())
                    keyboardView.updateRhythms(apiResponse.rhythms ?: emptyList())
                }
            } catch (e: Exception) {
                // Handle API error silently
            }
        }
    }
    
    private fun handleApiCorrection(correctedText: String) {
        val inputConnection = currentInputConnection ?: return
        
        // Replace current text with API correction
        val textLength = currentText.length
        inputConnection.deleteSurroundingText(textLength, 0)
        inputConnection.commitText(correctedText, 1)
        currentText = correctedText
        lastOperation = OperationType.API
    }
    
    fun onSuggestionSelected(suggestion: String) {
        val inputConnection = currentInputConnection ?: return
        
        // Replace current word with suggestion
        val words = currentText.split(" ").toMutableList()
        if (words.isNotEmpty()) {
            words[words.lastIndex] = suggestion
            val newText = words.joinToString(" ")
            
            val textLength = currentText.length
            inputConnection.deleteSurroundingText(textLength, 0)
            inputConnection.commitText(newText, 1)
            currentText = newText
            lastOperation = OperationType.API
        }
    }
}