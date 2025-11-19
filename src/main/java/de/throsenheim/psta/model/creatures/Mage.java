package de.throsenheim.psta.model.creatures;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Creature;
import de.throsenheim.psta.model.Team;

import java.util.List;
import java.util.Random;

/**
 * Mage class - a magical attacker with high damage but low defense.
 * Mages can deal area-of-effect damage to multiple enemies.
 */
public class Mage extends Creature {
    
    private static final int AOE_DAMAGE_PERCENTAGE = 60; // 60% of attack power
    private static final int MAX_AOE_TARGETS = 3;
    
    private final Random random;
    private int manaPool;
    
    /**
     * Creates a new Mage with the specified attributes.
     * 
     * @param name the mage's name
     * @param health the mage's health points
     * @param attackPower the mage's spell power
     * @param defense the mage's defense value
     * @param initiative the mage's initiative
     * @param team the mage's team
     * @param random random number generator for combat calculations
     * @throws InvalidCreatureStateException if any attribute is invalid
     */
    public Mage(String name, int health, int attackPower, int defense, int initiative, Team team, Random random) 
            throws InvalidCreatureStateException {
        super(name, health, attackPower, defense, initiative, team);
        this.random = random != null ? random : new Random();
        this.manaPool = 3; // Can do 3 AOE attacks before single-target only
    }
    
    /**
     * Calculates magical damage with random variance.
     * 
     * @param target the creature being attacked
     * @return the calculated damage amount
     */
    @Override
    protected int calculateDamage(Creature target) {
        // Magical damage has variance: 80% to 120% of attack power
        double variance = 0.8 + (random.nextDouble() * 0.4);
        return (int) (getAttackPower() * variance);
    }
    
    /**
     * Performs the mage's action: AOE attack if mana available, otherwise single target.
     * 
     * @param battlefield the battlefield context
     */
    @Override
    public void performRoundAction(Battlefield battlefield) {
        if (!isAlive()) {
            return;
        }
        
        List<Creature> enemies = battlefield.getEnemies(this);
        if (enemies.isEmpty()) {
            return;
        }
        
        // Use AOE if mana available and multiple enemies
        if (manaPool > 0 && enemies.size() >= 2) {
            performAoeAttack(enemies);
            manaPool--;
        } else {
            // Single target - attack highest health enemy
            Creature target = enemies.stream()
                    .max((a, b) -> Integer.compare(a.getHealth(), b.getHealth()))
                    .orElse(null);
            
            if (target != null) {
                attack(target);
            }
        }
    }
    
    /**
     * Performs an area-of-effect attack hitting multiple enemies.
     * 
     * @param enemies the list of available enemies
     */
    private void performAoeAttack(List<Creature> enemies) {
        System.out.println(getName() + " casts FIREBALL (AOE)!");
        
        int targetsHit = Math.min(MAX_AOE_TARGETS, enemies.size());
        int aoeDamage = (getAttackPower() * AOE_DAMAGE_PERCENTAGE) / 100;
        
        for (int i = 0; i < targetsHit; i++) {
            Creature target = enemies.get(i);
            if (target.isAlive()) {
                target.takeDamage(aoeDamage);
                System.out.println("  -> " + target.getName() + " takes " + aoeDamage + " fire damage!");
            }
        }
    }
    
    public int getManaPool() {
        return manaPool;
    }
}
