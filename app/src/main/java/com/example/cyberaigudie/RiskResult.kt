package com.example.cyberaigudie

/**
 * The result of analyzing a message or a URL.
 *
 * It holds everything the Risk Result screen needs to display:
 *  - the risk level (LOW / MEDIUM / HIGH)
 *  - a simple score (higher = more warning signs)
 *  - the list of detected warning signs ("reasons")
 *  - a recommended action
 *  - a short summary of the text that was checked
 */
data class RiskResult(
    val level: RiskLevel,
    val score: Int,
    val reasons: List<String>,
    val recommendation: String,
    val checkedText: String
)

/** The three possible risk levels. */
enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH;

    companion object {

        // Recommended actions shown on the result screen.
        // These are written exactly as required by the app design.
        const val RECOMMENDATION_HIGH =
            "Do not share OTP, password, banking information, or money."
        const val RECOMMENDATION_MEDIUM =
            "Be careful and verify the sender or website before taking action."
        const val RECOMMENDATION_LOW =
            "No obvious warning signs were detected, but always remain cautious."

        /** Turns a score into a risk level. */
        fun fromScore(score: Int): RiskLevel = when {
            score >= 35 -> HIGH
            score >= 15 -> MEDIUM
            else -> LOW
        }

        /** Returns the recommended action for a risk level. */
        fun recommendationFor(level: RiskLevel): String = when (level) {
            HIGH -> RECOMMENDATION_HIGH
            MEDIUM -> RECOMMENDATION_MEDIUM
            LOW -> RECOMMENDATION_LOW
        }
    }
}
