package de.throsenheim.psta.model;

/**
 * Enum representing the team affiliation of creatures in the battle.
 * Used to determine friend vs foe relationships during combat.
 */
public enum Team {
    /** Team of heroes fighting against monsters */
    HEROES,
    
    /** Team of monsters fighting against heroes */
    MONSTERS
}
