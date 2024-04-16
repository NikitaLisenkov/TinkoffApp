package com.example.app.utils

object EmojiUtils {

    fun unicodeSequenceToString(unicodeSequence: String): String {
        return unicodeSequence.split('-')
            .map { it.toInt(16) }
            .flatMap { Character.toChars(it).toList() }
            .joinToString("")
    }

    fun stringToUnicodeSequence(text: String): String {
        val result = StringBuilder()
        var i = 0
        while (i < text.length) {
            val codePoint = text.codePointAt(i)
            if (i > 0) result.append('-')
            result.append(Integer.toHexString(codePoint))
            i += Character.charCount(codePoint)
        }
        return result.toString()
    }
}