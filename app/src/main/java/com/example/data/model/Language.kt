package com.example.data.model

enum class Language(val code: String, val flag: String, val displayName: String, val subText: String) {
    TH("TH", "🇹🇭", "ภาษาไทย", "Thai Language"),
    EN("EN", "🇬🇧", "English", "English Language"),
    ZH("ZH", "🇨🇳", "简体中文", "Chinese (Mandarin)"),
    KO("KO", "🇰🇷", "한국어", "Korean Language");

    companion object {
        fun fromCode(code: String): Language {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: TH
        }
    }
}
