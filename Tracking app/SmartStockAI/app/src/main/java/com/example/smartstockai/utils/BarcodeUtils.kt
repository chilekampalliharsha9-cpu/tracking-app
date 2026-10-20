package com.example.smartstockai.utils

object BarcodeUtils {
    fun normalizeBarcode(raw: String): String {
        return raw.trim().replace("\\s+", "")
    }

    fun isSupportedBarcode(value: String): Boolean {
        val normalized = normalizeBarcode(value)
        if (normalized.isBlank()) return false
        if (normalized.length < 8 || normalized.length > 20) return false
        return normalized.all { it.isDigit() || it.isLetter() }
    }
}
