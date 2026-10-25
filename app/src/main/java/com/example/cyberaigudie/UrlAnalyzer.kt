package com.example.cyberaigudie

/**
 * Rule-based analyzer for website URLs.
 *
 * IMPORTANT: this app NEVER opens or visits the link — it only studies the
 * text of the address for common tricks used in phishing links.
 * Everything runs on the phone and nothing is uploaded.
 */
class UrlAnalyzer {

    // Words commonly found in scam / phishing links.
    private val suspiciousWords = listOf(
        "login", "verify", "account", "free", "reward", "winner", "urgent",
        "secure", "update", "confirm", "signin", "banking", "password",
        "bonus", "gift", "claim", "kyc"
    )

    // Services that make a long link short, hiding the real destination.
    private val shorteners = listOf(
        "bit.ly", "tinyurl.com", "t.co", "goo.gl", "rb.gy", "is.gd",
        "ow.ly", "buff.ly", "cutt.ly", "shorturl.at"
    )

    // Fake versions of famous brand names (for example "0" instead of "o").
    private val lookAlikeBrands = listOf(
        "paypa1", "g00gle", "amaz0n", "rnicrosoft", "faceb00k",
        "app1e", "netfl1x", "instagrarn", "whatsapp-"
    )

    // Domain endings often used in scam links.
    private val riskyTlds = listOf(
        ".zip", ".mov", ".top", ".xyz", ".tk", ".ml", ".ga", ".cf",
        ".gq", ".click", ".work", ".country"
    )

    /** Analyzes a URL and returns a [RiskResult]. */
    fun analyze(rawUrl: String): RiskResult {
        val url = rawUrl.trim()
        val lower = url.lowercase()
        val reasons = mutableListOf<String>()
        var score = 0

        // 1. HTTPS check — https:// means the connection is encrypted.
        if (!lower.startsWith("https://")) {
            score += 15
            reasons.add("The link does not start with https://, so the connection may not be secure.")
        }

        // The website name (host) is the part between "://" and the first "/".
        val afterProtocol = lower.removePrefix("https://").removePrefix("http://")
        val host = afterProtocol.substringBefore('/').substringBefore('?')

        // 2. A raw IP address (like 192.168.0.1) instead of a real name.
        if (Regex("^\\d{1,3}(\\.\\d{1,3}){3}").containsMatchIn(host)) {
            score += 35
            reasons.add("The link uses a raw IP address instead of a normal website name. Scammers often do this.")
        }

        // 3. The "@" trick: everything before "@" is ignored by browsers,
        //    so "real-bank.com@evil-site.com" actually goes to evil-site.com.
        if (url.contains('@')) {
            score += 35
            reasons.add("The link contains an \"@\" sign. The real destination is hidden after it.")
        }

        // 4. Encoded characters (xn--) can be used to copy the letters of a real site.
        if (host.contains("xn--")) {
            score += 30
            reasons.add("The website name uses special encoded characters (xn--), which can fake a real name.")
        }

        // 5. Very long links are used to hide the real website inside the text.
        if (url.length > 75) {
            score += 15
            reasons.add("The link is unusually long, which can be used to hide the real website.")
        }

        // 6. Many hyphens in the website name (example: secure-login-bank-verify.com).
        if (host.count { it == '-' } >= 3) {
            score += 15
            reasons.add("The website name contains many hyphens (-), which is common in fake links.")
        }

        // 7. Many dots in the website name (deep sub-domains, like login.secure.bank.evil.com).
        if (host.count { it == '.' } >= 4) {
            score += 10
            reasons.add("The website name contains many dots, which can be a trick to look real.")
        }

        // 8. Suspicious words in the link.
        val foundWords = suspiciousWords.filter { afterProtocol.contains(it) }
        if (foundWords.isNotEmpty()) {
            score += minOf(foundWords.size * 10, 25)
            reasons.add("The link contains words often used in scams: " + foundWords.joinToString(", ") + ".")
        }

        // 9. Shortened links hide where they really go.
        if (shorteners.any { host.endsWith(it) }) {
            score += 20
            reasons.add("This looks like a shortened link. You cannot see where it really goes before opening it.")
        }

        // 10. A fake version of a famous brand name.
        if (lookAlikeBrands.any { host.contains(it) }) {
            score += 25
            reasons.add("The website name looks like a fake version of a well-known brand.")
        }

        // 11. Risky domain endings.
        val riskyTld = riskyTlds.firstOrNull { host.endsWith(it) }
        if (riskyTld != null) {
            score += 15
            reasons.add("The link ends with \"$riskyTld\", a domain ending often used in scam links.")
        }

        // 12. Unusual port number (normal websites do not show one).
        if (Regex(":\\d{2,5}").containsMatchIn(host)) {
            score += 10
            reasons.add("The link shows an unusual port number, which normal websites do not use.")
        }

        val level = RiskLevel.fromScore(score)

        return RiskResult(
            level = level,
            score = score,
            reasons = reasons,
            recommendation = RiskLevel.recommendationFor(level),
            checkedText = "URL: \"$url\""
        )
    }
}
