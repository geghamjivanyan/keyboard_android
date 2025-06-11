package com.gegham.phoneimckeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.EditorInfo
import com.gegham.phoneimckeyboard.databinding.KeyboardViewBinding

class PhonemicKeyboardIME : InputMethodService() {
    private lateinit var keyboardView: PhonemicKeyboardView
    private lateinit var layoutManager: KeyboardLayoutManager
    private var _binding: KeyboardViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateInputView(): View {
        val root = layoutInflater.inflate(R.layout.keyboard_view, null)
        _binding = KeyboardViewBinding.bind(root)
        keyboardView = binding.keyboardView
        layoutManager = KeyboardLayoutManager(this)
        
        // Initialize keyboard layout
        layoutManager.initializeKeyboard(keyboardView)
        
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onFinishInput() {
        super.onFinishInput()
        // Clean up any resources
    }

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        // Handle input start
    }
} 