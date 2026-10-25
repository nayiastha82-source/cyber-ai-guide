package com.example.cyberaigudie

/**
 * Decides which AI the app uses.
 *
 * - If CYBER_AI_API_KEY is set in local.properties, the app uses the real
 *   OpenAI-compatible API.
 * - If the key is empty or missing, the app uses MockCyberAi (demo mode),
 *   so everything still works without any API key.
 *
 * The API key comes from BuildConfig, which Gradle fills from
 * local.properties. local.properties is listed in .gitignore, so the key
 * is never committed to GitHub.
 */
object CyberAiRepository {

    /** True when no API key is configured and the chat runs in demo mode. */
    val isDemoMode: Boolean = BuildConfig.CYBER_AI_API_KEY.isBlank()

    private val api: CyberAiApi =
        if (isDemoMode) MockCyberAi() else OpenAiCompatibleApi(BuildConfig.CYBER_AI_API_KEY)

    /** Asks the current AI a question and returns the answer through [onResult]. */
    fun ask(question: String, onResult: (String) -> Unit) {
        api.ask(question, onResult)
    }
}
