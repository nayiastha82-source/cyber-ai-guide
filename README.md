# Cyber AI Guide

An Android cybersecurity awareness app for students and beginners. It helps you judge whether a
suspicious message or website link looks like a scam, and teaches you how to stay safe online.

**Developed by Astha Nayi**

## Features

| Feature | What it does |
|---|---|
| **Home dashboard** | Quick access to all tools plus a set of short safety tips |
| **Message Checker** | Paste an SMS / WhatsApp / email message and see possible scam or phishing warning signs |
| **URL Checker** | Paste a website link and see risk indicators — the link is never opened |
| **AI Cyber Chat** | Ask cyber safety questions in plain language |
| **Cyber Safety Guide** | Ten short lessons on passwords, OTPs, UPI fraud, social media safety, and more |
| **Risk Result** | One shared result screen showing a risk score, level, and reasons |

## Privacy & security

- The Message Checker and URL Checker are **100% rule-based and run entirely on the device**.
  Your text is never stored, logged, or sent anywhere.
- No app data is collected. No account or login is required.
- The AI chat runs in **offline demo mode** by default using a built-in knowledge base.
- **No API key is ever committed to this repository.** To connect a real AI model, add one line to
  `local.properties` (which is git-ignored):

  ```
  CYBER_AI_API_KEY=your_key_here
  ```

  The app reads it at build time into `BuildConfig`, and automatically falls back to demo mode when
  the key is missing or empty.

> **Disclaimer:** this is an awareness tool, not a guarantee. Scammers change their tricks often, so
> always use your own judgement.

## Tech stack

- **Language:** Kotlin (app logic) + XML (UI layouts, colors, strings, navigation)
- **Platform:** Android — `minSdk 24`, `targetSdk 37`
- **UI:** Material Design 3 (Material Components), AndroidX AppCompat, ConstraintLayout
- **Navigation:** Jetpack Navigation Component — a single Activity with Fragments
- **View binding:** enabled (no `findViewById`)
- **Architecture:** UI fragments kept thin; detection logic lives in plain Kotlin classes
  (`MessageAnalyzer`, `UrlAnalyzer`, `RiskResult`, `CyberSafetyData`) with no Android dependencies
- **AI layer:** repository pattern (`CyberAiRepository`) that switches between `MockCyberAi` (offline)
  and `OpenAiCompatibleApi` (live) without changing any screen code
- **Build:** Gradle 9.6.0 with Android Gradle Plugin 9.4.1

## Project structure

```
app/src/main/java/com/example/cyberaigudie/
├── MainActivity.kt              Hosts the navigation graph
├── FirstFragment.kt             Home dashboard
├── MessageCheckerFragment.kt    Message Checker screen
├── UrlCheckerFragment.kt        URL Checker screen
├── SecondFragment.kt            Shared Risk Result screen
├── ChatFragment.kt              AI Cyber Chat screen
├── GuideFragment.kt             Cyber Safety Guide screen
├── ChatAdapter.kt               Chat bubble list adapter
├── ChatMessage.kt               Chat message model
├── MessageAnalyzer.kt           Scam / phishing rule engine
├── UrlAnalyzer.kt               URL risk rule engine
├── RiskResult.kt                Risk score, level and reasons model
├── CyberSafetyData.kt           Guide lessons content
├── CyberAiApi.kt                AI provider interface
├── MockCyberAi.kt               Offline demo knowledge base
├── OpenAiCompatibleApi.kt       Live AI provider
└── CyberAiRepository.kt         Chooses demo or live mode

app/src/main/res/
├── layout/       Screens and list items
├── drawable/     Icons, gradients, chat bubbles
├── navigation/   nav_graph.xml — screen flow
└── values/       colors.xml, strings.xml, themes.xml
```

## Build and run

**Requirements:** Android Studio (or just the Android SDK + a JDK), and a device or emulator running
Android 7.0 (API 24) or newer.

### Option A — Android Studio
1. **File → Open** and select this project folder.
2. Wait for Gradle sync to finish.
3. Click **Run** to install on a connected device or emulator.

### Option B — Command line
Double-click `build.bat`, or run:

```
gradlew.bat assembleDebug
```

The APK is written to:

```
app/build/outputs/apk/debug/app-debug.apk
```

Copy it to an Android phone and tap it to install (allow "install from unknown sources" when asked).

## Screens

Home dashboard → Message Checker → Risk Result
Home dashboard → URL Checker → Risk Result
Home dashboard → AI Cyber Chat
Home dashboard → Cyber Safety Guide

## License

Built as an educational / awareness project. Free to use for learning.
