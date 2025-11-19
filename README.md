# CreatureBattleSim

A simple turn-based battle simulation for the OOP PStA exam.

## Project Description

This is a text-based battle simulation where creatures fight automatically. No user input needed - just run the tests to see the battles.

### How it works

- Two teams fight: HEROES vs MONSTERS
- Each creature has health, attack, defense, and initiative
- Higher initiative goes first each round
- Different creatures do different things:
  - Warrior - can do critical hits sometimes
  - Mage - can hit multiple enemies with magic
  - Healer - heals teammates
  - MonsterBoss - gets stronger when low on health
- Battle ends when one team dies

## Why This Project?

I made this to practice OOP concepts like inheritance, interfaces, and polymorphism. The automatic battles make it easy to test without needing user input.

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
- All classes have private fields with getters
- Constructors check if values are valid
- **Algorithmic logic**: 
  - Damage calculation (attack - defense)
  - Choosing which enemy to attack
  - Deciding who to heal
  - Sorting creatures by initiative

### 4. Collections Framework ✓
- **List**: to store all creatures
- **Map**: to group creatures by team
- **Streams**: filter().sorted().collect() to find living creatures
- Used in Battlefield class methods

### 5. Comparator / Comparable ✓
- **Comparable**: Creature class sorts by initiative
- **Comparator**: CreatureComparators class has different ways to sort:
  - BY_HEALTH
  - BY_ATTACK_POWER
  - BY_DEFENSE
  - etc.
- Used for turn order in battles

### 6. Object Methods ✓
- **toString()**: Overridden in `Creature` with detailed state information
- **equals()**: Based on unique ID and name
- **hashCode()**: Consistent with equals (uses ID and name)
- **clone()**: Implements `Cloneable` for creating creature copies

### 7. Exceptions ✓
- **InvalidCreatureStateException** (checked exception)
  - Thrown when creature has bad values (negative health, empty name, etc.)
- **GameConfigurationException** (unchecked exception)
  - Thrown when game setup is wrong (duplicate names, etc.)
- Both are tested in ExceptionHandlingTest

### 8. Javadoc ✓
- Added Javadoc comments to all public classes and methods
- Used @param and @return tags

## Project Structure

```
src/
├── main/java/
│   └── de/throsenheim/psta/
│       ├── exceptions/          (2 exception classes)
│       ├── model/               (Creature, Battlefield, etc.)
│       └── util/                (Comparators)
└── test/java/
    └── de/throsenheim/psta/     (3 test classes)
```

## How to Run

You need Java 17 or newer.

```bash
# Run all tests
.\gradlew.bat test

# Just see the battles
.\gradlew.bat test --tests CreatureBattleSimTest
```

The tests will show battles in the console.

## Test Classes

1. **CreatureBattleSimTest** - runs full battle simulations
2. **CreatureLogicTest** - tests damage, healing, sorting
3. **ExceptionHandlingTest** - tests error handling

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

## Classes and Relationships

**Main Classes:**
- `Creature` (abstract) - base class for all creatures
  - `Warrior`, `Mage`, `Healer`, `MonsterBoss` extend Creature
- `ActionPerRound` (interface) - all creatures implement this
- `Battlefield` - runs the game, manages creatures
- `Team` (enum) - HEROES or MONSTERS
- `CreatureComparators` - for sorting creatures

**Relationships:**
- Warrior/Mage/Healer/MonsterBoss inherit from Creature
- All creatures implement ActionPerRound interface  
- Battlefield uses List and Map to store creatures
- Creature implements Comparable for sorting by initiative

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
