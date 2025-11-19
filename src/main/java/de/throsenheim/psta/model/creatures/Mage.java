package de.throsenheim.psta.model.creatures;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Creature;
import de.throsenheim.psta.model.Team;

import java.util.List;
import java.util.Random;

/**
 * Mage - magic user that can attack multiple enemies.
 */
public class Mage extends Creature {
    
    private static final int AOE_DAMAGE_PERCENTAGE = 60; // 60% of attack power
    private static final int MAX_AOE_TARGETS = 3;
    
    private final Random random;
    private int manaPool;
    
    public Mage(String name, int health, int attackPower, int defense, int initiative, Team team, Random random) 
            throws InvalidCreatureStateException {
        super(name, health, attackPower, defense, initiative, team);
        this.random = random != null ? random : new Random();
        this.manaPool = 3;  // Can do 3 AOE attacks
    }
    
    // Magic damage varies a bit
    @Override
    protected int calculateDamage(Creature target) {
        double variance = 0.8 + (random.nextDouble() * 0.4);
        return (int) (getAttackPower() * variance);
    }
    
    // Does AOE if has mana, otherwise single target
    @Override
    public void performRoundAction(Battlefield battlefield) {
        if (!isAlive()) {
            return;
        }
        
        List<Creature> enemies = battlefield.getEnemies(this);
        if (enemies.isEmpty()) {
            return;
        }
        
        // Use AOE if have mana and multiple enemies
        if (manaPool > 0 && enemies.size() >= 2) {
            performAoeAttack(enemies);
            manaPool--;
        } else {
            // Single target - attack highest health
            Creature target = enemies.stream()
                    .max((a, b) -> Integer.compare(a.getHealth(), b.getHealth()))
                    .orElse(null);
            
            if (target != null) {
                attack(target);
            }
        }
    }
    
    // Hit multiple enemies at once
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
