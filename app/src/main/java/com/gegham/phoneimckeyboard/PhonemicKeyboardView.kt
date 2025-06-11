package com.gegham.phoneimckeyboard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.gegham.phoneimckeyboard.databinding.KeyboardViewBinding

class PhonemicKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var _binding: KeyboardViewBinding? = null
    private val binding get() = _binding!!
    private var keyClickListener: OnKeyClickListener? = null

    init {
        orientation = VERTICAL
        _binding = KeyboardViewBinding.bind(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    fun setOnKeyClickListener(listener: OnKeyClickListener) {
        keyClickListener = listener
    }

    interface OnKeyClickListener {
        fun onKeyClick(key: String)
    }
} 