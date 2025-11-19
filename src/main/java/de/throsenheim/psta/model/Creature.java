package de.throsenheim.psta.model;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base class for all creatures in the game.
 * This is abstract so you can't create a plain Creature, only specific types.
 */
public abstract class Creature implements ActionPerRound, Comparable<Creature>, Cloneable {
    
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    
    private final int id;
    private final String name;
    private int health;
    private final int maxHealth;
    private final int attackPower;
    private final int defense;
    private final int initiative;
    private final Team team;
    private boolean alive;
    
    /**
     * Makes a new creature.
     * Checks if all the values make sense.
     */
    public Creature(String name, int health, int attackPower, int defense, int initiative, Team team) 
            throws InvalidCreatureStateException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidCreatureStateException("Creature name cannot be null or empty");
        }
        if (health <= 0) {
            throw new InvalidCreatureStateException("Health must be positive, got: " + health);
        }
        if (attackPower < 0) {
            throw new InvalidCreatureStateException("Attack power cannot be negative, got: " + attackPower);
        }
        if (defense < 0) {
            throw new InvalidCreatureStateException("Defense cannot be negative, got: " + defense);
        }
        if (team == null) {
            throw new InvalidCreatureStateException("Team cannot be null");
        }
        
        this.id = ID_GENERATOR.incrementAndGet();
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.attackPower = attackPower;
        this.defense = defense;
        this.initiative = initiative;
        this.team = team;
        this.alive = true;
    }
    
    /**
     * Each type of creature calculates damage differently.
     * Subclasses have to implement this.
     */
    protected abstract int calculateDamage(Creature target);
    
    /**
     * Attack another creature.
     */
    public void attack(Creature target) {
        if (!this.isAlive() || !target.isAlive()) {
            return;
        }
        
        int damage = calculateDamage(target);
        target.takeDamage(damage);
        
        System.out.println(this.name + " attacks " + target.getName() + 
                         " for " + damage + " damage!");
    }
    
    /**
     * Take damage from an attack.
     * Defense reduces the damage.
     */
    public void takeDamage(int damage) {
        if (!alive) {
            return;
        }
        
        // Defense reduces damage (minimum 1 damage if hit)
        int actualDamage = Math.max(1, damage - defense);
        health -= actualDamage;
        
        if (health <= 0) {
            health = 0;
            alive = false;
            System.out.println(">>> " + name + " has been defeated! <<<");
        }
    }
    
    /**
     * Restore health. Can't go above max health.
     */
    public void heal(int amount) {
        if (!alive) {
            return;
        }
        
        int oldHealth = health;
        health = Math.min(maxHealth, health + amount);
        int actualHealing = health - oldHealth;
        
        if (actualHealing > 0) {
            System.out.println(name + " is healed for " + actualHealing + " HP!");
        }
    }
    
    /**
     * Compare by initiative - higher goes first.
     */
    @Override
    public int compareTo(Creature other) {
        // Higher initiative goes first, so reverse order
        return Integer.compare(other.initiative, this.initiative);
    }
    
    /**
     * Make a copy of this creature.
     */
    @Override
    public Creature clone() {
        try {
            return (Creature) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone should be supported", e);
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s[id=%d, name='%s', HP=%d/%d, ATK=%d, DEF=%d, INI=%d, team=%s, alive=%s]",
                getClass().getSimpleName(), id, name, health, maxHealth, 
                attackPower, defense, initiative, team, alive);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Creature creature = (Creature) obj;
        return id == creature.id && Objects.equals(name, creature.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    
    // Getters
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public int getAttackPower() {
        return attackPower;
    }
    
    public int getDefense() {
        return defense;
    }
    
    public int getInitiative() {
        return initiative;
    }
    
    public Team getTeam() {
        return team;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public double getHealthPercentage() {
        return (double) health / maxHealth;
    }
}
