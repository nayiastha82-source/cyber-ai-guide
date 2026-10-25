package com.example.cyberaigudie

import android.os.Handler
import android.os.Looper
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Real AI implementation that talks to an OpenAI-compatible chat API.
 *
 * SECURITY NOTES:
 * - The API key is NOT written in this file. It is passed in from
 *   CyberAiRepository, which reads it from BuildConfig (local.properties).
 * - Only the user's question is sent to the API, and nothing is stored.
 * - The API key is never logged.
 */
class OpenAiCompatibleApi(private val apiKey: String) : CyberAiApi {

    override fun ask(question: String, onResult: (String) -> Unit) {
        val mainHandler = Handler(Looper.getMainLooper())

        // Network calls are not allowed on the main (UI) thread,
        // so the request runs on a background thread and the answer
        // is posted back to the main thread for the UI.
        Thread {
            var answer: String? = null
            try {
                answer = requestAnswer(question)
            } catch (e: Exception) {
                answer = null
            }

            val finalAnswer = answer
                ?: "Sorry, the AI service could not be reached right now. Please try again."

            mainHandler.post { onResult(finalAnswer) }
        }.start()
    }

    /** Performs the actual network request. Returns null when anything goes wrong. */
    private fun requestAnswer(question: String): String? {
        val url = URL("https://api.openai.com/v1/chat/completions")
        val connection = url.openConnection() as HttpsURLConnection
        return try {
            connection.requestMethod = "POST"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.doOutput = true
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.setRequestProperty("Content-Type", "application/json")

            // Build the JSON request body.
            val requestBody = JSONObject().apply {
                put("model", "gpt-4o-mini")
                put("max_tokens", 400)
                put("messages", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "system")
                        put("content", SYSTEM_PROMPT)
                    })
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", question)
                    })
                })
            }

            connection.outputStream.use { output ->
                output.write(requestBody.toString().toByteArray(Charsets.UTF_8))
            }

            if (connection.responseCode != HttpsURLConnection.HTTP_OK) {
                return null
            }

            // Read the answer from the JSON response.
            val responseText = connection.inputStream.bufferedReader().use { it.readText() }
            JSONObject(responseText)
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim()
        } finally {
            connection.disconnect()
        }
    }

    companion object {
        // The "personality" and safety rules of the AI assistant.
        private const val SYSTEM_PROMPT =
            "You are a friendly cybersecurity awareness assistant for beginners and students. " +
                "Give short, simple, practical answers about staying safe online. " +
                "Never help with hacking, stealing passwords, or anything harmful. " +
                "If a question is not related to cyber safety, politely bring the topic back " +
                "to online safety."
    }
}
