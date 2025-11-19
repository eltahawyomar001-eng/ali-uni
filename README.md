# CreatureBattleSim

A turn-based automatic battle simulation game for the OOP PStA exam at Technische Hochschule Rosenheim.

## Project Description

**CreatureBattleSim** is a text-based, automatic battle simulation where different types of creatures fight in teams. The game runs entirely in the terminal with no user input required - it's a fully automated simulation that demonstrates various object-oriented programming concepts.

### Game Mechanics

- Two teams (HEROES vs MONSTERS) battle automatically
- Each creature has unique attributes: health, attack power, defense, and initiative
- Creatures act in initiative order each round (higher initiative acts first)
- Different creature types have special abilities:
  - **Warrior**: Can deal critical hits (25% chance for 1.5x damage)
  - **Mage**: Can cast area-of-effect spells hitting multiple enemies
  - **Healer**: Heals wounded allies or attacks when no healing is needed
  - **MonsterBoss**: Enrages when health drops below 50%, dealing increased damage
- Battle ends when one team is eliminated or maximum rounds are reached

## Application Scenario

This simulation can model:
- RPG combat systems
- AI behavior in turn-based strategy games
- Automated game balance testing
- Teaching tool for OOP concepts in game development

The automatic nature allows for reproducible testing and analysis of different team compositions and strategies.

## Technical Requirements Fulfillment

This project satisfies all mandatory OOP requirements for the PStA exam:

### 1. Inheritance & Abstract Class ✓
- **Abstract class**: `Creature` (in `de.throsenheim.psta.model.Creature`)
- **Concrete subclasses**: 
  - `Warrior` - melee fighter with critical hits
  - `Mage` - magical attacker with AOE abilities
  - `Healer` - support class with healing
  - `MonsterBoss` - boss enemy with enrage mechanic
- Each subclass overrides `calculateDamage()` and `performRoundAction()` with unique behavior

### 2. Interface & Polymorphism ✓
- **Interface**: `ActionPerRound` (in `de.throsenheim.psta.model.ActionPerRound`)
- All creatures implement `performRoundAction(Battlefield battlefield)`
- Polymorphism demonstrated: The `Battlefield` treats all creatures through the `Creature` reference and calls overridden methods

### 3. Attributes, Constructors, Getters/Setters ✓
- All classes have proper encapsulation with private fields
- Constructors with validation
- Getter methods for all attributes
- **Algorithmic logic**: 
  - Damage calculation with defense reduction
  - Target selection algorithms (weakest enemy, highest health, lowest defense)
  - Healing priority based on health percentage
  - Turn order based on initiative sorting

### 4. Collections Framework ✓
- **List**: `allCreatures` stores all creatures in battle
- **Map**: `teamMap` groups creatures by team (HashMap<Team, List<Creature>>)
- **Stream operations**: Filtering living creatures, finding targets, calculating team health
- Demonstrated in `Battlefield.getEnemies()`, `getAllies()`, `executeRound()`, etc.

### 5. Comparator / Comparable ✓
- **Comparable**: `Creature` implements `Comparable<Creature>` for natural ordering by initiative
- **Comparator**: `CreatureComparators` utility class provides multiple comparators:
  - `BY_HEALTH` - sorts by health (ascending)
  - `BY_ATTACK_POWER` - sorts by attack power (descending)
  - `BY_DEFENSE` - sorts by defense
  - `BY_INITIATIVE` - sorts by initiative
  - `BY_NAME` - alphabetical sorting
  - `createPriorityComparator()` - composite comparator
- Used in `Battlefield.executeRound()` for turn order and in tests

### 6. Object Methods ✓
- **toString()**: Overridden in `Creature` with detailed state information
- **equals()**: Based on unique ID and name
- **hashCode()**: Consistent with equals (uses ID and name)
- **clone()**: Implements `Cloneable` for creating creature copies

### 7. Exceptions ✓
- **Custom checked exception**: `InvalidCreatureStateException` 
  - Thrown when creating creatures with invalid attributes (negative health, null name, etc.)
  - Must be caught or declared in method signatures
- **Custom unchecked exception**: `GameConfigurationException`
  - Thrown for game setup errors (duplicate names, invalid battlefield config)
  - RuntimeException subclass
- Both exceptions used meaningfully throughout the code with proper handling

### 8. Javadoc ✓
- All public classes have Javadoc comments
- All public methods documented with `@param` and `@return` tags
- Explains purpose, parameters, and behavior

## Project Structure

```
CreatureBattleSim/
├── build.gradle                     # Gradle build configuration
├── settings.gradle                  # Gradle settings
├── README.md                        # This file
├── src/
│   ├── main/
│   │   └── java/
│   │       └── de/throsenheim/psta/
│   │           ├── exceptions/
│   │           │   ├── InvalidCreatureStateException.java    # Custom checked exception
│   │           │   └── GameConfigurationException.java       # Custom unchecked exception
│   │           ├── model/
│   │           │   ├── ActionPerRound.java                   # Interface for round actions
│   │           │   ├── Team.java                             # Enum for team affiliation
│   │           │   ├── Creature.java                         # Abstract base class
│   │           │   ├── Battlefield.java                      # Game engine
│   │           │   └── creatures/
│   │           │       ├── Warrior.java                      # Concrete: Warrior class
│   │           │       ├── Mage.java                         # Concrete: Mage class
│   │           │       ├── Healer.java                       # Concrete: Healer class
│   │           │       └── MonsterBoss.java                  # Concrete: Boss class
│   │           └── util/
│   │               └── CreatureComparators.java              # Comparator implementations
│   └── test/
│       └── java/
│           └── de/throsenheim/psta/
│               ├── CreatureBattleSimTest.java                # Full simulation tests
│               ├── CreatureLogicTest.java                    # Business logic tests
│               └── ExceptionHandlingTest.java                # Exception tests
```

## How to Run

### Prerequisites
- Java 17 or higher
- Gradle (or use the Gradle wrapper included)

### Running Tests

The project has **no main method** and **no user input**. All functionality is demonstrated through JUnit tests.

To run all tests:

```bash
gradle test
```

Or with the Gradle wrapper:

```bash
./gradlew test        # Linux/Mac
gradlew.bat test      # Windows
```

### Test Classes

1. **CreatureBattleSimTest** - Full game simulation tests
   - Runs complete battles from start to finish
   - Tests different team compositions
   - Verifies winner determination
   - Tests special mechanics (healer effectiveness, boss enrage)

2. **CreatureLogicTest** - Business logic tests
   - Damage calculation and defense mechanics
   - Healing functionality
   - Comparator and Comparable sorting
   - equals(), hashCode(), clone(), toString()
   - Special abilities (critical hits, AOE, enrage)

3. **ExceptionHandlingTest** - Exception handling tests
   - Invalid creature creation scenarios
   - Invalid battlefield configuration
   - Proper exception throwing and catching
   - Validates both checked and unchecked exceptions

### Example Test Output

When you run the tests, you'll see battle simulation output like:

```
======================================================================
BATTLE BEGINS!
======================================================================

TEAM HEROES:
  - Aragorn (Warrior) - HP: 120, ATK: 25, DEF: 8, INI: 6
  - Gandalf (Mage) - HP: 80, ATK: 35, DEF: 3, INI: 7
  - Elrond (Healer) - HP: 90, ATK: 15, DEF: 5, INI: 5

TEAM MONSTERS:
  - Dark Lord (MonsterBoss) - HP: 200, ATK: 30, DEF: 10, INI: 4
  - Orc Chieftain (Warrior) - HP: 100, ATK: 20, DEF: 6, INI: 3

======================================================================
ROUND 1
======================================================================

[Gandalf's turn]
Gandalf casts FIREBALL (AOE)!
  -> Dark Lord takes 21 fire damage!
  -> Orc Chieftain takes 21 fire damage!

[Aragorn's turn]
Aragorn attacks Orc Chieftain for 18 damage!

...
```

## UML Class Diagram Overview

### Main Classes and Relationships:

1. **Creature (abstract)**
   - Implements: ActionPerRound, Comparable<Creature>, Cloneable
   - Subclasses: Warrior, Mage, Healer, MonsterBoss
   - Has attributes: id, name, health, maxHealth, attackPower, defense, initiative, team, alive

2. **Warrior extends Creature**
   - Has: Random (for critical hits)
   - Overrides: calculateDamage(), performRoundAction()

3. **Mage extends Creature**
   - Has: Random, manaPool
   - Overrides: calculateDamage(), performRoundAction()
   - Special: AOE attacks

4. **Healer extends Creature**
   - Has: Random, healPower
   - Overrides: calculateDamage(), performRoundAction()
   - Special: Healing abilities

5. **MonsterBoss extends Creature**
   - Has: Random, enraged
   - Overrides: calculateDamage(), performRoundAction()
   - Special: Enrage mechanic

6. **Battlefield (game engine)**
   - Has: List<Creature>, Map<Team, List<Creature>>
   - Manages: Round execution, battle logic, winner determination
   - Methods: startBattle(), executeRound(), getEnemies(), getAllies()

7. **ActionPerRound (interface)**
   - Method: performRoundAction(Battlefield)

8. **Team (enum)**
   - Values: HEROES, MONSTERS

9. **CreatureComparators (utility)**
   - Static comparators for sorting creatures

10. **Exceptions**
    - InvalidCreatureStateException (checked)
    - GameConfigurationException (unchecked)

### Key Relationships:
- Inheritance: Warrior, Mage, Healer, MonsterBoss ← Creature
- Interface implementation: Creature implements ActionPerRound
- Composition: Battlefield has-many Creatures
- Association: Creature uses Battlefield for context
- Dependency: All creatures depend on Team enum

## CI/CD Compatibility

This project is designed to work in CI/CD pipelines:
- Uses Gradle for reproducible builds
- All tests are automated with JUnit 5
- No external dependencies beyond JUnit
- No user input or GUI required
- Tests use fixed random seeds for reproducibility
- UTF-8 encoding configured

To run in CI:
```bash
gradle clean test --no-daemon
```

## Design Decisions

1. **No user input**: Satisfies exam requirement for automatic simulation
2. **Fixed random seeds in tests**: Ensures reproducible test results
3. **Interface + Abstract class**: Demonstrates both OOP concepts
4. **Collections variety**: Uses List, Map, and Set operations through streams
5. **Both exception types**: Shows understanding of checked vs unchecked exceptions
6. **Meaningful inheritance**: Each subclass has genuinely different behavior, not just different values

## Author

Created for the OOP PStA exam at Technische Hochschule Rosenheim.

## License

Educational project - for exam purposes.
