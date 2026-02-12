package de.raum7.local_llm_learning.ui.shared.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import de.raum7.local_llm_learning.R
import de.raum7.local_llm_learning.ui.theme.AppTheme

private val DEFAULT_MIME_TYPES = mapOf(
    ".pdf" to "application/pdf",
    ".docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    ".txt" to "text/plain",
)

private fun askForPermission(context: Context, uri: Uri) {
    context.contentResolver.takePersistableUriPermission(
        uri,
        Intent.FLAG_GRANT_READ_URI_PERMISSION,
    )
}

private fun getFileName(context: Context, uri: Uri): String? {
    return context.contentResolver
        .query(uri, null, null, null, null)
        ?.use { cursor ->
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(index)
        } ?: return null
}

private fun isValidFile(uri: Uri, context: Context, mimeTypes: Map<String, String>): Boolean {
    val name = getFileName(context, uri)

    return if (name == null) {
        false
    } else {
        mimeTypes.keys.toTypedArray().any { name.endsWith(it, true) }
    }
}

@Composable
fun FileInput(
    title: String? = null,
    placeholder: String,
    pathToSelectedFile: Uri?,
    onFileSelected: (Uri) -> Unit,
    mimeTypes: Map<String, String> = DEFAULT_MIME_TYPES,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            askForPermission(context, uri)
            isValidFile(uri, context, mimeTypes)
            onFileSelected(uri)
        }
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(bottom = 16.dp),
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            CustomElevatedButton(
                onclick = { launcher.launch((mimeTypes.values).toTypedArray()) },
                label = stringResource(
                    when(pathToSelectedFile) {
                        null -> R.string.file_input_select
                        else -> R.string.file_input_change
                    }
                ),
                // buttonClass = ButtonClass.SECONDARY, TODO:
            )

            when(pathToSelectedFile) {
                null -> FileInfo(Icons.Outlined.Info, placeholder)
                else -> {
                    val selectedFileName = getFileName(context, pathToSelectedFile) ?: pathToSelectedFile.toString()
                    return FileInfo(Icons.Default.FilePresent, selectedFileName)
                }
            }
        }
    }
}

@Composable
private fun FileInfo(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(start = 16.dp),
    ) {
        Icon(icon, text)

        Spacer(Modifier.width(4.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FileInputPreview_NotSelected() {
    AppTheme {
        FileInput(
            title = "Title",
            placeholder = "upload .pdf",
            onFileSelected = {},
            pathToSelectedFile = "test/bla.pdf".toUri()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FileInputPreview_Selected() {
    AppTheme {
        FileInput(
            title = "Title",
            placeholder = "upload .pdf",
            onFileSelected = {},
            pathToSelectedFile = null,
        )
    }
}