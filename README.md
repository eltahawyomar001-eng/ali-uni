# CreatureBattleSim

PStA exam project - automatic battle simulation

## What it does

Heroes fight Monsters. Runs automatically in terminal. No GUI, no user input.

## OOP Requirements

### 1. Inheritance
- Abstract class: `Creature`
- Subclasses: `Warrior`, `Mage`, `Healer`, `MonsterBoss`

### 2. Interface
- `ActionPerRound` interface
- All creatures implement it

### 3. Polymorphism
- Each creature overrides `calculateDamage()` and `performRoundAction()` differently

### 4. Collections
- `ArrayList<Creature>` stores all creatures
- `HashMap<Team, List<Creature>>` organizes by team
- Stream API used for filtering

### 5. Comparable/Comparator
- `Creature` implements `Comparable<Creature>` (sorts by initiative)
- Comparator in tests for sorting by health/attack

### 6. Exceptions
- Checked: `InvalidCreatureStateException`
- Unchecked: `GameConfigurationException`

### 7. Javadoc
- All classes documented

### 8. JUnit Tests
- 40 tests in 3 test classes

## Run Tests

```bash
gradlew.bat test
```

## Structure

```
src/main/java/
  ├── model/
  │   ├── Creature.java (abstract)
  │   ├── ActionPerRound.java (interface)
  │   ├── Team.java (enum)
  │   ├── Battlefield.java
  │   └── creatures/
  │       ├── Warrior.java
  │       ├── Mage.java
  │       ├── Healer.java
  │       └── MonsterBoss.java
  └── exceptions/
      ├── InvalidCreatureStateException.java
      └── GameConfigurationException.java
```
