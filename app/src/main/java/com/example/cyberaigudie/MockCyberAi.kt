package com.example.cyberaigudie

import android.os.Handler
import android.os.Looper

/**
 * Demo mode AI: a small built-in knowledge base about cyber safety.
 *
 * It is used when no real API key is configured, so the whole app
 * still works fully offline. This also means no user question is
 * ever sent anywhere in demo mode.
 */
class MockCyberAi : CyberAiApi {

    /** One topic the demo AI knows about. */
    private data class KnowledgeEntry(val keywords: List<String>, val answer: String)

    private val defaultAnswer =
        "I'm currently running in demo mode. AI API integration can be configured later. " +
            "Meanwhile, try asking about: phishing, OTP, passwords, 2FA, public Wi-Fi, " +
            "ransomware, VPN or social media safety."

    // Short exact greetings. These are checked with == (not "contains"),
    // so a word like "phishing" is never mistaken for the greeting "hi".
    private val greetings = setOf("hi", "hii", "hello", "hey", "hlo", "hi!", "hello!", "hey!")

    // Order matters: the first entry whose keyword is found in the question is used.
    private val knowledge = listOf(
        KnowledgeEntry(
            listOf("clicked", "tapped", "opened a link", "opened the link"),
            "If you clicked a suspicious link:\n\n" +
                "1. Do NOT enter any information on the page that opened.\n" +
                "2. Close the page immediately.\n" +
                "3. If you typed your password, change it right away.\n" +
                "4. If you shared an OTP or bank details, call your bank's official helpline now.\n\n" +
                "Also run a security scan and watch your account for strange activity."
        ),
        KnowledgeEntry(
            listOf("phishing"),
            "Phishing is when a scammer pretends to be a real company or person to steal your details.\n\n" +
                "Warning signs: urgency (\"act now!\"), requests for OTPs or passwords, and links that " +
                "look almost real. If you receive such a message: do not click anything, and never share codes."
        ),
        KnowledgeEntry(
            listOf("otp"),
            "An OTP (one-time password) is a code that proves it is really you. It is like the key to your account.\n\n" +
                "Banks and real companies NEVER ask for your OTP by call, message or email. " +
                "If someone asks for your OTP, it is always a scam — end the conversation."
        ),
        KnowledgeEntry(
            listOf("2fa", "two-factor", "two factor", "authenticator", "verification step"),
            "Two-factor authentication (2FA) adds a second step when you log in, usually a code on your phone.\n\n" +
                "Even if someone learns your password, they still cannot log in without that second step. " +
                "Turn on 2FA for your email, social media and banking accounts today."
        ),
        KnowledgeEntry(
            listOf("password"),
            "A strong password is long (12+ characters) and hard to guess. Mix letters, numbers and symbols.\n\n" +
                "Most importantly: use a DIFFERENT password for every important account. " +
                "If one website is hacked, your other accounts stay safe. A password manager can remember them for you."
        ),
        KnowledgeEntry(
            listOf("ransomware"),
            "Ransomware is malicious software that locks your files or your phone and demands money to unlock them.\n\n" +
                "Protect yourself: keep your system updated, do not install apps from outside the official store, " +
                "and keep backups of important files — then you never need to pay anyone."
        ),
        KnowledgeEntry(
            listOf("wifi", "wi-fi", "hotspot"),
            "Public Wi-Fi in cafes, airports and malls may be monitored or even fake.\n\n" +
                "Avoid logging into bank accounts or typing passwords on public Wi-Fi. " +
                "Use mobile data for sensitive tasks, or a trusted VPN if you must use public Wi-Fi. " +
                "Also turn off \"auto-connect to open networks\" in your phone settings."
        ),
        KnowledgeEntry(
            listOf("instagram", "facebook", "social media", "snapchat", "whatsapp account"),
            "Social media safety basics:\n\n" +
                "1. Keep your profile private.\n" +
                "2. Turn on two-factor authentication.\n" +
                "3. Do not click links in DMs from strangers (or from friends whose account may be hacked).\n" +
                "4. Never share codes or OTPs — even if a \"friend\" asks.\n" +
                "5. Think before posting: location and personal details help scammers."
        ),
        KnowledgeEntry(
            listOf("malware", "virus", "antivirus", "trojan"),
            "Malware (malicious software) can steal data or damage your phone.\n\n" +
                "To stay safe: install apps only from the official store, check what permissions apps ask for, " +
                "keep your phone and apps updated, and uninstall anything you do not recognise."
        ),
        KnowledgeEntry(
            listOf("vpn"),
            "A VPN (Virtual Private Network) hides and protects your internet traffic, which is useful " +
                "on public Wi-Fi.\n\n" +
                "Use a well-known, trusted VPN. Be careful with free VPNs — some collect and sell your data, " +
                "which defeats the whole purpose."
        ),
        KnowledgeEntry(
            listOf("scam", "scammer", "fraud", "cheat"),
            "Common scam warning signs:\n\n" +
                "• Urgency: \"act now or your account will be closed\"\n" +
                "• Money requests, gift cards or crypto payments\n" +
                "• Asking for OTPs, passwords or bank details\n" +
                "• Offers that are too good to be true\n\n" +
                "Always verify through the company's official app, website or helpline before taking action."
        ),
        KnowledgeEntry(
            listOf("browsing", "https", "website", "web", "browser"),
            "Safe browsing basics:\n\n" +
                "1. Check that the address starts with https:// before entering details.\n" +
                "2. Look closely at the website name for spelling tricks (paypa1 instead of paypal).\n" +
                "3. Avoid entering details on pages that open from pop-ups.\n" +
                "4. Type important website addresses yourself instead of tapping links in messages."
        ),
        KnowledgeEntry(
            listOf("backup", "back up", "back-up"),
            "A backup is a safe copy of your important files. If your phone is lost, stolen or attacked " +
                "by ransomware, a backup lets you get everything back.\n\n" +
                "Simple rule: keep copies in two different places, for example your phone plus a trusted " +
                "cloud service or an external drive."
        ),
        KnowledgeEntry(
            listOf("email", "spam"),
            "Email safety basics:\n\n" +
                "1. Do not click links in unexpected emails, even if they look official.\n" +
                "2. Check the sender's full address — scammers use look-alike addresses.\n" +
                "3. Never email your password, OTP or card details to anyone.\n" +
                "4. Report suspicious mail as spam so your filter learns."
        ),
        KnowledgeEntry(
            listOf("who are you", "what are you", "your name", "are you real", "are you a robot"),
            "I'm your Cyber AI Guide — a chat assistant inside the Cyber AI Guide app that answers " +
                "questions about online safety. I only talk about cyber safety, so ask away!"
        ),
        KnowledgeEntry(
            listOf("demo", "api", "real ai"),
            "Right now I'm running in demo mode with a built-in offline knowledge base. " +
                "A real AI API can be connected later without changing the app code — " +
                "the app reads the key from a private configuration file (local.properties), " +
                "never from the source code."
        )
    )

    override fun ask(question: String, onResult: (String) -> Unit) {
        val answer = findAnswer(question)

        // A small delay so it feels like the AI is "thinking".
        Handler(Looper.getMainLooper()).postDelayed({ onResult(answer) }, 500)
    }

    /** Finds the best answer for a question using simple keyword matching. */
    private fun findAnswer(question: String): String {
        val normalized = question.lowercase().trim()

        // Exact greeting check first (see comment on `greetings`).
        if (normalized in greetings) {
            return "Hello! Ask me anything about cyber safety — for example: " +
                "\"What is phishing?\" or \"How do I make a strong password?\""
        }

        // Then look for a topic by keyword. First match wins.
        for (entry in knowledge) {
            if (entry.keywords.any { normalized.contains(it) }) {
                return entry.answer
            }
        }

        return defaultAnswer
    }
}
