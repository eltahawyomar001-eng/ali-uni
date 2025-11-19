package de.throsenheim.psta.model;

/**
 * Interface defining the core action that all creatures must perform each round.
 * This interface demonstrates polymorphism - different creatures implement their
 * unique behaviors through this common contract.
 */
public interface ActionPerRound {
    
    /**
     * Performs this creature's action for the current round.
     * This could be attacking an enemy, healing an ally, or performing special abilities.
     * 
     * @param battlefield the battlefield context containing all creatures and game state
     */
    void performRoundAction(Battlefield battlefield);
}
