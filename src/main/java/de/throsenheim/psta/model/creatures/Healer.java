package de.throsenheim.psta.model.creatures;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Creature;
import de.throsenheim.psta.model.Team;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Healer - heals allies and attacks when no one needs healing.
 */
public class Healer extends Creature {
    
    private static final double HEAL_THRESHOLD = 0.6;  // Heal if below 60% HP
    
    private final Random random;
    private final int healPower;
    
    public Healer(String name, int health, int attackPower, int defense, int initiative, 
                  Team team, int healPower, Random random) throws InvalidCreatureStateException {
        super(name, health, attackPower, defense, initiative, team);
        this.random = random != null ? random : new Random();
        this.healPower = healPower;
    }
    
    @Override
    protected int calculateDamage(Creature target) {
        return getAttackPower();
    }
    
    // Heals allies or attacks if everyone is healthy
    @Override
    public void performRoundAction(Battlefield battlefield) {
        if (!isAlive()) {
            return;
        }
        
        // Find ally that needs healing
        List<Creature> allies = battlefield.getAllies(this);
        Creature woundedAlly = allies.stream()
                .filter(ally -> ally.getHealthPercentage() < HEAL_THRESHOLD)
                .min(Comparator.comparingDouble(Creature::getHealthPercentage))
                .orElse(null);
        
        if (woundedAlly != null) {
            System.out.println(getName() + " casts HEAL on " + woundedAlly.getName() + "!");
            woundedAlly.heal(healPower);
        } else {
            // No one needs healing, attack
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
