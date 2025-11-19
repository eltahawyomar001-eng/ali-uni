# Quick Start Guide

## Running the Project

This project requires **Java 17 or higher**. You do NOT need to install Gradle separately.

### Step 1: Verify Java Installation

Open a terminal/command prompt and run:

```bash
java -version
```

You should see Java 17 or higher. If not, download and install Java from:
- https://adoptium.net/ (recommended)
- https://www.oracle.com/java/technologies/downloads/

### Step 2: Run the Tests

#### On Windows (PowerShell or CMD):

```bash
.\gradlew.bat test
```

#### On Linux/Mac:

```bash
chmod +x gradlew
./gradlew test
```

### What You'll See

The tests will:
1. Compile all Java source files
2. Run three test classes with multiple test cases
3. Display battle simulations in the console
4. Show test results (passed/failed)

Example output:
```
======================================================================
BATTLE BEGINS!
======================================================================

TEAM HEROES:
  - Aragorn (Warrior) - HP: 120, ATK: 25, DEF: 8, INI: 6
  - Gandalf (Mage) - HP: 80, ATK: 35, DEF: 3, INI: 7
  ...

[Test results will show at the end]
BUILD SUCCESSFUL in 5s
3 actionable tasks: 3 executed
```

### Troubleshooting

**Problem**: `java: command not found` or `JAVA_HOME is not set`

**Solution**: Install Java 17+ and set JAVA_HOME environment variable

**Windows**:
1. Install Java
2. Set JAVA_HOME to your Java installation directory (e.g., `C:\Program Files\Java\jdk-17`)
3. Add `%JAVA_HOME%\bin` to your PATH

**Linux/Mac**:
1. Install Java
2. Add to ~/.bashrc or ~/.zshrc:
   ```bash
   export JAVA_HOME=/path/to/java
   export PATH=$JAVA_HOME/bin:$PATH
   ```

### Running Specific Tests

To run only one test class:

```bash
.\gradlew.bat test --tests CreatureBattleSimTest        # Windows
./gradlew test --tests CreatureBattleSimTest            # Linux/Mac
```

### Viewing Test Reports

After running tests, open:
```
build/reports/tests/test/index.html
```

This shows a detailed HTML report of all test results.

### Project Structure Quick Reference

- **Source code**: `src/main/java/de/throsenheim/psta/`
- **Tests**: `src/test/java/de/throsenheim/psta/`
- **Build output**: `build/`
- **Test reports**: `build/reports/tests/test/`

### For CI/CD

```bash
./gradlew clean test --no-daemon
```

This runs a clean build suitable for continuous integration environments.
