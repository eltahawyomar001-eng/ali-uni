package de.throsenheim.psta.model.creatures;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Creature;
import de.throsenheim.psta.model.Team;

import java.util.List;
import java.util.Random;

/**
 * Warrior - strong fighter that can do critical hits.
 */
public class Warrior extends Creature {
    
    private static final double CRITICAL_HIT_CHANCE = 0.25;  // 25% chance
    private static final double CRITICAL_MULTIPLIER = 1.5;    // 1.5x damage
    
    private final Random random;
    
    public Warrior(String name, int health, int attackPower, int defense, int initiative, Team team, Random random) 
            throws InvalidCreatureStateException {
        super(name, health, attackPower, defense, initiative, team);
        this.random = random != null ? random : new Random();
    }
    
    // Sometimes does critical hit
    @Override
    protected int calculateDamage(Creature target) {
        int baseDamage = getAttackPower();
        
        // Check for critical hit
        if (random.nextDouble() < CRITICAL_HIT_CHANCE) {
            int critDamage = (int) (baseDamage * CRITICAL_MULTIPLIER);
            System.out.println("  ** CRITICAL HIT! **");
            return critDamage;
        }
        
        return baseDamage;
    }
    
    // Attacks the weakest enemy
    @Override
    public void performRoundAction(Battlefield battlefield) {
        if (!isAlive()) {
            return;
        }
        
        List<Creature> enemies = battlefield.getEnemies(this);
        if (enemies.isEmpty()) {
            return;
        }
        
        // Find enemy with lowest health
        Creature target = enemies.stream()
                .min((a, b) -> Integer.compare(a.getHealth(), b.getHealth()))
                .orElse(null);
        
        if (target != null) {
            attack(target);
        }
    }
}
