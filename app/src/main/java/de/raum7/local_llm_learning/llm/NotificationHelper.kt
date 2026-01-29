package de.raum7.local_llm_learning.llm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import de.raum7.local_llm_learning.R

/**
 * Hilfsklasse für das Erstellen von Notifications
 * für den LLM Foreground Service.
 */
object NotificationHelper {

    // ID des Notification Channels
    private const val CHANNEL_ID = "llm_channel"

    /**
     * Erstellt den Notification Channel.
     * Muss einmal beim App-Start aufgerufen werden.
     */
    fun createChannel(ctx: Context) {
        val nm = ctx.getSystemService(NotificationManager::class.java)

        val ch = NotificationChannel(
            CHANNEL_ID,
            "LLM Generation",
            NotificationManager.IMPORTANCE_LOW
        )

        nm.createNotificationChannel(ch)
    }

    /**
     * Notification während der laufenden Generierung.
     * Enthält eine Action zum Abbrechen.
     */
    fun running(ctx: Context, text: String): Notification {

        // Intent zum Stoppen des Services
        val stopIntent = Intent(ctx, LlmGenerationService::class.java).apply {
            action = LlmGenerationService.ACTION_STOP
        }

        // PendingIntent für den Abbruch Button
        val stopPending = PendingIntent.getService(
            ctx,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setContentTitle("LLM läuft")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, "Abbrechen", stopPending)
            .build()
    }

    /**
     * Notification nach Abschluss der Generierung.
     * Öffnet die App beim Antippen.
     */
    fun finished(ctx: Context, openPending: PendingIntent): Notification {
        return NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setContentTitle("LLM fertig")
            .setContentText("Tippen zum Ergebnis")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(openPending)
            .setAutoCancel(true)
            .build()
    }
}
