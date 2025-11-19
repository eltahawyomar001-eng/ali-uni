package de.throsenheim.psta.model;

import de.throsenheim.psta.exceptions.InvalidCreatureStateException;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract base class representing a creature in the battle simulation.
 * This class demonstrates inheritance, abstraction, and implements Comparable
 * for natural ordering by initiative (turn order).
 * 
 * All creatures have basic attributes like health, attack power, defense, and initiative.
 * Concrete subclasses must implement the attack logic and action behavior.
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
     * Constructs a new Creature with the specified attributes.
     * 
     * @param name the name of the creature
     * @param health the initial health points
     * @param attackPower the base attack power
     * @param defense the defense value (reduces incoming damage)
     * @param initiative the initiative value (higher acts first)
     * @param team the team this creature belongs to
     * @throws InvalidCreatureStateException if any attribute is invalid
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
     * Calculates the damage this creature deals to a target.
     * This is an abstract method that subclasses must implement to define
     * their specific attack behavior.
     * 
     * @param target the creature being attacked
     * @return the amount of damage dealt
     */
    protected abstract int calculateDamage(Creature target);
    
    /**
     * Attacks another creature, dealing damage based on attack power and target's defense.
     * 
     * @param target the creature to attack
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
     * Reduces this creature's health by the specified damage amount.
     * Takes defense into account and handles death.
     * 
     * @param damage the raw damage amount
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
     * Heals this creature by the specified amount, up to max health.
     * 
     * @param amount the amount of health to restore
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
     * Compares creatures by initiative (higher initiative acts first).
     * This implements natural ordering for turn order determination.
     * 
     * @param other the other creature to compare to
     * @return negative if this creature acts after other, positive if before, 0 if same initiative
     */
    @Override
    public int compareTo(Creature other) {
        // Higher initiative goes first, so reverse order
        return Integer.compare(other.initiative, this.initiative);
    }
    
    /**
     * Creates a shallow copy of this creature.
     * Note: Team enum is immutable so shallow copy is safe.
     * 
     * @return a clone of this creature
     */
    @Override
    public Creature clone() {
        try {
            return (Creature) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone should be supported", e);
        }
    }
    
    /**
     * Returns a string representation of this creature including its current state.
     * 
     * @return a formatted string with creature details
     */
    @Override
    public String toString() {
        return String.format("%s[id=%d, name='%s', HP=%d/%d, ATK=%d, DEF=%d, INI=%d, team=%s, alive=%s]",
                getClass().getSimpleName(), id, name, health, maxHealth, 
                attackPower, defense, initiative, team, alive);
    }
    
    /**
     * Checks equality based on unique ID and name.
     * 
     * @param obj the object to compare to
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Creature creature = (Creature) obj;
        return id == creature.id && Objects.equals(name, creature.name);
    }
    
    /**
     * Returns hash code based on ID and name.
     * 
     * @return the hash code
     */
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
    
    /**
     * Gets the health as a percentage of max health.
     * 
     * @return health percentage (0.0 to 1.0)
     */
    public double getHealthPercentage() {
        return (double) health / maxHealth;
    }
}
