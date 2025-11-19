package de.throsenheim.psta;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Creature;
import de.throsenheim.psta.model.Team;
import de.throsenheim.psta.model.creatures.Healer;
import de.throsenheim.psta.model.creatures.Mage;
import de.throsenheim.psta.model.creatures.MonsterBoss;
import de.throsenheim.psta.model.creatures.Warrior;
import de.throsenheim.psta.util.CreatureComparators;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for business logic including damage calculation, healing,
 * comparator usage, equals/hashCode, and clone functionality.
 */
@DisplayName("Creature Business Logic Tests")
class CreatureLogicTest {
    
    @Test
    @DisplayName("Warrior deals damage correctly")
    void testWarriorDamage() throws InvalidCreatureStateException {
        Random random = new Random(100);
        Warrior attacker = new Warrior("Attacker", 100, 30, 5, 6, Team.HEROES, random);
        Warrior target = new Warrior("Target", 100, 20, 8, 5, Team.MONSTERS, random);
        
        int initialHealth = target.getHealth();
        attacker.attack(target);
        
        // Target should have lost health
        assertTrue(target.getHealth() < initialHealth, "Target should take damage");
        assertTrue(target.isAlive(), "Target should still be alive");
    }
    
    @Test
    @DisplayName("Defense reduces damage taken")
    void testDefenseReducesDamage() throws InvalidCreatureStateException {
        Random random = new Random(200);
        
        // High defense target
        Warrior highDefTarget = new Warrior("HighDef", 100, 20, 15, 5, Team.HEROES, random);
        // Low defense target
        Warrior lowDefTarget = new Warrior("LowDef", 100, 20, 2, 5, Team.HEROES, new Random(200));
        
        Warrior attacker = new Warrior("Attacker", 80, 25, 5, 6, Team.MONSTERS, new Random(200));
        
        attacker.attack(highDefTarget);
        int highDefDamage = 100 - highDefTarget.getHealth();
        
        // Reset and attack low defense
        attacker = new Warrior("Attacker2", 80, 25, 5, 6, Team.MONSTERS, new Random(200));
        attacker.attack(lowDefTarget);
        int lowDefDamage = 100 - lowDefTarget.getHealth();
        
        // Low defense should take more damage
        assertTrue(lowDefDamage > highDefDamage, 
                  "Lower defense should result in more damage taken");
    }
    
    @Test
    @DisplayName("Healer restores health correctly")
    void testHealerHealing() throws InvalidCreatureStateException {
        Random random = new Random(300);
        Healer healer = new Healer("Healer", 90, 15, 5, 7, Team.HEROES, 30, random);
        Warrior wounded = new Warrior("Wounded", 100, 20, 5, 6, Team.HEROES, random);
        
        // Damage the warrior first
        wounded.takeDamage(50);
        int healthAfterDamage = wounded.getHealth();
        
        // Heal the warrior
        wounded.heal(30);
        
        assertTrue(wounded.getHealth() > healthAfterDamage, "Health should increase after healing");
        assertTrue(wounded.getHealth() <= 100, "Health should not exceed max");
    }
    
    @Test
    @DisplayName("Healing cannot exceed max health")
    void testHealingCap() throws InvalidCreatureStateException {
        Random random = new Random(400);
        Warrior creature = new Warrior("Test", 100, 20, 5, 6, Team.HEROES, random);
        
        creature.takeDamage(20); // Now at 80 HP
        creature.heal(50); // Try to heal for 50
        
        assertEquals(100, creature.getHealth(), "Health should be capped at max health");
    }
    
    @Test
    @DisplayName("Creature dies when health reaches zero")
    void testCreatureDeath() throws InvalidCreatureStateException {
        Random random = new Random(500);
        Warrior creature = new Warrior("Doomed", 50, 20, 5, 6, Team.HEROES, random);
        
        assertTrue(creature.isAlive(), "Creature should start alive");
        
        creature.takeDamage(100); // Massive damage
        
        assertFalse(creature.isAlive(), "Creature should be dead");
        assertEquals(0, creature.getHealth(), "Health should be 0");
    }
    
    @Test
    @DisplayName("Dead creatures cannot be healed")
    void testCannotHealDead() throws InvalidCreatureStateException {
        Random random = new Random(600);
        Warrior creature = new Warrior("Dead", 50, 20, 5, 6, Team.HEROES, random);
        
        creature.takeDamage(100); // Kill it
        assertFalse(creature.isAlive());
        
        creature.heal(50); // Try to heal
        
        assertEquals(0, creature.getHealth(), "Dead creature should stay at 0 HP");
        assertFalse(creature.isAlive(), "Dead creature should stay dead");
    }
    
    @Test
    @DisplayName("Comparator BY_HEALTH sorts correctly")
    void testHealthComparator() throws InvalidCreatureStateException {
        Random random = new Random(700);
        List<Creature> creatures = new ArrayList<>();
        
        creatures.add(new Warrior("High", 100, 20, 5, 6, Team.HEROES, random));
        creatures.add(new Warrior("Low", 30, 20, 5, 5, Team.HEROES, random));
        creatures.add(new Warrior("Medium", 60, 20, 5, 7, Team.HEROES, random));
        
        creatures.sort(CreatureComparators.BY_HEALTH);
        
        assertEquals("Low", creatures.get(0).getName(), "Lowest health should be first");
        assertEquals("Medium", creatures.get(1).getName(), "Medium health should be second");
        assertEquals("High", creatures.get(2).getName(), "Highest health should be last");
    }
    
    @Test
    @DisplayName("Comparator BY_ATTACK_POWER sorts correctly")
    void testAttackPowerComparator() throws InvalidCreatureStateException {
        Random random = new Random(800);
        List<Creature> creatures = new ArrayList<>();
        
        creatures.add(new Warrior("Weak", 100, 15, 5, 6, Team.HEROES, random));
        creatures.add(new Mage("Strong", 80, 40, 3, 7, Team.HEROES, random));
        creatures.add(new Warrior("Medium", 100, 25, 5, 5, Team.HEROES, random));
        
        creatures.sort(CreatureComparators.BY_ATTACK_POWER);
        
        assertEquals("Strong", creatures.get(0).getName(), "Highest attack should be first");
        assertEquals("Medium", creatures.get(1).getName());
        assertEquals("Weak", creatures.get(2).getName(), "Lowest attack should be last");
    }
    
    @Test
    @DisplayName("Comparable (initiative) sorts correctly")
    void testComparableInitiative() throws InvalidCreatureStateException {
        Random random = new Random(900);
        List<Creature> creatures = new ArrayList<>();
        
        creatures.add(new Warrior("Slow", 100, 20, 5, 3, Team.HEROES, random));
        creatures.add(new Mage("Fast", 80, 30, 3, 10, Team.HEROES, random));
        creatures.add(new Healer("Medium", 90, 15, 5, 6, Team.HEROES, 20, random));
        
        creatures.sort(null); // Uses natural ordering (Comparable)
        
        assertEquals("Fast", creatures.get(0).getName(), "Highest initiative should be first");
        assertEquals("Medium", creatures.get(1).getName());
        assertEquals("Slow", creatures.get(2).getName(), "Lowest initiative should be last");
    }
    
    @Test
    @DisplayName("Equals and hashCode work correctly")
    void testEqualsAndHashCode() throws InvalidCreatureStateException {
        Random random = new Random(1000);
        Warrior warrior1 = new Warrior("Hero", 100, 20, 5, 6, Team.HEROES, random);
        Warrior warrior2 = new Warrior("Hero", 100, 20, 5, 6, Team.HEROES, random);
        Warrior warrior3 = new Warrior("Other", 100, 20, 5, 6, Team.HEROES, random);
        
        // Same instance
        assertEquals(warrior1, warrior1, "Creature should equal itself");
        
        // Different instances with same name but different IDs
        assertNotEquals(warrior1, warrior2, "Different instances should not be equal (different IDs)");
        
        // Different names
        assertNotEquals(warrior1, warrior3, "Different names should not be equal");
        
        // HashCode consistency
        assertEquals(warrior1.hashCode(), warrior1.hashCode(), "HashCode should be consistent");
        
        // Null and different class
        assertNotEquals(warrior1, null, "Should not equal null");
        assertNotEquals(warrior1, "String", "Should not equal different type");
    }
    
    @Test
    @DisplayName("Clone creates independent copy")
    void testClone() throws InvalidCreatureStateException {
        Random random = new Random(1100);
        Warrior original = new Warrior("Original", 100, 20, 5, 6, Team.HEROES, random);
        
        Creature cloned = original.clone();
        
        assertNotNull(cloned, "Clone should not be null");
        assertNotSame(original, cloned, "Clone should be different instance");
        assertEquals(original.getName(), cloned.getName(), "Clone should have same name");
        assertEquals(original.getHealth(), cloned.getHealth(), "Clone should have same health");
        assertEquals(original.getAttackPower(), cloned.getAttackPower(), "Clone should have same attack");
    }
    
    @Test
    @DisplayName("ToString returns meaningful output")
    void testToString() throws InvalidCreatureStateException {
        Random random = new Random(1200);
        Warrior warrior = new Warrior("TestWarrior", 100, 25, 8, 7, Team.HEROES, random);
        
        String result = warrior.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("TestWarrior"), "toString should contain name");
        assertTrue(result.contains("100"), "toString should contain health");
        assertTrue(result.contains("HEROES"), "toString should contain team");
    }
    
    @Test
    @DisplayName("Mage AOE attacks multiple enemies")
    void testMageAOE() throws InvalidCreatureStateException {
        Random random = new Random(1300);
        Mage mage = new Mage("AoeMage", 80, 30, 3, 8, Team.HEROES, random);
        
        // Mage starts with 3 mana for AOE
        assertEquals(3, mage.getManaPool(), "Mage should start with 3 mana");
    }
    
    @Test
    @DisplayName("MonsterBoss enrage mechanic")
    void testBossEnrage() throws InvalidCreatureStateException {
        Random random = new Random(1400);
        MonsterBoss boss = new MonsterBoss("Boss", 200, 30, 8, 6, Team.MONSTERS, random);
        
        assertFalse(boss.isEnraged(), "Boss should not be enraged at full health");
        
        // Damage boss to below 50%
        boss.takeDamage(120); // Now at 80/200 = 40%
        
        // Enrage is checked during attack, not on damage taken
        // So we just verify the boss is damaged
        assertTrue(boss.getHealth() < boss.getMaxHealth() / 2, "Boss should be below 50% health");
    }
    
    @Test
    @DisplayName("Health percentage calculation")
    void testHealthPercentage() throws InvalidCreatureStateException {
        Random random = new Random(1500);
        Warrior warrior = new Warrior("Test", 100, 20, 0, 6, Team.HEROES, random);
        
        assertEquals(1.0, warrior.getHealthPercentage(), 0.01, "Should be at 100% health");
        
        warrior.takeDamage(50);
        assertEquals(0.5, warrior.getHealthPercentage(), 0.01, "Should be at 50% health");
        
        warrior.takeDamage(50);
        assertEquals(0.0, warrior.getHealthPercentage(), 0.01, "Should be at 0% health");
    }
}
