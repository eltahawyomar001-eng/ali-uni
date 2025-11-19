package de.throsenheim.psta.model.creatures;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Creature;
import de.throsenheim.psta.model.Team;

import java.util.List;
import java.util.Random;

/**
 * MonsterBoss class - a powerful enemy with special enrage ability.
 * The boss becomes stronger when its health drops below 50%.
 */
public class MonsterBoss extends Creature {
    
    private static final double ENRAGE_THRESHOLD = 0.5;
    private static final double ENRAGE_MULTIPLIER = 1.5;
    
    private final Random random;
    private boolean enraged;
    
    /**
     * Creates a new MonsterBoss with the specified attributes.
     * 
     * @param name the boss's name
     * @param health the boss's health points
     * @param attackPower the boss's attack power
     * @param defense the boss's defense value
     * @param initiative the boss's initiative
     * @param team the boss's team
     * @param random random number generator for combat calculations
     * @throws InvalidCreatureStateException if any attribute is invalid
     */
    public MonsterBoss(String name, int health, int attackPower, int defense, int initiative, Team team, Random random) 
            throws InvalidCreatureStateException {
        super(name, health, attackPower, defense, initiative, team);
        this.random = random != null ? random : new Random();
        this.enraged = false;
    }
    
    /**
     * Calculates damage with enrage bonus when health is low.
     * 
     * @param target the creature being attacked
     * @return the calculated damage amount
     */
    @Override
    protected int calculateDamage(Creature target) {
        // Check for enrage
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
    
    /**
     * Performs the boss's action: attack random enemy with preference for low-defense targets.
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
        
        // Boss targets low-defense enemies (intelligent targeting)
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
