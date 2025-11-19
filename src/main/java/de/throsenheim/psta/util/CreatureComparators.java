package de.throsenheim.psta.util;

import de.throsenheim.psta.model.Creature;

import java.util.Comparator;

/**
 * Utility class containing various Comparator implementations for sorting creatures.
 * Demonstrates the use of Comparator interface as required by the assignment.
 */
public class CreatureComparators {
    
    /**
     * Comparator that sorts creatures by health (ascending - lowest health first).
     */
    public static final Comparator<Creature> BY_HEALTH = new Comparator<Creature>() {
        @Override
        public int compare(Creature c1, Creature c2) {
            return Integer.compare(c1.getHealth(), c2.getHealth());
        }
    };
    
    /**
     * Comparator that sorts creatures by health percentage (ascending).
     */
    public static final Comparator<Creature> BY_HEALTH_PERCENTAGE = 
            Comparator.comparingDouble(Creature::getHealthPercentage);
    
    /**
     * Comparator that sorts creatures by attack power (descending - highest first).
     */
    public static final Comparator<Creature> BY_ATTACK_POWER = 
            (c1, c2) -> Integer.compare(c2.getAttackPower(), c1.getAttackPower());
    
    /**
     * Comparator that sorts creatures by defense (ascending - lowest first).
     */
    public static final Comparator<Creature> BY_DEFENSE = 
            Comparator.comparingInt(Creature::getDefense);
    
    /**
     * Comparator that sorts creatures by initiative (descending - highest first).
     * This is similar to the natural ordering defined in Creature.compareTo(),
     * but provided as a separate Comparator for demonstration.
     */
    public static final Comparator<Creature> BY_INITIATIVE = 
            (c1, c2) -> Integer.compare(c2.getInitiative(), c1.getInitiative());
    
    /**
     * Comparator that sorts creatures by name (alphabetically).
     */
    public static final Comparator<Creature> BY_NAME = 
            Comparator.comparing(Creature::getName);
    
    /**
     * Comparator that sorts creatures by team, then by name within each team.
     */
    public static final Comparator<Creature> BY_TEAM_THEN_NAME = 
            Comparator.comparing(Creature::getTeam)
                      .thenComparing(Creature::getName);
    
    /**
     * Creates a comparator that combines multiple criteria:
     * 1. Alive creatures before dead ones
     * 2. Higher health percentage first among living
     * 3. Higher attack power as tiebreaker
     * 
     * @return a composite comparator
     */
    public static Comparator<Creature> createPriorityComparator() {
        return Comparator.comparing(Creature::isAlive, Comparator.reverseOrder())
                         .thenComparing(Creature::getHealthPercentage, Comparator.reverseOrder())
                         .thenComparing(Creature::getAttackPower, Comparator.reverseOrder());
    }
    
    // Private constructor to prevent instantiation
    private CreatureComparators() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
