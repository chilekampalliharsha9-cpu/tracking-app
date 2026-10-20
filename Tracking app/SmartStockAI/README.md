# SmartStock AI — Phase 1 and Phase 2

This project includes the initial Phase 1 dashboard and the Phase 2 local Room database and product management implementation.

## Prerequisites on Windows

1. Install Android Studio Ladybug or newer.
2. Install Android SDK Platform 34 and Android SDK Build-Tools 34.
3. Install JDK 17.
4. Ensure `JAVA_HOME` is configured.
5. Ensure Android Studio can see the SDK path.

## Open the project

- Open Android Studio.
- Choose Open an existing project.
- Select: `C:\Users\HARSHA REDDY\OneDrive\Documents\Tracking app\SmartStockAI`
- Let Gradle sync complete.

## Main deliverables

Phase 1:
- Premium dashboard UI with a dark navy bottom navigation
- Material 3-based theme
- KPI card widgets and AI recommendations
- Working navigation shell

Phase 2:
- Room database with `products` table
- DAO and repository setup
- Product creation, listing, search, and archive flow
- Persisted inventory data between app restarts
- Product screen in the bottom navigation

Phase 3:
- Camera permission handling for scanner access
- Barcode/QR scanning using ML Kit
- Manual fallback barcode entry
- Scanner screen integrated into the bottom navigation

Phase 4:
- Stock-in and stock-out movement processing
- Transaction history persistence
- Quantity validation and negative-stock prevention
- Stock movement screen in the bottom navigation for real inventory updates

## Files created

- `settings.gradle.kts`
- `build.gradle.kts`
- `app/build.gradle.kts`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/example/smartstockai/MainActivity.kt`
- `app/src/main/java/com/example/smartstockai/data/local/ProductEntity.kt`
- `app/src/main/java/com/example/smartstockai/data/local/ProductDao.kt`
- `app/src/main/java/com/example/smartstockai/data/local/SmartStockDatabase.kt`
- `app/src/main/java/com/example/smartstockai/data/repository/ProductRepository.kt`
- `app/src/main/java/com/example/smartstockai/ui/products/ProductViewModel.kt`
- `app/src/main/java/com/example/smartstockai/ui/products/ProductScreen.kt`
- `app/src/main/res/values/colors.xml`
- `app/src/main/res/values/themes.xml`

## Run the app

1. Open the project in Android Studio.
2. Wait for Gradle sync to finish.
3. Select a connected Android device or emulator.
4. Click Run > Run 'app'.
5. Verify the dashboard appears and the Products screen works with add/search/archive actions.

## Expected output

The app launches with:

- SmartStock AI header
- KPI cards
- AI recommendations panel
- Bottom navigation
- Products screen where products can be added, searched, and archived

## Current status

Phase 1 and Phase 2 are implemented as project foundation and persistent product management.
