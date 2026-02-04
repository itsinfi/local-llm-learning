package de.raum7.local_llm_learning.ui.shared.utils

import java.util.Locale

fun formatDoubleString(value: Double, unit: String = "", format: String = "%.2f"): String {
    val formattedString: String = String.format(
        locale = Locale.GERMANY, // TODO: add dynamic locales,
        format,
        value,
    )

    return "$formattedString$unit"
}