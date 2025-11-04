# GitHub Copilot Instructions for ReiProHax

## Project Overview

ReiProHax is an Android application built with:
- **Java** - Primary application logic and utilities
- **Kotlin** - Modern Android components and features (Kotlin 2.2.0)
- **C/C++** - Native code via JNI for performance-critical operations
- **Gradle** - Build system (version 8.14.2+)

The project targets Android SDK 36 with minimum SDK requirements defined in the app module.

## Project Structure

```
.
├── app/
│   ├── src/main/
│   │   ├── java/com/happy/pro/    # Java source files
│   │   ├── kotlin/                 # Kotlin source files
│   │   └── jni/                    # C/C++ native code
│   ├── build.gradle                # App-level Gradle configuration
│   └── proguard-rules.pro          # ProGuard configuration
├── build.gradle                    # Root-level Gradle configuration
├── gradle.properties               # Gradle build properties
└── .github/workflows/              # CI/CD workflows
```

## Build & Test

### Prerequisites
- JDK 17 (Temurin distribution recommended)
- Android SDK 36
- NDK version 27.1.12297006
- Gradle 8.14.2 or higher

### Building the Project

1. **Grant execute permissions** (if needed):
   ```bash
   chmod +x gradlew
   ```

2. **Build the project**:
   ```bash
   ./gradlew build
   ```

3. **Clean build**:
   ```bash
   ./gradlew clean build
   ```

4. **Build specific variants**:
   ```bash
   ./gradlew assembleDebug      # Debug build
   ./gradlew assembleRelease    # Release build
   ```

### Testing

Run tests with:
```bash
./gradlew test
./gradlew connectedAndroidTest  # For instrumented tests
```

### Linting

The project includes Android Lint with a baseline configuration:
```bash
./gradlew lint
./gradlew lintDebug
```

## Coding Conventions

### General Guidelines
- Follow the Kotlin official code style (`kotlin.code.style=official`)
- Use AndroidX libraries (no support libraries)
- Enable R8/ProGuard for release builds

### Java Code
- Use meaningful variable and method names
- Follow standard Java naming conventions (camelCase for methods/variables, PascalCase for classes)
- Add JavaDoc comments for public APIs
- Handle exceptions appropriately

### Kotlin Code
- Follow Kotlin coding conventions
- Use data classes for simple data holders
- Prefer immutability (val over var)
- Use Kotlin null-safety features
- Utilize Kotlin extension functions appropriately

### Native C/C++ Code
- Follow project's obfuscation patterns when present
- Ensure proper JNI method signatures
- Handle memory management carefully
- Include appropriate headers

### File Organization
- Keep related classes in appropriate packages
- Separate utility classes in `utils/` package
- Use clear, descriptive file names

## Security & Signing

### Release Signing
The project uses environment variables for secure signing:
- `BEARMOD_KEYSTORE_PATH` - Path to keystore file
- `BEARMOD_KEYSTORE_PASSWORD` - Keystore password
- `BEARMOD_KEY_ALIAS` - Key alias
- `BEARMOD_KEY_PASSWORD` - Key password

**Important**: Never commit keystore files, passwords, or credentials to the repository.

### ProGuard/R8
- Release builds use ProGuard/R8 for code obfuscation
- Configuration is in `app/proguard-rules.pro`
- Test thoroughly after ProGuard changes

## Dependencies

### Adding Dependencies
1. Check if the dependency is necessary and well-maintained
2. Add to `app/build.gradle` in the appropriate section
3. Sync Gradle and verify the build still works
4. Update documentation if adding major dependencies

### Repository Configuration
- Primary repositories: Google Maven and Maven Central
- Dependencies should be added to module-level `build.gradle`

## Pull Requests

### Before Submitting
1. Ensure the build succeeds: `./gradlew build`
2. Run lint checks: `./gradlew lint`
3. Run tests: `./gradlew test`
4. Check for ProGuard/R8 issues if modifying release configuration
5. Verify the app runs on debug and release builds

### PR Guidelines
- Reference the related issue number in PR title or description
- Provide clear description of changes
- Include any relevant testing performed
- Update documentation if behavior changes

## CI/CD

The project uses GitHub Actions for continuous integration:
- **Workflow**: `.github/workflows/android.yml`
- **Triggers**: Push to `main` branch and pull requests
- **Steps**: Checkout, setup JDK 17, cache Gradle, build

## Common Issues

### Gradle Build Failures
- Ensure you have proper internet connectivity for dependency downloads
- Clear Gradle cache if needed: `./gradlew clean --refresh-dependencies`
- Check that environment variables for signing are set (for release builds)

### NDK Issues
- Ensure NDK version 27.1.12297006 is installed
- Check CMakeLists.txt or Android.mk configuration if present

### Memory Issues
- Gradle is configured with increased heap size (see `gradle.properties`)
- If builds fail with OOM, adjust `org.gradle.jvmargs`

## Package Information

- **Package Name**: `com.happy.pro`
- **Version Code**: 100
- **Version Name**: 3.0.0

## Additional Notes

- The project uses non-transitive R classes (`android.nonTransitiveRClass=true`)
- Configuration cache is disabled (`org.gradle.configuration-cache=false`)
- Legacy transforms are not forced to be incremental
- The project settings are in `settings.gradle`
