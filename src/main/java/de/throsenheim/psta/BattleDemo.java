package de.throsenheim.psta;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Team;
import de.throsenheim.psta.model.creatures.Healer;
import de.throsenheim.psta.model.creatures.Mage;
import de.throsenheim.psta.model.creatures.MonsterBoss;
import de.throsenheim.psta.model.creatures.Warrior;

import java.util.Random;

/**
 * Demo class to run custom battle scenarios.
 * You can modify the creatures and teams to create different battles.
 */
public class BattleDemo {
    
    public static void main(String[] args) throws InvalidCreatureStateException {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║         CREATURE BATTLE SIM - CUSTOM BATTLE                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Create a random generator with a seed for reproducibility
        // Change the seed (e.g., 123, 456, 789) to get different battle outcomes
        Random random = new Random(42);
        
        // Create battlefield with max 30 rounds
        Battlefield battlefield = new Battlefield(30);
        
        // ========== CREATE YOUR TEAM ==========
        // Heroes Team
        Warrior knight = new Warrior("Sir Galahad", 130, 28, 9, 6, Team.HEROES, random);
        Mage wizard = new Mage("Merlin", 85, 40, 4, 8, Team.HEROES, random);
        Healer priest = new Healer("Father Thomas", 95, 12, 6, 7, Team.HEROES, 30, random);
        
        // Monsters Team
        MonsterBoss dragon = new MonsterBoss("Ancient Dragon", 250, 35, 12, 5, Team.MONSTERS, random);
        Warrior troll = new Warrior("Cave Troll", 150, 30, 8, 4, Team.MONSTERS, random);
        
        // Add creatures to battlefield
        battlefield.addCreature(knight);
        battlefield.addCreature(wizard);
        battlefield.addCreature(priest);
        battlefield.addCreature(dragon);
        battlefield.addCreature(troll);
        
        // Start the battle!
        battlefield.startBattle();
        
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                   BATTLE COMPLETE!                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}
