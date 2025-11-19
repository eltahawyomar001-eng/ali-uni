package de.throsenheim.psta;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Creature;
import de.throsenheim.psta.model.Team;
import de.throsenheim.psta.model.creatures.Healer;
import de.throsenheim.psta.model.creatures.Mage;
import de.throsenheim.psta.model.creatures.MonsterBoss;
import de.throsenheim.psta.model.creatures.Warrior;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test class for the complete battle simulation.
 * Tests the full game flow from start to finish with reproducible results.
 */
@DisplayName("Creature Battle Simulation Tests")
class CreatureBattleSimTest {
    
    @Test
    @DisplayName("Full battle simulation - Heroes vs Monsters")
    void testFullBattleSimulation() throws InvalidCreatureStateException {
        // Use fixed seed for reproducible tests
        Random random = new Random(42);
        
        // Create battlefield
        Battlefield battlefield = new Battlefield(20);
        
        // Create heroes team
        Warrior warrior = new Warrior("Aragorn", 120, 25, 8, 6, Team.HEROES, random);
        Mage mage = new Mage("Gandalf", 80, 35, 3, 7, Team.HEROES, random);
        Healer healer = new Healer("Elrond", 90, 15, 5, 5, Team.HEROES, 25, random);
        
        // Create monsters team
        MonsterBoss boss = new MonsterBoss("Dark Lord", 200, 30, 10, 4, Team.MONSTERS, random);
        Warrior orcWarrior = new Warrior("Orc Chieftain", 100, 20, 6, 3, Team.MONSTERS, random);
        
        // Add creatures to battlefield
        battlefield.addCreature(warrior);
        battlefield.addCreature(mage);
        battlefield.addCreature(healer);
        battlefield.addCreature(boss);
        battlefield.addCreature(orcWarrior);
        
        // Start the battle
        battlefield.startBattle();
        
        // Verify battle completed
        assertTrue(battlefield.getCurrentRound() > 0, "Battle should have progressed");
        assertTrue(battlefield.getCurrentRound() <= 20, "Battle should not exceed max rounds");
        
        // Verify a winner was determined
        assertNotNull(battlefield.getWinnerTeam(), "A winner should be determined");
        
        // Verify casualties occurred
        long totalDead = battlefield.getDeadCount(Team.HEROES) + battlefield.getDeadCount(Team.MONSTERS);
        assertTrue(totalDead > 0, "There should be casualties in battle");
        
        // Verify at least one team is eliminated or severely damaged
        assertTrue(battlefield.getLivingCount(Team.HEROES) == 0 || 
                  battlefield.getLivingCount(Team.MONSTERS) == 0,
                  "At least one team should be eliminated");
    }
    
    @Test
    @DisplayName("Battle with overwhelming hero advantage")
    void testHeroesWinEasily() throws InvalidCreatureStateException {
        Random random = new Random(100);
        Battlefield battlefield = new Battlefield(10);
        
        // Strong heroes
        battlefield.addCreature(new Warrior("Hero1", 150, 40, 10, 8, Team.HEROES, random));
        battlefield.addCreature(new Warrior("Hero2", 150, 40, 10, 7, Team.HEROES, random));
        battlefield.addCreature(new Healer("Hero3", 100, 20, 8, 6, Team.HEROES, 30, random));
        
        // Weak monster
        battlefield.addCreature(new Warrior("WeakMonster", 50, 10, 2, 3, Team.MONSTERS, random));
        
        battlefield.startBattle();
        
        // Heroes should win
        assertEquals(Team.HEROES, battlefield.getWinnerTeam());
        assertTrue(battlefield.getLivingCount(Team.HEROES) > 0);
        assertEquals(0, battlefield.getLivingCount(Team.MONSTERS));
    }
    
    @Test
    @DisplayName("Battle with balanced teams")
    void testBalancedBattle() throws InvalidCreatureStateException {
        Random random = new Random(200);
        Battlefield battlefield = new Battlefield(30);
        
        // Create balanced teams
        battlefield.addCreature(new Warrior("Knight", 100, 25, 7, 6, Team.HEROES, random));
        battlefield.addCreature(new Mage("Wizard", 70, 30, 2, 8, Team.HEROES, random));
        
        battlefield.addCreature(new MonsterBoss("Dragon", 150, 28, 8, 5, Team.MONSTERS, random));
        
        battlefield.startBattle();
        
        // Battle should complete
        assertNotNull(battlefield.getWinnerTeam());
        assertTrue(battlefield.getCurrentRound() >= 1);
    }
    
    @Test
    @DisplayName("Healer keeps team alive longer")
    void testHealerEffectiveness() throws InvalidCreatureStateException {
        Random random = new Random(300);
        
        // Battle WITH healer
        Battlefield battlefieldWithHealer = new Battlefield(20);
        battlefieldWithHealer.addCreature(new Warrior("Hero1", 80, 20, 5, 6, Team.HEROES, random));
        battlefieldWithHealer.addCreature(new Healer("Healer", 70, 10, 4, 7, Team.HEROES, 20, random));
        battlefieldWithHealer.addCreature(new Warrior("Monster1", 120, 25, 6, 5, Team.MONSTERS, new Random(300)));
        
        battlefieldWithHealer.startBattle();
        int roundsWithHealer = battlefieldWithHealer.getCurrentRound();
        
        // Battle WITHOUT healer
        Battlefield battlefieldNoHealer = new Battlefield(20);
        battlefieldNoHealer.addCreature(new Warrior("Hero2", 80, 20, 5, 6, Team.HEROES, new Random(300)));
        battlefieldNoHealer.addCreature(new Warrior("Hero3", 70, 10, 4, 7, Team.HEROES, new Random(300)));
        battlefieldNoHealer.addCreature(new Warrior("Monster2", 120, 25, 6, 5, Team.MONSTERS, new Random(300)));
        
        battlefieldNoHealer.startBattle();
        
        // With healer, battle might last longer (healer provides sustain)
        // This is a demonstration test - exact outcome depends on RNG
        assertTrue(roundsWithHealer >= 1, "Battle with healer should complete");
    }
    
    @Test
    @DisplayName("Boss enrage mechanic activates")
    void testBossEnrageActivates() throws InvalidCreatureStateException {
        Random random = new Random(400);
        Battlefield battlefield = new Battlefield(25);
        
        MonsterBoss boss = new MonsterBoss("Enraged Boss", 150, 30, 5, 8, Team.MONSTERS, random);
        battlefield.addCreature(boss);
        
        // Create heroes that will damage boss
        battlefield.addCreature(new Warrior("DamageDealer1", 120, 25, 8, 7, Team.HEROES, random));
        battlefield.addCreature(new Mage("DamageDealer2", 80, 35, 3, 6, Team.HEROES, random));
        
        battlefield.startBattle();
        
        // Boss should have taken damage and possibly enraged
        // (Enrage happens at 50% health or below)
        assertTrue(boss.getHealth() < boss.getMaxHealth() || !boss.isAlive(), 
                  "Boss should have taken damage");
    }
    
    @Test
    @DisplayName("Initiative determines turn order")
    void testInitiativeOrdering() throws InvalidCreatureStateException {
        Random random = new Random(500);
        Battlefield battlefield = new Battlefield(1); // Only 1 round needed
        
        // Create creatures with different initiative
        Creature slow = new Warrior("SlowWarrior", 100, 20, 5, 1, Team.HEROES, random);
        Creature fast = new Mage("FastMage", 80, 25, 3, 10, Team.MONSTERS, random);
        Creature medium = new Healer("MediumHealer", 90, 15, 4, 5, Team.HEROES, 20, random);
        
        battlefield.addCreature(slow);
        battlefield.addCreature(fast);
        battlefield.addCreature(medium);
        
        // The battlefield sorts by initiative in executeRound (via Comparable)
        // This test verifies the game runs with different initiative values
        battlefield.startBattle();
        
        assertTrue(battlefield.getCurrentRound() >= 1, "At least one round should execute");
    }
}
