package de.throsenheim.psta.exceptions;

/**
 * Custom checked exception for invalid creature state conditions.
 * Thrown when a creature is in an invalid state for the requested operation,
 * such as negative health, invalid attribute values, or illegal state transitions.
 */
public class InvalidCreatureStateException extends Exception {
    
    /**
     * Constructs a new InvalidCreatureStateException with the specified detail message.
     * 
     * @param message the detail message explaining the invalid state
     */
    public InvalidCreatureStateException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new InvalidCreatureStateException with the specified detail message and cause.
     * 
     * @param message the detail message explaining the invalid state
     * @param cause the cause of this exception
     */
    public InvalidCreatureStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
