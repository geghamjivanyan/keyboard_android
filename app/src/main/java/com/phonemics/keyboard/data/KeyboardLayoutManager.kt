package com.phonemics.keyboard.data

import com.phonemics.keyboard.model.KeyboardAction
import com.phonemics.keyboard.model.KeyboardKey

class KeyboardLayoutManager {
    
    // Color constants
    private val WHITE = "#FAFAFA"
    private val YELLOW = "#E6E6B4"
    private val RED = "#F0C8C8"
    private val PURPLE = "#d397d3"
    private val GREEN = "#C8F0C8"
    private val BLUE = "#C8D2FA"
    
    // Keyboard Layout 1 - Diacritics version
    private val keyboard1 = listOf(
        listOf(
            KeyboardKey(1, WHITE, "a", "", KeyboardAction.DELETE),
            KeyboardKey(2, BLUE, "b", "", KeyboardAction.SWITCH_KEYBOARD),
            KeyboardKey(3, YELLOW, "c", "\u0644"), // ل
            KeyboardKey(4, RED, "d", "\u064E"), // فتحة
            KeyboardKey(5, RED, "e", "\u064F"), // ضمة
            KeyboardKey(6, RED, "f", "\u0650")  // كسرة
        ),
        listOf(
            KeyboardKey(7, PURPLE, "g", "\u0641"), // ف
            KeyboardKey(8, YELLOW, "h", "\u0646"), // ن
            KeyboardKey(9, YELLOW, "i", "\u0631"), // ر
            KeyboardKey(10, YELLOW, "j", "\u0633"), // س
            KeyboardKey(11, BLUE, "k", "\u0621"), // ء
            KeyboardKey(12, BLUE, "l", ".", KeyboardAction.DOT)
        ),
        listOf(
            KeyboardKey(13, PURPLE, "m", "\u0645"), // م
            KeyboardKey(14, PURPLE, "n", "\u066E"), // ٮ
            KeyboardKey(15, PURPLE, "o", "\u062F"), // د
            KeyboardKey(16, YELLOW, "p", "\u0635"), // ص
            KeyboardKey(17, YELLOW, "q", "\u062D"), // ح
            KeyboardKey(18, YELLOW, "r", "\u0647")  // ه
        ),
        listOf(
            KeyboardKey(19, WHITE, "s", "ـ", KeyboardAction.SPACE),
            KeyboardKey(20, PURPLE, "t", "\u0637"), // ط
            KeyboardKey(21, GREEN, "u", "\u0643"), // ك
            KeyboardKey(22, GREEN, "v", "\u0642"), // ق
            KeyboardKey(23, YELLOW, "w", "\u0639"), // ع
            KeyboardKey(24, WHITE, "x", "", KeyboardAction.ENTER)
        )
    )
    
    // Keyboard Layout 2 - Full vowels version
    private val keyboard2 = listOf(
        listOf(
            KeyboardKey(1, WHITE, "a", "", KeyboardAction.DELETE),
            KeyboardKey(2, BLUE, "b", "", KeyboardAction.SWITCH_KEYBOARD),
            KeyboardKey(3, YELLOW, "c", "\u0644"), // ل
            KeyboardKey(4, RED, "d", "\u0627"), // ا
            KeyboardKey(5, RED, "e", "\u0648"), // و
            KeyboardKey(6, RED, "f", "\u064a")  // ي
        ),
        listOf(
            KeyboardKey(7, PURPLE, "g", "\u0641"), // ف
            KeyboardKey(8, YELLOW, "h", "\u0646"), // ن
            KeyboardKey(9, YELLOW, "i", "\u0631"), // ر
            KeyboardKey(10, YELLOW, "j", "\u0633"), // س
            KeyboardKey(11, BLUE, "k", "\u0621"), // ء
            KeyboardKey(12, BLUE, "l", "", KeyboardAction.DOT)
        ),
        listOf(
            KeyboardKey(13, PURPLE, "m", "\u0645"), // م
            KeyboardKey(14, PURPLE, "n", "\u066E"), // ٮ
            KeyboardKey(15, PURPLE, "o", "\u062F"), // د
            KeyboardKey(16, YELLOW, "p", "\u0635"), // ص
            KeyboardKey(17, YELLOW, "q", "\u062D"), // ح
            KeyboardKey(18, YELLOW, "r", "\u0647")  // ه
        ),
        listOf(
            KeyboardKey(19, WHITE, "s", "ـ", KeyboardAction.SPACE),
            KeyboardKey(20, PURPLE, "t", "\u0637"), // ط
            KeyboardKey(21, GREEN, "u", "\u0643"), // ك
            KeyboardKey(22, GREEN, "v", "\u0642"), // ق
            KeyboardKey(23, YELLOW, "w", "\u0639"), // ع
            KeyboardKey(24, WHITE, "x", "", KeyboardAction.ENTER)
        )
    )
    
    fun getKeyboard(version: Int): List<List<KeyboardKey>> {
        return if (version == 1) keyboard1 else keyboard2
    }
}