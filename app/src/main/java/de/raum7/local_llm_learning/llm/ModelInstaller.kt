package de.raum7.local_llm_learning.llm

import android.content.Context
import java.io.File

object ModelInstaller {

    fun ensureModelInstalled(
        context: Context,
        assetPath: String,
        targetFileName: String
    ): File {

        val modelsDir = File(context.filesDir, "models")
        if (!modelsDir.exists()) modelsDir.mkdirs()

        val targetFile = File(modelsDir, targetFileName)

        val expectedSize = context.assets.openFd(assetPath).length

        if (targetFile.exists() && targetFile.length() == expectedSize) {
            return targetFile
        }

        if (targetFile.exists()) {
            targetFile.delete()
        }

        context.assets.open(assetPath).use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output, bufferSize = 1024 * 1024)
            }
        }

        return targetFile
    }
}
