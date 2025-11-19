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
    
    private static final int MAX_AOE_TARGETS = 3;
    
    private final Random random;
    
    public Mage(String name, int health, int attackPower, int defense, int initiative, Team team, Random random) 
            throws InvalidCreatureStateException {
        super(name, health, attackPower, defense, initiative, team);
        this.random = random != null ? random : new Random();
    }
    
    @Override
    protected int calculateDamage(Creature target) {
        return getAttackPower();
    }
    
    // Attacks multiple enemies at once
    @Override
    public void performRoundAction(Battlefield battlefield) {
        if (!isAlive()) {
            return;
        }
        
        List<Creature> enemies = battlefield.getEnemies(this);
        if (enemies.isEmpty()) {
            return;
        }
        
        // Attack up to 3 enemies
        int targetsHit = Math.min(MAX_AOE_TARGETS, enemies.size());
        System.out.println(getName() + " casts AOE spell on " + targetsHit + " targets!");
        
        for (int i = 0; i < targetsHit; i++) {
            if (enemies.get(i).isAlive()) {
                attack(enemies.get(i));
            }
        }
    }
}
