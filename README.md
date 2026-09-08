<div align="center">
  <img src="./readme-assets/screenshots/icon.png" alt="PageKeeper app icon" width="112" height="112" />

  <h1>PageKeeper</h1>

  <p><strong>Your personal FB2 library and reader for Android.</strong></p>

  <p>
    Import, organize, read, and bookmark ebooks in a clean, adaptive interface.
  </p>

<p align="center">
  <img
    src="https://img.shields.io/badge/Android-7.0%2B-3DDC84?style=flat-square&logo=android&logoColor=white"
    alt="Android 7.0 or newer"
  />
  <img
    src="https://img.shields.io/badge/Kotlin-2.4.10-7F52FF?style=flat-square&logo=kotlin&logoColor=white"
    alt="Kotlin 2.4.10"
  />
  <img
    src="https://img.shields.io/badge/Jetpack-Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white"
    alt="Jetpack Compose"
  />
  <img
    src="https://img.shields.io/badge/version-1.0.0-007EC6?style=flat-square"
    alt="Version 1.0.0"
  />
  <img
    src="https://img.shields.io/github/last-commit/Tonnie-Dev/PageKeeper?style=flat-square&label=last%20commit"
    alt="Last commit"
  />
  <img
    src="https://img.shields.io/github/license/Tonnie-Dev/PageKeeper?style=flat-square"
    alt="MIT License"
  />
</p>

  <p>
    <a href="#screenshots">Screenshots</a> &bull;
    <a href="#features">Features</a> &bull;
    <a href="#tech-stack">Tech stack</a> &bull;
    <a href="#getting-started">Getting started</a> &bull;
    <a href="#architecture">Architecture</a> &bull;
    <a href="#contributing">Contributing</a> &bull;
   <a href="#️author">Author</a> 
  </p>
</div>

---

## 📱 About PageKeeper

PageKeeper is a local-first Android ebook manager and reader focused on the **FB2 (FictionBook 2.0)** format. It extracts book metadata and cover art during import, keeps reading progress on-device, and provides a comfortable reading experience without requiring an account.

> [!NOTE]
> PageKeeper currently supports `.fb2` files only. Imported books, reading progress, bookmarks, and reader preferences are stored locally on the device.

---

<p align="center">
  <!-- Google Play badge -->
  <a href="https://play.google.com/store/apps/details?id=com.tonyxlab.smartstep" target="_blank">
    <img alt="Get it on Google Play" src="./readme-assets/screenshots/google_badge.png" width="280" />
  </a>
</p>

<p align="center">
  <!-- Demo GIF -->
  <img src="./readme-assets/gif/demo.gif" width="560" alt="App demo" />
</p>

---

## 📸 Screenshots

<p align="center">
  <img src="./readme-assets/screenshots/screen_1.png" width="30%" alt="PageKeeper library screen" />
  <img src="./readme-assets/screenshots/screen_2.png" width="30%" alt="PageKeeper reader screen" />
  <img src="./readme-assets/screenshots/screen_3.png" width="30%" alt="PageKeeper bookmarks screen" />
</p>

## ✨ Features

- 📥 Import FB2 eBooks using Android’s system file picker
- 📚 Extract titles, authors, cover images, chapters, and formatted text
- 🛡️ Prevent duplicate imports using SHA-256 content hashing
- 🔍 Search the library and select multiple books for bulk actions
- ❤️ Mark books as favorites or finished
- 📤 Share books or remove them from the library
- 🔖 Resume reading from the last saved position and track progress
- 📑 Browse chapters and jump directly to a selected section
- 🏷️ Create, edit, color-code, open, and delete bookmarks
- 🔠 Adjust and persist the reader font size
- 📱 Enjoy adaptive navigation across different screen sizes
- 🔒 Keep library data private with local Room and DataStore persistence

## 🚀 Tech Stacks

- 🟣 Kotlin — primary programming language
- 🎨 Jetpack Compose — modern declarative UI toolkit
- 🧩 Material 3 — adaptive layouts and UI components
- 🔄 Kotlin Coroutines & Flow — asynchronous and reactive data handling
- 🧭 Navigation 3 — serializable, type-safe navigation
- 🧱 MVVM + Clean Architecture — separation of presentation, domain, and data layers
- 💾 Room Database — local storage for books, bookmarks, and reading progress
- ⚙- ️ Preferences DataStore — reader settings and user preferences
- 🧪 **Koin** — dependency injection
- 📖 XmlPullParser / KXml — FB2 eBook parsing
- 🖼️ Coil — book-cover image loading
- 🪵 Timber — debug logging



## 🏗️ Architecture

PageKeeper follows MVVM with Clean Architecture, separating the app into three main layers:

🎨 Presentation — Jetpack Compose screens, ViewModels, UI state, events, and Navigation 3
🧠 Domain — business models and repository contracts
💾 Data — Room, DataStore, repository implementations, and the FB2 importer/parser

The presentation layer uses unidirectional state handling:

📦 Immutable StateFlow values represent persistent UI state
👆 Sealed UI events represent user interactions
⚡ Channel-backed action events deliver one-off effects such as navigation and snackbar messages

## 🧰 Getting Started

### Prerequisites

- A recent stable version of [Android Studio](https://developer.android.com/studio)
- An emulator or physical device running Android 7.0 (API 24) or newer

### Set up the project

1. Clone the repository:

   ```bash
   git clone https://github.com/Tonnie-Dev/PageKeeper.git
   ```

2. Open the project in Android Studio and allow Gradle to sync.

3. Select the `app` run configuration and launch it on an emulator or connected device.

No API keys or external services are required.

## 🛂 Contributing

Contributions are welcome. To propose a change:

1. Fork the repository and create a branch from `main`.
2. Keep changes focused and follow the existing architecture and naming conventions.
3. Add or update tests where appropriate.
4. Run `./gradlew test` and `./gradlew :app:compileDebugKotlin` before opening a pull request.
5. Open a pull request describing the problem and your solution.

For larger features, consider opening an issue first so the implementation can be discussed.


## 🖋️ Author

Created by **Tonnie Dev**.

<p>
  <a href="https://github.com/Tonnie-Dev">
    <img src="https://img.shields.io/badge/GitHub-Tonnie--Dev-181717?style=for-the-badge&logo=github" alt="Tonnie Dev on GitHub" />
  </a>
  <a href="https://www.linkedin.com/in/antony-muchiri/">
    <img src="https://img.shields.io/badge/LinkedIn-Antony_Muchiri-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white" alt="Antony Muchiri on LinkedIn" />
  </a>
  <a href="https://twitter.com/Tonnie_Dev">
    <img src="https://img.shields.io/badge/X-@Tonnie__Dev-000000?style=for-the-badge&logo=x&logoColor=white" alt="Tonnie Dev on X" />
  </a>
</p>

## License

PageKeeper is available under the [MIT License](./readme-assets/LICENSE).
