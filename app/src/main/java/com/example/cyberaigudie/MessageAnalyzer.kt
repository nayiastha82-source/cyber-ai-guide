package com.example.cyberaigudie

/**
 * Rule-based analyzer for suspicious messages.
 *
 * It looks for common scam / phishing warning signs (about 10 categories)
 * and gives a risk score. Everything runs on the phone — the message text
 * is NEVER stored, logged or uploaded anywhere.
 *
 * This is a simple awareness tool, not a guarantee.
 */
class MessageAnalyzer {

    /**
     * One warning sign rule:
     *  - pattern: what to search for in the message
     *  - weight:  how many points this sign adds to the score
     *  - reason:  the plain-English explanation shown to the user
     */
    private data class Rule(val pattern: Regex, val weight: Int, val reason: String)

    // (?i) means the search ignores upper/lower case.
    private val rules = listOf(
        Rule(
            Regex("(?i)\\b(otp|one[- ]time password|verification code|security code)\\b"),
            35,
            "It asks for an OTP or verification code. Banks and real companies never ask for these."
        ),
        Rule(
            Regex("(?i)\\b(password|login details|account details|username and password)\\b"),
            35,
            "It mentions passwords or login details. Never share these with anyone."
        ),
        Rule(
            Regex("(?i)\\b(bank account|account number|cvv|card number|debit card|credit card|aadhaar|pan card|pan number)\\b"),
            35,
            "It asks for bank, card or identity details."
        ),
        Rule(
            Regex("(?i)(\\bmoney\\b|\\btransfer\\b|\\bpayment\\b|\\brefund\\b|\\bpay\\b|₹|\\brs\\.?\\s?\\d|\\binr\\b|\\$\\s?\\d)"),
            30,
            "It talks about money, payments or transfers."
        ),
        Rule(
            Regex("(?i)\\b(blocked|suspended|deactivated|terminated|legal action|police|court|lawsuit)\\b"),
            20,
            "It threatens that your account will be blocked, or mentions legal action."
        ),
        Rule(
            Regex("(?i)\\b(urgent|immediately|last chance|act now|within [0-9]+ (hours?|minutes?|days?)|expires? (today|soon|in))\\b"),
            15,
            "It creates urgency so you act fast without thinking."
        ),
        Rule(
            Regex("(?i)\\b(won|winner|prize|lottery|lucky draw|gift|free|reward|bonus)\\b"),
            15,
            "It promises a prize, gift or reward. This is a very common scam trick."
        ),
        Rule(
            Regex("(?i)(https?://|www\\.|bit\\.ly|tinyurl|\\bt\\.co\\b|goo\\.gl|rb\\.gy|is\\.gd)"),
            15,
            "It contains a link. Be careful before tapping links in messages."
        ),
        Rule(
            Regex("(?i)(click (here|the link|this link|below)|follow this link|open the link|verify (now|here|your account))"),
            10,
            "It asks you to click a link or to \"verify\" something through it."
        ),
        Rule(
            Regex("(?i)\\b(paytm|phonepe|gpay|google pay|amazon|flipkart|netflix|sbi|hdfc|icici|paypal)\\b"),
            5,
            "It mentions a well-known company or bank. Scammers often pretend to be them."
        ),
        Rule(
            Regex("[A-Z]{6,}|!{3,}"),
            10,
            "It uses lots of capital letters or exclamation marks, which is common in scam messages."
        )
    )

    /**
     * Analyzes a message and returns a [RiskResult].
     */
    fun analyze(message: String): RiskResult {
        val reasons = mutableListOf<String>()
        var score = 0

        // Check the message against every rule once.
        for (rule in rules) {
            if (rule.pattern.containsMatchIn(message)) {
                score += rule.weight
                reasons.add(rule.reason)
            }
        }

        val level = RiskLevel.fromScore(score)

        return RiskResult(
            level = level,
            score = score,
            reasons = reasons,
            recommendation = RiskLevel.recommendationFor(level),
            checkedText = "Message: \"" + message.trim().shortenForSummary() + "\""
        )
    }

    /** Only a short preview is kept, so long messages are not carried around. */
    private fun String.shortenForSummary(): String =
        if (length <= 120) this else take(120).trimEnd() + "…"
}
