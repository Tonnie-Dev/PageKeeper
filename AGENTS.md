# PageKeeper AI Agent Guide

**PageKeeper** is an Android book management app built with **Jetpack Compose** for browsing, importing, and tracking FB2 ebooks with a modern architecture using clean layers (domain/data/presentation).

## Architecture Overview

### Layer Structure (Clean Architecture)
```
presentation/ → core handlers (BaseViewModel) → screens (Library)
     ↓
data/ → repository (BookRepository impl) + database (Room/DAO) + importer (BookImporter)
     ↓
domain/ → models (Book) + interfaces (BookRepository) + value objects (ImportBookResult)
```

**Key Files:**
- `PageKeeperApp.kt` - Application class initializing Koin DI & Timber logging
- `MainActivity.kt` - Single activity with LibraryScreen as main content
- `AppModule.kt` - Koin module defining viewModels, database, repository, importer singletons

### Presentation Layer Pattern (MVVM with State Management)

**All ViewModels extend `BaseViewModel<State, Event, ActionEvent>`:**
- **State** (implements `UiState`) - immutable data held in `MutableStateFlow`, updated via `updateState { copy(...) }`
- **Event** (implements `UiEvent`) - user actions dispatched via `onEvent(event)`
- **ActionEvent** (implements `ActionEvent`) - one-off side effects sent to UI via Channel (toast, navigation)

**Example:** `LibraryViewModel` with `LibraryUiState` (books, isLoading, isImporting, dialog) → events like `OpenBook` → actions like `ShowToast`

**Base utilities in** `presentation/core/`:
- `BaseViewModel.launchCatching()` - wraps suspend work with onStart/onError/onCompletion callbacks
- `BaseContentLayout` - generic Compose container managing action event collection & action handler
- `UiState/UiEvent/ActionEvent` - marker interfaces for type safety

### Data Layer

**Room Database:** `PageKeeperDatabase` with single entity `BookEntity` (id, title, author, filePath, dateAdded, isFavorite, isFinished)

**Repository Pattern:** `BookRepository` interface in domain, `BookRepositoryImpl` in data
- Uses DAO for DB access, maps `BookEntity` ↔ `Book` domain model
- `observeBooks()` returns `Flow<List<Book>>` for reactive updates

**BookImporter:** Complex business logic for FB2 file import
- Validates `.fb2` extension
- Calculates SHA-256 file hash to detect duplicates
- Copies file to internal storage (`context.filesDir/books/`)
- Extracts metadata (title, author, cover) via XML parsing
- Returns sealed `ImportBookResult` (Success, Duplicate, UnsupportedFormat, Error)

## Build & Development Workflow

### Gradle Configuration
- **Plugin setup** in `settings.gradle.kts`: KSP (annotation processor), Compose Compiler, Kotlin Serialization
- **Version catalog** in `gradle/libs.versions.toml`: AGP 9.2.1, Kotlin 2.3.21, KSP 2.3.2
- **Dependency management via refreshVersions**: Run `./gradlew refreshVersionsCatalog` to update `libs.versions.toml`

### Build Commands
```bash
# Build debug variant
./gradlew build

# Run unit tests (JUnit4 + Kotest)
./gradlew test

# Run instrumented tests (Espresso + Compose UI tests)
./gradlew connectedAndroidTest

# Build APK
./gradlew assembleDebug

# Watch mode for rapid iteration
./gradlew build -t
```

### Target & Compile Settings
- Target SDK: 36 (Android 15)
- Min SDK: 24 (Android 7.0)
- Compile SDK: 36 with minor API level 1
- Java: VERSION_21 with Core Library Desugaring enabled
- Desugar for Java 21 features availability on older APIs

## Project-Specific Conventions & Patterns

### Package Structure
```
com.tonyxlab.pagekeeper/
├── presentation/
│   ├── core/
│   │   ├── BaseViewModel.kt
│   │   ├── BaseContentLayout.kt
│   │   └── handling/ (UiState, UiEvent, ActionEvent interfaces)
│   ├── screens/library/
│   │   ├── LibraryScreen.kt (Compose UI)
│   │   ├── LibraryViewModel.kt
│   │   ├── handling/ (LibraryUiState, LibraryUiEvent, LibraryActionEvent)
│   │   └── components/ (reusable Composables)
│   └── theme/ (Colors, Typography, Shapes, Dimens)
├── data/
│   ├── local/
│   │   ├── PageKeeperDatabase.kt (Room)
│   │   ├── dao/ (BookDao)
│   │   ├── entity/ (BookEntity)
│   │   └── mapper/ (Entity ↔ Model conversions)
│   ├── repository/ (BookRepositoryImpl)
│   └── importer/ (BookImporter)
├── domain/
│   ├── model/ (Book)
│   ├── repository/ (BookRepository interface)
│   └── ImportBookResult.kt (sealed interface)
├── di/ (AppModule with Koin modules)
├── utils/ (Constants, AppDefaults)
└── PageKeeperApp.kt
```

### Type-Safe Events & State
- **No string-based event routing** - sealed classes (e.g., `LibraryUiEvent.OpenBook`, `LibraryUiEvent.FileSelected`) ensure compile-time safety
- **Dialog via state** - dialogs managed in state (`LibraryUiState.dialog`), not imperative calls; `LibraryDialogType` enum for type discrimination
- **One-off actions via Channel** - navigation & toasts sent as `ActionEvent` to avoid state leaks

### Coroutine Management
- All async work scoped to `viewModelScope` (auto-cancelled on ViewModel clear)
- `launchCatching()` template handles errors generically; specific errors mapped to actions/UI feedback
- `collectLatest()` used for Flow observables to cancel previous collections on new emission

### File Import Flow
1. User picks file via Activity Result Contract
2. `LibraryUiEvent.FileSelected` → validate extension → hash calculation
3. `BookImporter.importBook()` → deduplication check → internal storage copy → metadata extraction
4. Result (Success/Duplicate/Error) sent as action → UI shows toast/dialog
5. On success, DB insert triggers `observeBooks()` Flow update → UI recomposition

### Naming Conventions
- **Event classes**: `<ScreenName>UiEvent` (sealed), `<ScreenName>ActionEvent` (sealed)
- **State classes**: `<ScreenName>UiState` (data class)
- **Dialog types**: Enums co-located with state (e.g., `LibraryDialogType`)
- **Private methods in ViewModel**: Prefix `on` for event handlers (e.g., `onFinishBook`), `show` for UI updates

## Critical Integration Points

### Dependency Injection (Koin)
```kotlin
// Modules auto-discovered in PageKeeperApp
appModule = listOf(viewModelModule, databaseModule, repositoryModule, importerModule)

// Compose integration
LibraryScreen(viewModel: LibraryViewModel = koinViewModel())
```
- Add new ViewModels via `viewModelOf(::ScreenViewModel)` in `AppModule.kt`
- Add new singletons via `single { ... }` in new modules
- **Update `appModule` list** when adding modules

### Room Database Versions
- Currently **version 1**, schema not exported
- **To migrate**: Increment version, add migration strategy (or recreate for dev builds)

### Permissions & File Access
- Uses `ActivityResultContracts.GetContent()` for file picker (no manual file permissions needed)
- File copy uses `context.contentResolver.openInputStream()` → works across URI schemes
- Internal storage path: `context.filesDir/books/` (app-private, auto-backed-up)

### Timber Logging
- Initialized in `PageKeeperApp.onCreate()` - only plants `DebugTree` in DEBUG builds
- Used implicitly; leverage `Timber.d()`, `Timber.e()` for debugging
- No production log configuration currently

## Important Gotchas & Decisions

1. **Hash-based IDs**: Book ID is SHA-256 hash of file content → same file never imported twice, enables deduplication
2. **FB2-only support**: Extension validation enforced at UI & import layers; metadata extraction assumes FB2 XML structure
3. **Single screen MVP**: Currently only `LibraryScreen` + single `MainActivity` — future multi-screen work requires Navigation3 integration
4. **State-driven dialogs**: Dialogs rendered conditionally from state, not imperative show/hide — aligns with Compose best practices
5. **No error state in UiState**: Errors communicated as one-time `ActionEvent` (toast) — prevents stale error display

## Testing Strategy

- **Unit tests**: JUnit4 in `app/src/test/`, uses Kotest assertions
- **Instrumented tests**: Espresso + Compose UI testing in `app/src/androidTest/`, TestRunner is `AndroidJUnitRunner`
- **Coverage focus**: Repository logic, ViewModel state transitions, Importer business rules

### Test Boilerplate
```kotlin
// Unit test example
@Test
fun testBookImport_success() {
    // Setup: mock repository, file URI
    // Act: bookImporter.importBook(uri)
    // Assert: verify repository.insertBook() called with correct Book
}
```

## Common Tasks for AI Agents

### Adding a New Feature
1. **Define domain model** (e.g., new `Book` field) → update `BookEntity`, mappers
2. **Update Repository interface** & implementation with new queries/mutations
3. **Create screen state/events/actions** (`NewFeatureUiState`, events, actions)
4. **Implement ViewModel** extending `BaseViewModel`, use `launchCatching()` for async work
5. **Compose UI** in screen file, connect to ViewModel via `onEvent()`
6. **Update `AppModule`** if adding new singletons/ViewModels
7. **Write tests** for repository logic & ViewModel state transitions

### Debugging
- Check **Logcat** with `Timber` tags; run `./gradlew build -x test` for faster builds without tests
- **State inspection**: Add debug logs in `updateState { ... }` blocks
- **Action events**: Verify `sendActionEvent()` calls reach UI action handler in `BaseContentLayout`

### Extending Navigation
- **Current state**: Single screen app; future work needs Navigation3 integration (library already included)
- **When adding routes**: Define sealed `NavigationEvent`, send via `ActionEvent`, handle in `MainActivity`

## Creating a New Screen

When creating a new screen feature:

Create the following structure:

screens/
└── <screen-name>/
├── <ScreenName>Screen.kt
├── <ScreenName>ViewModel.kt
├── handling/
│   ├── <ScreenName>UiState.kt
│   ├── <ScreenName>UiEvent.kt
│   └── <ScreenName>ActionEvent.kt
├── components/
└── model/

Requirements:

- Follow the BaseViewModel architecture.
- Use BaseContentLayout.
- Do not create navigation.
- Do not create repository code.
- Keep implementations compile-safe.
- Add the ViewModel to AppModule when required.
- Run:

./gradlew :app:compileDebugKotlin


