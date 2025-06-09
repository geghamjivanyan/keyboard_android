package com.phonemics.keyboard.model

data class KeyboardKey(
    val id: Int,
    val color: String,
    val english: String,
    val arabic: String,
    val action: KeyboardAction? = null
)

enum class KeyboardAction {
    DELETE,
    SWITCH_KEYBOARD,
    SPACE,
    ENTER,
    DOT
}

enum class OperationType {
    NONE,
    INSERT,
    DELETE,
    REPLACE,
    TRANSFORM,
    API
}

data class ApiResponse(
    val suggestions: List<String>? = null,
    val rhythms: List<String>? = null,
    val correctedText: String? = null,
    val isHamza: Boolean = false
)

data class SearchRequest(
    val text: String,
    val rhythms: List<String> = emptyList(),
    val keyboardVersion: Int = 1,
    val keyboardChange: Boolean = false
)