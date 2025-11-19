package de.throsenheim.psta.model.creatures;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Creature;
import de.throsenheim.psta.model.Team;

import java.util.List;
import java.util.Random;

/**
 * MonsterBoss - strong enemy that gets stronger when health is low.
 */
public class MonsterBoss extends Creature {
    
    private static final double ENRAGE_THRESHOLD = 0.5;      // 50% health
    private static final double ENRAGE_MULTIPLIER = 1.5;     // 1.5x damage
    
    private final Random random;
    private boolean enraged;
    
    public MonsterBoss(String name, int health, int attackPower, int defense, int initiative, Team team, Random random) 
            throws InvalidCreatureStateException {
        super(name, health, attackPower, defense, initiative, team);
        this.random = random != null ? random : new Random();
        this.enraged = false;
    }
    
    // Does more damage when health is low
    @Override
    protected int calculateDamage(Creature target) {
        if (!enraged && getHealthPercentage() <= ENRAGE_THRESHOLD) {
            enraged = true;
            System.out.println("  >>> " + getName() + " ENRAGES! Attack power increased! <<<");
        }
        
        int baseDamage = getAttackPower();
        if (enraged) {
            baseDamage = (int) (baseDamage * ENRAGE_MULTIPLIER);
        }
        
        return baseDamage;
    }
    
    // Attacks enemies with low defense
    @Override
    public void performRoundAction(Battlefield battlefield) {
        if (!isAlive()) {
            return;
        }
        
        List<Creature> enemies = battlefield.getEnemies(this);
        if (enemies.isEmpty()) {
            return;
        }
        
        // Target low-defense enemies
        Creature target = enemies.stream()
                .min((a, b) -> Integer.compare(a.getDefense(), b.getDefense()))
                .orElse(null);
        
        if (target != null) {
            attack(target);
        }
    }
    
    public boolean isEnraged() {
        return enraged;
    }
}
