package de.throsenheim.psta.model.creatures;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Creature;
import de.throsenheim.psta.model.Team;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Healer class - a support creature that heals allies and deals light damage.
 * Healers prioritize healing wounded allies over attacking.
 */
public class Healer extends Creature {
    
    private static final double HEAL_THRESHOLD = 0.6; // Heal if ally below 60% HP
    
    private final Random random;
    private final int healPower;
    
    /**
     * Creates a new Healer with the specified attributes.
     * 
     * @param name the healer's name
     * @param health the healer's health points
     * @param attackPower the healer's attack power
     * @param defense the healer's defense value
     * @param initiative the healer's initiative
     * @param team the healer's team
     * @param healPower the amount of HP the healer can restore
     * @param random random number generator for combat calculations
     * @throws InvalidCreatureStateException if any attribute is invalid
     */
    public Healer(String name, int health, int attackPower, int defense, int initiative, 
                  Team team, int healPower, Random random) throws InvalidCreatureStateException {
        super(name, health, attackPower, defense, initiative, team);
        this.random = random != null ? random : new Random();
        this.healPower = healPower;
    }
    
    /**
     * Calculates healing damage (light attacks).
     * 
     * @param target the creature being attacked
     * @return the calculated damage amount
     */
    @Override
    protected int calculateDamage(Creature target) {
        // Healers deal less damage
        return getAttackPower();
    }
    
    /**
     * Performs the healer's action: heal wounded ally or attack enemy.
     * 
     * @param battlefield the battlefield context
     */
    @Override
    public void performRoundAction(Battlefield battlefield) {
        if (!isAlive()) {
            return;
        }
        
        // Check if any ally needs healing
        List<Creature> allies = battlefield.getAllies(this);
        Creature woundedAlly = allies.stream()
                .filter(ally -> ally.getHealthPercentage() < HEAL_THRESHOLD)
                .min(Comparator.comparingDouble(Creature::getHealthPercentage))
                .orElse(null);
        
        if (woundedAlly != null) {
            // Heal the most wounded ally
            System.out.println(getName() + " casts HEAL on " + woundedAlly.getName() + "!");
            woundedAlly.heal(healPower);
        } else {
            // No one needs healing, attack an enemy
            List<Creature> enemies = battlefield.getEnemies(this);
            if (!enemies.isEmpty()) {
                Creature target = enemies.get(random.nextInt(enemies.size()));
                attack(target);
            }
        }
    }
    
    public int getHealPower() {
        return healPower;
    }
}
