package de.throsenheim.psta;

import de.throsenheim.psta.exceptions.GameConfigurationException;
import de.throsenheim.psta.exceptions.InvalidCreatureStateException;
import de.throsenheim.psta.model.Battlefield;
import de.throsenheim.psta.model.Team;
import de.throsenheim.psta.model.creatures.Warrior;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for exception handling.
 * Verifies that custom exceptions are thrown and caught correctly.
 */
@DisplayName("Exception Handling Tests")
class ExceptionHandlingTest {
    
    @Test
    @DisplayName("InvalidCreatureStateException - negative health")
    void testNegativeHealthThrowsException() {
        Random random = new Random(100);
        
        InvalidCreatureStateException exception = assertThrows(
            InvalidCreatureStateException.class,
            () -> new Warrior("Invalid", -10, 20, 5, 6, Team.HEROES, random),
            "Creating creature with negative health should throw exception"
        );
        
        assertTrue(exception.getMessage().contains("Health must be positive"),
                  "Exception message should mention health");
    }
    
    @Test
    @DisplayName("InvalidCreatureStateException - zero health")
    void testZeroHealthThrowsException() {
        Random random = new Random(200);
        
        InvalidCreatureStateException exception = assertThrows(
            InvalidCreatureStateException.class,
            () -> new Warrior("Invalid", 0, 20, 5, 6, Team.HEROES, random),
            "Creating creature with zero health should throw exception"
        );
        
        assertNotNull(exception.getMessage());
    }
    
    @Test
    @DisplayName("InvalidCreatureStateException - negative attack power")
    void testNegativeAttackThrowsException() {
        Random random = new Random(300);
        
        InvalidCreatureStateException exception = assertThrows(
            InvalidCreatureStateException.class,
            () -> new Warrior("Invalid", 100, -5, 5, 6, Team.HEROES, random),
            "Creating creature with negative attack should throw exception"
        );
        
        assertTrue(exception.getMessage().contains("Attack power cannot be negative"),
                  "Exception message should mention attack power");
    }
    
    @Test
    @DisplayName("InvalidCreatureStateException - negative defense")
    void testNegativeDefenseThrowsException() {
        Random random = new Random(400);
        
        InvalidCreatureStateException exception = assertThrows(
            InvalidCreatureStateException.class,
            () -> new Warrior("Invalid", 100, 20, -3, 6, Team.HEROES, random),
            "Creating creature with negative defense should throw exception"
        );
        
        assertTrue(exception.getMessage().contains("Defense cannot be negative"),
                  "Exception message should mention defense");
    }
    
    @Test
    @DisplayName("InvalidCreatureStateException - null name")
    void testNullNameThrowsException() {
        Random random = new Random(500);
        
        InvalidCreatureStateException exception = assertThrows(
            InvalidCreatureStateException.class,
            () -> new Warrior(null, 100, 20, 5, 6, Team.HEROES, random),
            "Creating creature with null name should throw exception"
        );
        
        assertTrue(exception.getMessage().contains("name cannot be null"),
                  "Exception message should mention name");
    }
    
    @Test
    @DisplayName("InvalidCreatureStateException - empty name")
    void testEmptyNameThrowsException() {
        Random random = new Random(600);
        
        InvalidCreatureStateException exception = assertThrows(
            InvalidCreatureStateException.class,
            () -> new Warrior("", 100, 20, 5, 6, Team.HEROES, random),
            "Creating creature with empty name should throw exception"
        );
        
        assertNotNull(exception.getMessage());
    }
    
    @Test
    @DisplayName("InvalidCreatureStateException - null team")
    void testNullTeamThrowsException() {
        Random random = new Random(700);
        
        InvalidCreatureStateException exception = assertThrows(
            InvalidCreatureStateException.class,
            () -> new Warrior("Hero", 100, 20, 5, 6, null, random),
            "Creating creature with null team should throw exception"
        );
        
        assertTrue(exception.getMessage().contains("Team cannot be null"),
                  "Exception message should mention team");
    }
    
    @Test
    @DisplayName("GameConfigurationException - invalid max rounds")
    void testInvalidMaxRoundsThrowsException() {
        GameConfigurationException exception = assertThrows(
            GameConfigurationException.class,
            () -> new Battlefield(0),
            "Creating battlefield with 0 max rounds should throw exception"
        );
        
        assertTrue(exception.getMessage().contains("Max rounds must be positive"),
                  "Exception message should mention max rounds");
    }
    
    @Test
    @DisplayName("GameConfigurationException - negative max rounds")
    void testNegativeMaxRoundsThrowsException() {
        GameConfigurationException exception = assertThrows(
            GameConfigurationException.class,
            () -> new Battlefield(-5),
            "Creating battlefield with negative max rounds should throw exception"
        );
        
        assertNotNull(exception.getMessage());
    }
    
    @Test
    @DisplayName("GameConfigurationException - duplicate creature names")
    void testDuplicateCreatureNamesThrowsException() throws InvalidCreatureStateException {
        Random random = new Random(800);
        Battlefield battlefield = new Battlefield(10);
        
        Warrior warrior1 = new Warrior("Duplicate", 100, 20, 5, 6, Team.HEROES, random);
        Warrior warrior2 = new Warrior("Duplicate", 80, 25, 4, 5, Team.MONSTERS, random);
        
        battlefield.addCreature(warrior1);
        
        GameConfigurationException exception = assertThrows(
            GameConfigurationException.class,
            () -> battlefield.addCreature(warrior2),
            "Adding creature with duplicate name should throw exception"
        );
        
        assertTrue(exception.getMessage().contains("Duplicate creature name"),
                  "Exception message should mention duplicate name");
    }
    
    @Test
    @DisplayName("GameConfigurationException - adding null creature")
    void testNullCreatureThrowsException() {
        Battlefield battlefield = new Battlefield(10);
        
        GameConfigurationException exception = assertThrows(
            GameConfigurationException.class,
            () -> battlefield.addCreature(null),
            "Adding null creature should throw exception"
        );
        
        assertTrue(exception.getMessage().contains("Cannot add null creature"),
                  "Exception message should mention null creature");
    }
    
    @Test
    @DisplayName("GameConfigurationException - starting battle with no creatures")
    void testEmptyBattlefieldThrowsException() {
        Battlefield battlefield = new Battlefield(10);
        
        GameConfigurationException exception = assertThrows(
            GameConfigurationException.class,
            () -> battlefield.startBattle(),
            "Starting battle with no creatures should throw exception"
        );
        
        assertTrue(exception.getMessage().contains("no creatures"),
                  "Exception message should mention no creatures");
    }
    
    @Test
    @DisplayName("GameConfigurationException - starting battle with only one team")
    void testSingleTeamThrowsException() throws InvalidCreatureStateException {
        Random random = new Random(900);
        Battlefield battlefield = new Battlefield(10);
        
        // Add only heroes, no monsters
        battlefield.addCreature(new Warrior("Hero1", 100, 20, 5, 6, Team.HEROES, random));
        battlefield.addCreature(new Warrior("Hero2", 100, 20, 5, 6, Team.HEROES, random));
        
        GameConfigurationException exception = assertThrows(
            GameConfigurationException.class,
            () -> battlefield.startBattle(),
            "Starting battle with only one team should throw exception"
        );
        
        assertTrue(exception.getMessage().contains("Both teams must have"),
                  "Exception message should mention both teams");
    }
    
    @Test
    @DisplayName("Valid creature creation does not throw exception")
    void testValidCreatureCreation() {
        Random random = new Random(1000);
        
        assertDoesNotThrow(
            () -> new Warrior("ValidHero", 100, 25, 8, 7, Team.HEROES, random),
            "Creating valid creature should not throw exception"
        );
    }
    
    @Test
    @DisplayName("Valid battlefield creation does not throw exception")
    void testValidBattlefieldCreation() {
        assertDoesNotThrow(
            () -> new Battlefield(20),
            "Creating valid battlefield should not throw exception"
        );
    }
    
    @Test
    @DisplayName("Exception with cause")
    void testExceptionWithCause() {
        Throwable cause = new RuntimeException("Original cause");
        
        InvalidCreatureStateException exception = new InvalidCreatureStateException(
            "Wrapper message", cause
        );
        
        assertEquals("Wrapper message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    @DisplayName("GameConfigurationException is RuntimeException")
    void testGameConfigurationExceptionIsRuntimeException() {
        GameConfigurationException exception = new GameConfigurationException("Test");
        
        assertTrue(exception instanceof RuntimeException,
                  "GameConfigurationException should be a RuntimeException");
    }
    
    @Test
    @DisplayName("InvalidCreatureStateException is checked Exception")
    void testInvalidCreatureStateExceptionIsCheckedException() {
        InvalidCreatureStateException exception = new InvalidCreatureStateException("Test");
        
        assertTrue(exception instanceof Exception,
                  "InvalidCreatureStateException should be an Exception");
        // Checked exceptions do not extend RuntimeException
        assertFalse(RuntimeException.class.isAssignableFrom(exception.getClass()),
                   "InvalidCreatureStateException should not be a RuntimeException (it's checked)");
    }
}
