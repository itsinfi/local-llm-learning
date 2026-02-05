package de.raum7.local_llm_learning.llm

import android.content.Context
import java.io.File

/**
 * Verantwortlich für das Bereitstellen von LLM Modellen aus den App Assets.
 * Kopiert das Modell bei Bedarf ins interne Dateisystem.
 */
object ModelInstaller {

    /**
     * Stellt sicher, dass ein Modell im internen Speicher existiert.
     *
     * Falls die Datei noch nicht vorhanden ist, wird sie aus den Assets kopiert.
     *
     * @param context Application Context
     * @param assetPath Pfad zur Modell Datei in den Assets
     * @param targetFileName Ziel Dateiname im internen Speicher
     * @return File Objekt der installierten Modell Datei
     */
    fun ensureModelInstalled(
        context: Context,
        assetPath: String,
        targetFileName: String
    ): File {

        // Verzeichnis für Modelle im internen Speicher
        val modelsDir = File(context.filesDir, "models")

        // Verzeichnis anlegen, falls es noch nicht existiert
        if (!modelsDir.exists()) {
            modelsDir.mkdirs()
        }

        // Ziel Datei
        val targetFile = File(modelsDir, targetFileName)

        // Wenn die Datei bereits existiert und nicht leer ist, direkt zurückgeben
        if (targetFile.exists() && targetFile.length() > 0L) {
            return targetFile
        }

        // Modell aus den Assets kopieren
        context.assets.open(assetPath).use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return targetFile
    }
}
