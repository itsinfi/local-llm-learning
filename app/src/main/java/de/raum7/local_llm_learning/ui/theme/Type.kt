package de.raum7.local_llm_learning.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import de.raum7.local_llm_learning.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("VT323"),
        fontProvider = provider,
    )
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Space Mono"),
        fontProvider = provider,
    )
)

// helper functions for scaling all font sizes
private const val TYPOGRAPHY_SCALING_FACTOR: Float = 1.15f

private fun TextStyle.scale(factor: Float): TextStyle {
    return this.copy(
        fontSize = fontSize * factor,
        lineHeight = lineHeight * factor,
        letterSpacing = letterSpacing * factor,
    )
}

private fun Typography.scale(factor: Float): Typography {
    return Typography(
        displayLarge = displayLarge.scale(factor),
        displayMedium = displayMedium.scale(factor),
        displaySmall = displaySmall.scale(factor),
        headlineLarge = headlineLarge.scale(factor),
        headlineMedium = headlineMedium.scale(factor),
        headlineSmall = headlineSmall.scale(factor),
        titleLarge = titleLarge.scale(factor),
        titleMedium = titleMedium.scale(factor),
        titleSmall = titleSmall.scale(factor),
        bodyLarge = bodyLarge.scale(factor),
        bodyMedium = bodyMedium.scale(factor),
        bodySmall = bodySmall.scale(factor),
        labelLarge = labelLarge.scale(factor),
        labelMedium = labelMedium.scale(factor),
        labelSmall = labelSmall.scale(factor),
    )
}

// Default Material 3 typography values
private val baseline = Typography()

private val baseTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)

// apply typography scaling
val scaledTypography = baseTypography.scale(TYPOGRAPHY_SCALING_FACTOR)