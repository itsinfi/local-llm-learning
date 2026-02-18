package de.raum7.local_llm_learning.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.zip.ZipInputStream
import kotlin.math.min

class DocumentTextExtractor(private val context: Context) {

    init {
        try {
            PDFBoxResourceLoader.init(context)
        } catch (_: Throwable) {
        }
    }

    fun extractText(
        uri: Uri?,
        maxChars: Int = 12000,
        maxPdfPages: Int = 5
    ): String {
        if (uri == null) return ""

        val cr = context.contentResolver
        val mime = cr.getType(uri).orEmpty()

        return when {
            mime == "application/pdf" -> readPdf(uri, maxChars, maxPdfPages)
            mime == "text/plain" -> readTxt(uri, maxChars)
            mime == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> readDocx(uri, maxChars)
            else -> {
                val name = queryDisplayName(uri).lowercase()
                when {
                    name.endsWith(".pdf") -> readPdf(uri, maxChars, maxPdfPages)
                    name.endsWith(".txt") -> readTxt(uri, maxChars)
                    name.endsWith(".docx") -> readDocx(uri, maxChars)
                    else -> ""
                }
            }
        }
    }

    private fun readTxt(uri: Uri, maxChars: Int): String {
        val cr = context.contentResolver
        cr.openInputStream(uri).use { input ->
            if (input == null) return ""
            val sb = StringBuilder()
            BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8)).useLines { lines ->
                for (line in lines) {
                    if (sb.length >= maxChars) break
                    sb.append(line).append('\n')
                }
            }
            return sb.toString().trim().take(maxChars)
        }
    }

    private fun readPdf(uri: Uri, maxChars: Int, maxPages: Int): String {
        val cr = context.contentResolver
        cr.openInputStream(uri).use { input ->
            if (input == null) return ""
            PDDocument.load(input).use { doc ->
                val stripper = PDFTextStripper()
                stripper.sortByPosition = true
                stripper.startPage = 1
                stripper.endPage = min(maxPages.coerceAtLeast(1), doc.numberOfPages)
                val text = stripper.getText(doc).orEmpty().trim()
                return text.take(maxChars)
            }
        }
    }

    private fun readDocx(uri: Uri, maxChars: Int): String {
        val cr = context.contentResolver
        cr.openInputStream(uri).use { input ->
            if (input == null) return ""
            val xml = extractDocxDocumentXml(input) ?: return ""
            val text = docxXmlToText(xml)
            return text.trim().take(maxChars)
        }
    }

    private fun extractDocxDocumentXml(input: InputStream): String? {
        ZipInputStream(input).use { zip ->
            while (true) {
                val entry = zip.nextEntry ?: break
                if (!entry.isDirectory && entry.name == "word/document.xml") {
                    val bytes = zip.readBytes()
                    return String(bytes, StandardCharsets.UTF_8)
                }
                zip.closeEntry()
            }
        }
        return null
    }

    private fun docxXmlToText(xml: String): String {
        val paragraphs = xml.split("<w:p")
        val out = StringBuilder()
        val textRegex = Regex("<w:t[^>]*>(.*?)</w:t>", setOf(RegexOption.DOT_MATCHES_ALL))

        for (p in paragraphs) {
            val parts = textRegex.findAll(p).map { it.groupValues[1] }.toList()
            if (parts.isNotEmpty()) {
                if (out.isNotEmpty()) out.append('\n')
                out.append(parts.joinToString(""))
            }
            if (out.length >= 200000) break
        }

        return out.toString()
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
    }

    private fun queryDisplayName(uri: Uri): String {
        val cr = context.contentResolver
        val cursor = cr.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val idx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx >= 0) return it.getString(idx).orEmpty()
            }
        }
        return ""
    }
}