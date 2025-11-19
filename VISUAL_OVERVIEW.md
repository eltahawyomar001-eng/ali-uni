# CreatureBattleSim - Visual Project Overview

## 🎮 What Does This Project Do?

This is an **automatic turn-based battle simulation** where teams of creatures fight each other. It runs in the terminal and requires **no user input** - perfect for automated testing and exam requirements!

```
┌─────────────────────────────────────────────────────────────┐
│                    BATTLE SIMULATION                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  TEAM HEROES              vs            TEAM MONSTERS       │
│  ━━━━━━━━━━━━                          ━━━━━━━━━━━━━━       │
│                                                             │
│  ⚔️  Warrior              💀            MonsterBoss         │
│  🧙 Mage                   👹            Orc Warrior        │
│  ❤️  Healer                                                 │
│                                                             │
│  Turn Order: Initiative (Highest → Lowest)                  │
│  Actions: Attack, Heal, Special Abilities                   │
│  Win Condition: Eliminate enemy team                        │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## 📊 Class Structure (UML-like)

```
                    ┌──────────────────┐
                    │  ActionPerRound  │ (Interface)
                    │  <<interface>>   │
                    ├──────────────────┤
                    │ + performRound   │
                    │   Action()       │
                    └────────▲─────────┘
                             │
                             │ implements
                             │
          ┌──────────────────┴──────────────────┐
          │         Creature (Abstract)         │
          │  ◆ Comparable<Creature>             │
          │  ◆ Cloneable                        │
          ├─────────────────────────────────────┤
          │ - id: int                           │
          │ - name: String                      │
          │ - health: int                       │
          │ - attackPower: int                  │
          │ - defense: int                      │
          │ - initiative: int                   │
          │ - team: Team                        │
          │ - alive: boolean                    │
          ├─────────────────────────────────────┤
          │ + attack(Creature)                  │
          │ + takeDamage(int)                   │
          │ + heal(int)                         │
          │ # calculateDamage(Creature): int    │ (abstract)
          │ + performRoundAction(Battlefield)   │ (abstract)
          │ + compareTo(Creature): int          │
          │ + equals(Object): boolean           │
          │ + hashCode(): int                   │
          │ + toString(): String                │
          │ + clone(): Creature                 │
          └──────────────┬──────────────────────┘
                         │
                         │ extends
         ┌───────────────┼───────────────┬──────────────┐
         │               │               │              │
         │               │               │              │
    ┌────▼────┐    ┌────▼────┐    ┌────▼────┐   ┌─────▼──────┐
    │ Warrior │    │  Mage   │    │ Healer  │   │ MonsterBoss│
    ├─────────┤    ├─────────┤    ├─────────┤   ├────────────┤
    │Critical │    │AOE      │    │Healing  │   │Enrage      │
    │Hits     │    │Attacks  │    │Priority │   │Mechanic    │
    │25% crit │    │Mana:3   │    │healPower│   │@50% HP     │
    │1.5x dmg │    │Hits 3   │    │Targets  │   │1.5x damage │
    └─────────┘    └─────────┘    └─────────┘   └────────────┘
```

```
    ┌─────────────────────────────────────────┐
    │         Battlefield (Game Engine)       │
    ├─────────────────────────────────────────┤
    │ - allCreatures: List<Creature>          │
    │ - teamMap: Map<Team, List<Creature>>    │
    │ - maxRounds: int                        │
    │ - currentRound: int                     │
    │ - winnerTeam: Team                      │
    ├─────────────────────────────────────────┤
    │ + addCreature(Creature)                 │
    │ + startBattle()                         │
    │ - executeRound()                        │
    │ - isBattleOver(): boolean               │
    │ + getEnemies(Creature): List<Creature>  │
    │ + getAllies(Creature): List<Creature>   │
    └─────────────────────────────────────────┘
```

## 🔧 How It Works - Battle Flow

```
START
  ↓
Add Creatures to Battlefield
  ↓
Start Battle
  ↓
┌─────────── ROUND LOOP ───────────┐
│                                  │
│ 1. Get all living creatures      │
│                                  │
│ 2. Sort by initiative            │
│    (Comparable.compareTo)        │
│                                  │
│ 3. Each creature performs action │
│    (polymorphic call)            │
│    ┌──────────────────────────┐  │
│    │ Warrior → Attack weakest │  │
│    │ Mage → AOE or single     │  │
│    │ Healer → Heal or attack  │  │
│    │ Boss → Target low defense│  │
│    └──────────────────────────┘  │
│                                  │
│ 4. Update health/status          │
│                                  │
│ 5. Check win condition           │
│                                  │
└──────────────────────────────────┘
  ↓
  │ Battle Over? (One team dead   
  │ or max rounds reached)
  ↓
Determine Winner
  ↓
Print Results
  ↓
END
```

## 🧪 Test Structure

```
┌────────────────────────────────────────────────────┐
│              TEST CLASSES (3)                      │
├────────────────────────────────────────────────────┤
│                                                    │
│  1️⃣  CreatureBattleSimTest (6 tests)              │
│     ├─ Full battle simulation                     │
│     ├─ Heroes win scenario                        │
│     ├─ Balanced battle                            │
│     ├─ Healer effectiveness                       │
│     ├─ Boss enrage mechanic                       │
│     └─ Initiative ordering                        │
│                                                    │
│  2️⃣  CreatureLogicTest (17 tests)                 │
│     ├─ Damage calculation                         │
│     ├─ Defense mechanics                          │
│     ├─ Healing functionality                      │
│     ├─ Comparator sorting                         │
│     ├─ equals/hashCode/toString/clone             │
│     ├─ Special abilities                          │
│     └─ Health percentage                          │
│                                                    │
│  3️⃣  ExceptionHandlingTest (17 tests)             │
│     ├─ Invalid creature states (8 tests)          │
│     ├─ Invalid game config (6 tests)              │
│     └─ Exception type verification (3 tests)      │
│                                                    │
│  TOTAL: 40 Test Methods                           │
└────────────────────────────────────────────────────┘
```

## 📋 OOP Requirements Mapping

```
┌────────────────────────────────────────────────────────┐
│  REQUIREMENT           │  IMPLEMENTATION               │
├────────────────────────┼───────────────────────────────┤
│ Abstract Class         │  Creature.java                │
│ Inheritance (3+ sub)   │  4 subclasses (Warrior, etc.) │
│ Interface              │  ActionPerRound.java          │
│ Polymorphism           │  performRoundAction() calls   │
│ Collections            │  List, Map in Battlefield     │
│ Comparable             │  Creature.compareTo()         │
│ Comparator             │  CreatureComparators class    │
│ toString()             │  Creature.toString()          │
│ equals()               │  Creature.equals()            │
│ hashCode()             │  Creature.hashCode()          │
│ clone()                │  Creature.clone()             │
│ Custom Exception (✓)   │  InvalidCreatureStateException│
│ Custom Exception (RE)  │  GameConfigurationException   │
│ Javadoc                │  All public APIs documented   │
│ JUnit Tests            │  3 test classes, 40 tests     │
│ No GUI                 │  Console output only          │
│ Algorithms             │  Combat, targeting, healing   │
└────────────────────────────────────────────────────────┘

✓ = Checked Exception
RE = Runtime Exception (Unchecked)
```

## 🎯 Key Features

### Creature Types & Special Abilities

```
⚔️  WARRIOR
    • Critical Hits (25% chance)
    • 1.5x damage on crit
    • Targets weakest enemy

🧙 MAGE
    • Area of Effect attacks
    • Hits up to 3 enemies
    • 3 mana charges
    • 60% damage per target

❤️  HEALER
    • Heals allies below 60% HP
    • Prioritizes most wounded
    • Attacks if no healing needed
    • Heal power: configurable

💀 MONSTER BOSS
    • Enrages at 50% HP
    • 1.5x damage when enraged
    • Targets low-defense enemies
    • High health pool
```

### Collections in Action

```java
// List usage
List<Creature> allCreatures = new ArrayList<>();

// Map usage  
Map<Team, List<Creature>> teamMap = new HashMap<>();
teamMap.put(Team.HEROES, new ArrayList<>());
teamMap.put(Team.MONSTERS, new ArrayList<>());

// Stream operations
creatures.stream()
    .filter(Creature::isAlive)
    .sorted()  // Uses Comparable
    .forEach(c -> c.performRoundAction(battlefield));
```

### Exception Handling

```java
// Checked Exception - Must handle
try {
    Creature c = new Warrior("Hero", -10, 20, 5, 6, Team.HEROES, random);
} catch (InvalidCreatureStateException e) {
    // Handle invalid creature state
}

// Unchecked Exception - Runtime
Battlefield bf = new Battlefield(-5); 
// Throws GameConfigurationException
```

## 📈 Project Statistics

```
┌─────────────────────────────────────┐
│ Production Code:  ~1,400 lines      │
│ Test Code:        ~700 lines        │
│ Documentation:    ~500 lines        │
│ Total Files:      22 files          │
│ Java Classes:     14 classes        │
│ Test Methods:     40 tests          │
│ Dependencies:     JUnit 5 only      │
└─────────────────────────────────────┘
```

## 🚀 Quick Run Commands

```bash
# Compile everything
.\gradlew.bat build

# Run all tests
.\gradlew.bat test

# Clean and test
.\gradlew.bat clean test

# View test report
# Open: build/reports/tests/test/index.html
```

## 🎓 Perfect for PStA Exam Because...

✅ **No user input** - Runs automatically  
✅ **Comprehensive OOP** - All requirements covered  
✅ **Well-tested** - 40 test cases  
✅ **Documented** - Extensive Javadoc + README  
✅ **Original** - Non-trivial game mechanics  
✅ **CI/CD ready** - Gradle build system  
✅ **Clean code** - Readable and maintainable  
✅ **Reproducible** - Fixed random seeds in tests  

## 📝 Sample Output

```
======================================================================
BATTLE BEGINS!
======================================================================

TEAM HEROES:
  - Aragorn (Warrior) - HP: 120, ATK: 25, DEF: 8, INI: 6
  - Gandalf (Mage) - HP: 80, ATK: 35, DEF: 3, INI: 7

TEAM MONSTERS:
  - Dark Lord (MonsterBoss) - HP: 200, ATK: 30, DEF: 10, INI: 4

======================================================================
ROUND 1
======================================================================

[Gandalf's turn]
Gandalf casts FIREBALL (AOE)!
  -> Dark Lord takes 21 fire damage!

[Aragorn's turn]
  ** CRITICAL HIT! **
Aragorn attacks Dark Lord for 37 damage!

--- Round 1 Summary ---
HEROES:
  Gandalf: 80/80 HP
  Aragorn: 120/120 HP
MONSTERS:
  Dark Lord: 142/200 HP

======================================================================
BATTLE ENDED!
======================================================================
WINNER: TEAM HEROES
```

This project is **complete, tested, documented, and ready for submission!** 🎉
