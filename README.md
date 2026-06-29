# 💊 Pharmacy Inventory

An offline-first Android app that pulls medicine stock and sales prices from a hosted JSON API, caches them locally, and provides instant search — even without internet.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android)
![Language](https://img.shields.io/badge/language-Kotlin-7F52FF?logo=kotlin)
![API](https://img.shields.io/badge/API-26%2B-4CAF50)
![Build](https://github.com/danielsem65/Pharmacy-Inventory/actions/workflows/build.yml/badge.svg)

---

## ✨ Features

| Feature | Description |
|---|---|
| **📡 API Sync** | Pulls the latest medicine data from your hosted JSON source |
| **📴 Offline-First** | Data is cached locally using Room — works with no internet |
| **🔍 Search** | Real-time search by medicine name with debounced input |
| **🔄 Refresh** | One-tap refresh button to re-sync the latest data |
| **📦 4,000+ Medicines** | Full product catalog loaded and searchable instantly |
| **👨‍💼 Responsible Person** | See which staff member is assigned to each item |
| **💰 Sales Prices** | Clear price display in GH¢ (Ghana Cedi) |

## 📲 Download

Get the latest APK from GitHub Actions:

1. Go to **[Actions → Build APK](https://github.com/danielsem65/Pharmacy-Inventory/actions/workflows/build.yml)**
2. Click on the latest successful run (green checkmark)
3. Scroll down to **Artifacts**
4. Download **`PharmacyInventory.zip`**
5. Extract the `.apk` and install on your Android device

> **Note:** Enable *"Install from unknown apps"* in your device settings if prompted.

## 🏗 Architecture

```
Pharmacy Inventory
├── app/
│   └── src/main/java/com/pharmacy/inventory/
│       ├── MainActivity.kt          ← Entry point (Compose host)
│       ├── PharmacyApp.kt           ← Application class (DI setup)
│       ├── data/
│       │   ├── model/Medicine.kt    ← Data models + Gson mapping
│       │   ├── local/               ← Room DB + DAO (offline cache)
│       │   └── repository/          ← Repository pattern
│       ├── network/MedicineApi.kt   ← Retrofit API interface
│       └── ui/                      ← Jetpack Compose UI + ViewModel
├── .github/workflows/build.yml      ← CI pipeline (auto-builds APK)
└── build.gradle.kts                 ← Gradle project config
```

### Data Flow

```
┌──────────┐     ┌──────────┐     ┌────────────┐     ┌─────────┐
│ jsonsilo │ ──> │ Retrofit │ ──> │ Repository │ ──> │ Room DB │
│   API    │     │ (HTTP)   │     │            │     │ (Cache) │
└──────────┘     └──────────┘     └──────┬─────┘     └─────────┘
                                         │
                                         ▼
                                  ┌──────────────┐
                                  │  ViewModel   │
                                  │  (StateFlow) │
                                  └──────┬───────┘
                                         │
                                         ▼
                                  ┌──────────────┐
                                  │ Compose UI   │
                                  │ (LazyColumn) │
                                  └──────────────┘
```

- **Online:** App fetches fresh data from the API → stores in Room → displays via Compose
- **Offline:** App loads directly from Room DB — previously synced data is always available
- **Refresh:** Manual refresh button triggers a full re-sync from the API

## 🛠 Tech Stack

| Layer | Library |
|---|---|
| UI | [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3) |
| State | `ViewModel` + `StateFlow` |
| Database | [Room](https://developer.android.com/training/data-storage/room) (SQLite) |
| Networking | [Retrofit](https://square.github.io/retrofit/) + [Gson](https://github.com/google/gson) |
| Build | Gradle 8.5 + AGP 8.2.2 |
| CI/CD | GitHub Actions |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |

## 🚀 Local Development

### Prerequisites

- [Android Studio](https://developer.android.com/studio) Hedgehog or later
- JDK 17+

### Steps

```bash
# Clone the repository
git clone https://github.com/danielsem65/Pharmacy-Inventory.git

# Open in Android Studio
cd Pharmacy-Inventory
# File → Open → select the folder

# Sync Gradle & Run
# Wait for Gradle sync to finish, then click Run ▶
```

The app will automatically load data from the hosted API on first launch.

## 🔧 Configuration

If you need to change the data source URL, edit:

- `app/src/main/java/com/pharmacy/inventory/network/MedicineApi.kt:8` — Endpoint path
- `app/src/main/java/com/pharmacy/inventory/PharmacyApp.kt:14` — Base URL

## 📄 License

This project is private and developed for **Pharmacy Inventory**.
