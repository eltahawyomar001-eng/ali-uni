# Installation & Setup Guide

## Prerequisites

### Required Software

1. **Java Development Kit (JDK) 17 or higher**
   - Download from: https://adoptium.net/temurin/releases/
   - Select: Java 17 (LTS) or Java 21 (LTS)
   - Choose your operating system (Windows, macOS, Linux)
   - Install using the installer

2. **Verify Installation**
   
   After installing Java, open a new terminal/command prompt and run:
   ```bash
   java -version
   ```
   
   Expected output:
   ```
   openjdk version "17.0.x" 2023-xx-xx
   OpenJDK Runtime Environment Temurin-17.0.x (build ...)
   OpenJDK 64-Bit Server VM Temurin-17.0.x (build ...)
   ```

### Optional (Gradle is included via wrapper)

- Gradle is NOT required to be installed
- The project includes Gradle Wrapper (`gradlew` and `gradlew.bat`)
- The wrapper will automatically download the correct Gradle version

## Project Setup

### 1. Extract/Clone the Project

Ensure your project directory looks like this:
```
CreatureBattleSim/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── README.md
├── QUICKSTART.md
├── .gitignore
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
└── src/
    ├── main/
    │   └── java/
    │       └── de/throsenheim/psta/
    │           ├── exceptions/
    │           ├── model/
    │           └── util/
    └── test/
        └── java/
            └── de/throsenheim/psta/
```

### 2. Set JAVA_HOME (if needed)

#### Windows

1. Open System Properties → Environment Variables
2. Add new System Variable:
   - Variable name: `JAVA_HOME`
   - Variable value: Path to your JDK (e.g., `C:\Program Files\Eclipse Adoptium\jdk-17.0.x`)
3. Edit PATH variable and add: `%JAVA_HOME%\bin`
4. Restart your terminal

#### Linux/Mac

Add to `~/.bashrc` or `~/.zshrc`:
```bash
export JAVA_HOME=/path/to/jdk-17
export PATH=$JAVA_HOME/bin:$PATH
```

Then run:
```bash
source ~/.bashrc  # or source ~/.zshrc
```

### 3. Make Gradle Wrapper Executable (Linux/Mac only)

```bash
chmod +x gradlew
```

## Running the Project

### Run All Tests

**Windows (PowerShell or CMD)**:
```bash
cd "e:\Ali Uni"
.\gradlew.bat test
```

**Linux/Mac**:
```bash
cd /path/to/CreatureBattleSim
./gradlew test
```

### First Run

The first time you run the project, Gradle will:
1. Download Gradle distribution (~100 MB)
2. Download JUnit 5 dependencies (~2 MB)
3. Compile all Java source files
4. Run all tests

This may take 1-2 minutes. Subsequent runs will be much faster.

### Expected Output

You should see:
```
> Task :compileJava
> Task :processResources NO-SOURCE
> Task :classes
> Task :compileTestJava
> Task :processTestResources NO-SOURCE
> Task :testClasses
> Task :test

======================================================================
BATTLE BEGINS!
======================================================================
...
[Battle simulation output]
...

BUILD SUCCESSFUL in 10s
3 actionable tasks: 3 executed
```

### Test Results

After running tests, you can view detailed results:
```
build/reports/tests/test/index.html
```

Open this file in a web browser to see:
- Number of tests passed/failed
- Execution time
- Console output from each test
- Stack traces for any failures

## Development

### Opening in IDE

#### IntelliJ IDEA
1. File → Open
2. Select the project folder (containing `build.gradle`)
3. IntelliJ will automatically detect it as a Gradle project
4. Wait for Gradle sync to complete
5. Run tests from the IDE: Right-click test class → Run

#### Eclipse
1. File → Import → Gradle → Existing Gradle Project
2. Select project directory
3. Finish
4. Wait for build to complete
5. Run tests: Right-click test class → Run As → JUnit Test

#### VS Code
1. Install "Extension Pack for Java" extension
2. Open project folder
3. VS Code will detect Java project automatically
4. Run tests from Test Explorer sidebar

### Gradle Commands

```bash
# Clean build directory
.\gradlew.bat clean          # Windows
./gradlew clean              # Linux/Mac

# Compile source code only
.\gradlew.bat compileJava
./gradlew compileJava

# Run specific test class
.\gradlew.bat test --tests CreatureBattleSimTest
./gradlew test --tests CreatureBattleSimTest

# Run tests with more output
.\gradlew.bat test --info
./gradlew test --info

# Clean and test
.\gradlew.bat clean test
./gradlew clean test
```

## Troubleshooting

### Problem: "JAVA_HOME is not set"

**Solution**: Follow "Set JAVA_HOME" section above

### Problem: "Could not find or load main class"

**Solution**: This project has no main class (it's test-only). Run tests instead:
```bash
.\gradlew.bat test
```

### Problem: "Unsupported class file major version"

**Solution**: You're using Java 16 or lower. Install Java 17 or higher.

### Problem: "Permission denied" (Linux/Mac)

**Solution**: Make gradlew executable:
```bash
chmod +x gradlew
```

### Problem: Tests fail to run

**Solution**: 
1. Ensure Java 17+ is installed
2. Run `.\gradlew.bat clean test`
3. Check `build/reports/tests/test/index.html` for details

### Problem: "Cannot connect to daemon"

**Solution**: Run with `--no-daemon`:
```bash
.\gradlew.bat test --no-daemon
```

## For the Examiner

To verify the project works correctly:

1. Install Java 17 or higher
2. Open terminal in project root
3. Run: `.\gradlew.bat test` (Windows) or `./gradlew test` (Linux/Mac)
4. All tests should pass (green)
5. Check console output for battle simulations
6. Open `build/reports/tests/test/index.html` for detailed test report

The project demonstrates:
- ✅ Inheritance & Abstract classes
- ✅ Interfaces & Polymorphism  
- ✅ Collections (List, Map, Streams)
- ✅ Comparator & Comparable
- ✅ Object methods (equals, hashCode, toString, clone)
- ✅ Custom exceptions (checked & unchecked)
- ✅ Javadoc documentation
- ✅ JUnit 5 tests
- ✅ Gradle build system
- ✅ No GUI, no user input (automatic simulation)

## Contact & Support

For issues or questions about this project, refer to:
- README.md - Full project documentation
- QUICKSTART.md - Quick start guide
- Source code comments - Detailed inline documentation
- Javadoc - Generate with `.\gradlew.bat javadoc`
