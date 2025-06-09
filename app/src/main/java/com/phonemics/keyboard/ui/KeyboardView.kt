package com.phonemics.keyboard.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.phonemics.keyboard.model.KeyboardAction
import com.phonemics.keyboard.model.KeyboardKey
import com.phonemics.keyboard.service.PhonemicsKeyboardService

class KeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    private var onKeyPressListener: OnKeyPressListener? = null
    private var suggestionsRecyclerView: RecyclerView? = null
    private var rhythmsTextView: TextView? = null
    private var keyboardLayout: LinearLayout? = null
    
    interface OnKeyPressListener {
        fun onKey(key: KeyboardKey)
    }
    
    init {
        orientation = VERTICAL
        setupView()
    }
    
    private fun setupView() {
        // Suggestions row
        suggestionsRecyclerView = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 120)
        }
        addView(suggestionsRecyclerView)
        
        // Rhythms display
        rhythmsTextView = TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            textSize = 14f
            setPadding(16, 8, 16, 8)
        }
        addView(rhythmsTextView)
        
        // Keyboard layout container
        keyboardLayout = LinearLayout(context).apply {
            orientation = VERTICAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        addView(keyboardLayout)
    }
    
    fun setKeyboard(keyboard: List<List<KeyboardKey>>) {
        keyboardLayout?.removeAllViews()
        
        keyboard.forEach { row ->
            val rowLayout = LinearLayout(context).apply {
                orientation = HORIZONTAL
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                weightSum = row.size.toFloat()
            }
            
            row.forEach { key ->
                val button = createKeyButton(key)
                rowLayout.addView(button)
            }
            
            keyboardLayout?.addView(rowLayout)
        }
    }
    
    private fun createKeyButton(key: KeyboardKey): Button {
        return Button(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, 120, 1f).apply {
                setMargins(2, 2, 2, 2)
            }
            
            // Set button text based on action or Arabic character
            text = when (key.action) {
                KeyboardAction.DELETE -> "⌫"
                KeyboardAction.SWITCH_KEYBOARD -> "⌨"
                KeyboardAction.SPACE -> "___"
                KeyboardAction.ENTER -> "↵"
                KeyboardAction.DOT -> "•"
                else -> key.arabic
            }
            
            // Set background color
            background = GradientDrawable().apply {
                setColor(Color.parseColor(key.color))
                cornerRadius = 8f
                setStroke(1, Color.GRAY)
            }
            
            // Set text properties for Arabic
            textSize = 18f
            setTextColor(Color.BLACK)
            
            // Set click listener
            setOnClickListener {
                onKeyPressListener?.onKey(key)
            }
        }
    }
    
    fun updateSuggestions(suggestions: List<String>) {
        suggestionsRecyclerView?.adapter = SuggestionsAdapter(suggestions) { suggestion ->
            (context as? PhonemicsKeyboardService)?.onSuggestionSelected(suggestion)
        }
    }
    
    fun updateRhythms(rhythms: List<String>) {
        rhythmsTextView?.text = rhythms.joinToString(" • ")
    }
    
    fun setOnKeyPressListener(listener: OnKeyPressListener) {
        onKeyPressListener = listener
    }
}

private class SuggestionsAdapter(
    private val suggestions: List<String>,
    private val onSuggestionClick: (String) -> Unit
) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {
    
    class ViewHolder(val button: Button) : RecyclerView.ViewHolder(button)
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val button = Button(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.MATCH_PARENT
            ).apply {
                setMargins(4, 4, 4, 4)
            }
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#E0E0E0"))
                cornerRadius = 6f
            }
            setPadding(16, 8, 16, 8)
        }
        return ViewHolder(button)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.button.text = suggestion
        holder.button.setOnClickListener {
            onSuggestionClick(suggestion)
        }
    }
    
    override fun getItemCount() = suggestions.size
}