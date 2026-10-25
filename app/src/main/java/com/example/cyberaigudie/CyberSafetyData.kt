package com.example.cyberaigudie

/** One lesson in the Cyber Safety Guide. */
data class GuideSection(
    val title: String,
    val explanation: String,
    val tip: String
)

/**
 * The content of the Cyber Safety Guide: ten short beginner-friendly lessons.
 * Keeping the text here instead of ten XML files makes it easy to read and edit.
 */
object CyberSafetyData {

    val sections = listOf(
        GuideSection(
            title = "Password Safety",
            explanation = "A strong password is long and hard to guess. Use a mix of letters, " +
                "numbers and symbols, and never use the same password on different websites. " +
                "If one website leaks your password, all your other accounts become risky too.",
            tip = "Use a different password for every important account, and consider a password manager."
        ),
        GuideSection(
            title = "Two-Factor Authentication (2FA)",
            explanation = "Two-factor authentication adds a second step when you log in — usually " +
                "a code on your phone. Even if someone learns your password, they still cannot log " +
                "in without that second step.",
            tip = "Turn on 2FA for your email, social media and banking accounts today."
        ),
        GuideSection(
            title = "Phishing Awareness",
            explanation = "Phishing is when a scammer pretends to be a company or a person you " +
                "trust, to steal your details. Phishing messages often create urgency, ask you to " +
                "click a link, or ask for an OTP or password.",
            tip = "Never share OTPs or passwords, and check the sender carefully before you click anything."
        ),
        GuideSection(
            title = "Safe Browsing",
            explanation = "Before typing your details on a website, check that the address starts " +
                "with https:// and looks exactly like the real website. A small spelling mistake in " +
                "the name can mean a fake site.",
            tip = "Type important website addresses yourself instead of tapping links in messages."
        ),
        GuideSection(
            title = "Social Media Safety",
            explanation = "Scammers use social media to collect personal information. Sharing your " +
                "location, birthday or family details publicly can help them trick you or guess " +
                "your passwords.",
            tip = "Keep your profiles private and accept friend requests only from people you know."
        ),
        GuideSection(
            title = "Public Wi-Fi Safety",
            explanation = "Public Wi-Fi in cafes, airports and malls may be monitored or even fake. " +
                "Avoid logging into bank accounts or entering passwords while you are on public Wi-Fi.",
            tip = "Use mobile data for sensitive tasks, or a trusted VPN if you must use public Wi-Fi."
        ),
        GuideSection(
            title = "App Permission Safety",
            explanation = "Apps can ask for many permissions. A simple torch app does not need your " +
                "contacts or messages. Permissions you grant can expose your private information.",
            tip = "Review app permissions and remove any permission that an app does not really need."
        ),
        GuideSection(
            title = "Software Updates",
            explanation = "Updates do not only add new features — they also fix security problems. " +
                "Attackers often use old bugs in outdated apps and operating systems.",
            tip = "Turn on automatic updates for your phone, apps and computer."
        ),
        GuideSection(
            title = "Backup Safety",
            explanation = "A backup is a copy of your important files kept somewhere safe. If your " +
                "phone is lost, stolen or attacked by ransomware, a backup lets you recover your data.",
            tip = "Back up important files regularly to a trusted service or an external drive."
        ),
        GuideSection(
            title = "OTP Safety",
            explanation = "An OTP (one-time password) is a code that proves it is really you. It is " +
                "like the key to your account. Banks and real companies never call or message you " +
                "to ask for your OTP.",
            tip = "Never read out or forward an OTP — not even to someone claiming to be from your bank."
        )
    )
}
