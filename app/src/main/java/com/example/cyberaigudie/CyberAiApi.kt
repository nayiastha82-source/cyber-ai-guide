package com.example.cyberaigudie

/**
 * The interface used by the AI Cyber Chat screen to talk to an AI.
 *
 * Keeping the AI behind an interface means the app can use the built-in
 * demo answers (MockCyberAi) or a real API (OpenAiCompatibleApi) later,
 * without changing any code in the ChatFragment.
 */
interface CyberAiApi {

    /**
     * Sends [question] to the AI and calls [onResult] with the answer.
     * [onResult] is always called on the main (UI) thread.
     */
    fun ask(question: String, onResult: (String) -> Unit)
}
