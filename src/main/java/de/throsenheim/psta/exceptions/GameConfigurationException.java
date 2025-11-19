package de.throsenheim.psta.exceptions;

/**
 * Custom unchecked exception for game configuration errors.
 * Thrown when the game is configured incorrectly, such as duplicate creature names,
 * invalid team assignments, or missing required components.
 */
public class GameConfigurationException extends RuntimeException {
    
    /**
     * Constructs a new GameConfigurationException with the specified detail message.
     * 
     * @param message the detail message explaining the configuration error
     */
    public GameConfigurationException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new GameConfigurationException with the specified detail message and cause.
     * 
     * @param message the detail message explaining the configuration error
     * @param cause the cause of this exception
     */
    public GameConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
