package de.throsenheim.psta.model.creatures;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Creature;
import de.throsenheim.psta.model.Team;

import java.util.List;
import java.util.Random;

/**
 * Warrior class - a strong melee fighter with high attack and defense.
 * Warriors deal consistent physical damage and can perform critical hits.
 */
public class Warrior extends Creature {
    
    private static final double CRITICAL_HIT_CHANCE = 0.25;
    private static final double CRITICAL_MULTIPLIER = 1.5;
    
    private final Random random;
    
    /**
     * Creates a new Warrior with the specified attributes.
     * 
     * @param name the warrior's name
     * @param health the warrior's health points
     * @param attackPower the warrior's attack power
     * @param defense the warrior's defense value
     * @param initiative the warrior's initiative
     * @param team the warrior's team
     * @param random random number generator for combat calculations
     * @throws InvalidCreatureStateException if any attribute is invalid
     */
    public Warrior(String name, int health, int attackPower, int defense, int initiative, Team team, Random random) 
            throws InvalidCreatureStateException {
        super(name, health, attackPower, defense, initiative, team);
        this.random = random != null ? random : new Random();
    }
    
    /**
     * Calculates damage with a chance for critical hits.
     * 
     * @param target the creature being attacked
     * @return the calculated damage amount
     */
    @Override
    protected int calculateDamage(Creature target) {
        int baseDamage = getAttackPower();
        
        // 25% chance for critical hit
        if (random.nextDouble() < CRITICAL_HIT_CHANCE) {
            int critDamage = (int) (baseDamage * CRITICAL_MULTIPLIER);
            System.out.println("  ** CRITICAL HIT! **");
            return critDamage;
        }
        
        return baseDamage;
    }
    
    /**
     * Performs the warrior's action: attack the weakest enemy.
     * 
     * @param battlefield the battlefield context
     */
    @Override
    public void performRoundAction(Battlefield battlefield) {
        if (!isAlive()) {
            return;
        }
        
        // Find the enemy with lowest health to finish them off
        List<Creature> enemies = battlefield.getEnemies(this);
        if (enemies.isEmpty()) {
            return;
        }
        
        Creature target = enemies.stream()
                .min((a, b) -> Integer.compare(a.getHealth(), b.getHealth()))
                .orElse(null);
        
        if (target != null) {
            attack(target);
        }
    }
}
