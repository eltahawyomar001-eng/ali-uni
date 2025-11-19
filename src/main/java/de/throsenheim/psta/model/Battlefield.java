package de.throsenheim.psta.model;

import de.throsenheim.psta.exceptions.GameConfigurationException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Battlefield - runs the battle simulation.
 */
public class Battlefield {
    
    private final List<Creature> allCreatures;
    private final Map<Team, List<Creature>> teamMap;
    private final int maxRounds;
    private int currentRound;
    private Team winnerTeam;
    
    public Battlefield(int maxRounds) {
        if (maxRounds <= 0) {
            throw new GameConfigurationException("Max rounds must be positive, got: " + maxRounds);
        }
        
        this.allCreatures = new ArrayList<>();
        this.teamMap = new HashMap<>();
        this.maxRounds = maxRounds;
        this.currentRound = 0;
        this.winnerTeam = null;
        
        // Initialize team lists
        teamMap.put(Team.HEROES, new ArrayList<>());
        teamMap.put(Team.MONSTERS, new ArrayList<>());
    }
    
    // Add a creature to the battlefield
    public void addCreature(Creature creature) {
        if (creature == null) {
            throw new GameConfigurationException("Cannot add null creature");
        }
        
        // Check for duplicate names
        boolean duplicateName = allCreatures.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(creature.getName()));
        
        if (duplicateName) {
            throw new GameConfigurationException(
                "Duplicate creature name: " + creature.getName());
        }
        
        allCreatures.add(creature);
        teamMap.get(creature.getTeam()).add(creature);
    }
    
    // Runs the battle
    public void startBattle() {
        if (allCreatures.isEmpty()) {
            throw new GameConfigurationException("Cannot start battle with no creatures");
        }
        
        // Verify both teams have creatures
        if (teamMap.get(Team.HEROES).isEmpty() || teamMap.get(Team.MONSTERS).isEmpty()) {
            throw new GameConfigurationException("Both teams must have at least one creature");
        }
        
        System.out.println("=".repeat(70));
        System.out.println("BATTLE BEGINS!");
        System.out.println("=".repeat(70));
        printTeams();
        System.out.println();
        
        // Main battle loop
        while (currentRound < maxRounds && !isBattleOver()) {
            currentRound++;
            executeRound();
        }
        
        // Determine and announce winner
        determineWinner();
        printBattleResults();
    }
    
    /**
     * Executes a single round of combat.
     * Creatures act in order of initiative (using Comparable implementation).
     */
    private void executeRound() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ROUND " + currentRound);
        System.out.println("=".repeat(70));
        
        // Get living creatures and sort by initiative (natural ordering via Comparable)
        List<Creature> actingCreatures = allCreatures.stream()
                .filter(Creature::isAlive)
                .sorted() // Uses Creature.compareTo() - higher initiative first
                .collect(Collectors.toList());
        
        // Each creature performs its action
        for (Creature creature : actingCreatures) {
            if (creature.isAlive() && !isBattleOver()) {
                System.out.println("\n[" + creature.getName() + "'s turn]");
                creature.performRoundAction(this);
            }
        }
        
        printRoundSummary();
    }
    
    /**
     * Checks if the battle is over (one team eliminated).
     * 
     * @return true if battle is over, false otherwise
     */
    private boolean isBattleOver() {
        boolean heroesAlive = teamMap.get(Team.HEROES).stream().anyMatch(Creature::isAlive);
        boolean monstersAlive = teamMap.get(Team.MONSTERS).stream().anyMatch(Creature::isAlive);
        
        return !heroesAlive || !monstersAlive;
    }
    
    /**
     * Determines the winning team.
     */
    private void determineWinner() {
        boolean heroesAlive = teamMap.get(Team.HEROES).stream().anyMatch(Creature::isAlive);
        boolean monstersAlive = teamMap.get(Team.MONSTERS).stream().anyMatch(Creature::isAlive);
        
        if (heroesAlive && !monstersAlive) {
            winnerTeam = Team.HEROES;
        } else if (monstersAlive && !heroesAlive) {
            winnerTeam = Team.MONSTERS;
        } else if (currentRound >= maxRounds) {
            // Tie or max rounds reached - team with more HP wins
            int heroesHP = teamMap.get(Team.HEROES).stream()
                    .mapToInt(Creature::getHealth)
                    .sum();
            int monstersHP = teamMap.get(Team.MONSTERS).stream()
                    .mapToInt(Creature::getHealth)
                    .sum();
            
            winnerTeam = heroesHP > monstersHP ? Team.HEROES : Team.MONSTERS;
        }
    }
    
    /**
     * Prints the initial team compositions.
     */
    private void printTeams() {
        System.out.println("\nTEAM HEROES:");
        teamMap.get(Team.HEROES).forEach(c -> 
            System.out.println("  - " + c.getName() + " (" + c.getClass().getSimpleName() + 
                             ") - HP: " + c.getHealth() + ", ATK: " + c.getAttackPower() + 
                             ", DEF: " + c.getDefense() + ", INI: " + c.getInitiative()));
        
        System.out.println("\nTEAM MONSTERS:");
        teamMap.get(Team.MONSTERS).forEach(c -> 
            System.out.println("  - " + c.getName() + " (" + c.getClass().getSimpleName() + 
                             ") - HP: " + c.getHealth() + ", ATK: " + c.getAttackPower() + 
                             ", DEF: " + c.getDefense() + ", INI: " + c.getInitiative()));
    }
    
    /**
     * Prints a summary of the current round.
     */
    private void printRoundSummary() {
        System.out.println("\n--- Round " + currentRound + " Summary ---");
        
        System.out.println("HEROES:");
        teamMap.get(Team.HEROES).forEach(c -> {
            if (c.isAlive()) {
                System.out.println("  " + c.getName() + ": " + c.getHealth() + "/" + c.getMaxHealth() + " HP");
            } else {
                System.out.println("  " + c.getName() + ": DEFEATED");
            }
        });
        
        System.out.println("MONSTERS:");
        teamMap.get(Team.MONSTERS).forEach(c -> {
            if (c.isAlive()) {
                System.out.println("  " + c.getName() + ": " + c.getHealth() + "/" + c.getMaxHealth() + " HP");
            } else {
                System.out.println("  " + c.getName() + ": DEFEATED");
            }
        });
    }
    
    /**
     * Prints the final battle results.
     */
    private void printBattleResults() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("BATTLE ENDED!");
        System.out.println("=".repeat(70));
        System.out.println("Total Rounds: " + currentRound);
        
        if (winnerTeam != null) {
            System.out.println("WINNER: TEAM " + winnerTeam);
        } else {
            System.out.println("RESULT: DRAW");
        }
        
        long heroesAlive = teamMap.get(Team.HEROES).stream().filter(Creature::isAlive).count();
        long monstersAlive = teamMap.get(Team.MONSTERS).stream().filter(Creature::isAlive).count();
        
        System.out.println("\nSurvivors:");
        System.out.println("  Heroes: " + heroesAlive + "/" + teamMap.get(Team.HEROES).size());
        System.out.println("  Monsters: " + monstersAlive + "/" + teamMap.get(Team.MONSTERS).size());
        
        System.out.println("\nFinal Status:");
        allCreatures.stream()
                .filter(Creature::isAlive)
                .forEach(c -> System.out.println("  " + c.getName() + " (" + c.getTeam() + "): " + 
                                               c.getHealth() + "/" + c.getMaxHealth() + " HP"));
        
        System.out.println("=".repeat(70));
    }
    
    /**
     * Gets all living enemies of the specified creature.
     * 
     * @param creature the creature whose enemies to find
     * @return list of living enemy creatures
     */
    public List<Creature> getEnemies(Creature creature) {
        Team enemyTeam = creature.getTeam() == Team.HEROES ? Team.MONSTERS : Team.HEROES;
        return teamMap.get(enemyTeam).stream()
                .filter(Creature::isAlive)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets all living allies of the specified creature (excluding the creature itself).
     * 
     * @param creature the creature whose allies to find
     * @return list of living ally creatures
     */
    public List<Creature> getAllies(Creature creature) {
        return teamMap.get(creature.getTeam()).stream()
                .filter(Creature::isAlive)
                .filter(c -> !c.equals(creature))
                .collect(Collectors.toList());
    }
    
    // Getters
    
    public int getCurrentRound() {
        return currentRound;
    }
    
    public int getMaxRounds() {
        return maxRounds;
    }
    
    public Team getWinnerTeam() {
        return winnerTeam;
    }
    
    public List<Creature> getAllCreatures() {
        return Collections.unmodifiableList(allCreatures);
    }
    
    public List<Creature> getTeamCreatures(Team team) {
        return Collections.unmodifiableList(teamMap.get(team));
    }
    
    /**
     * Gets the count of living creatures for a team.
     * 
     * @param team the team to count
     * @return number of living creatures
     */
    public long getLivingCount(Team team) {
        return teamMap.get(team).stream().filter(Creature::isAlive).count();
    }
    
    /**
     * Gets the count of dead creatures for a team.
     * 
     * @param team the team to count
     * @return number of dead creatures
     */
    public long getDeadCount(Team team) {
        return teamMap.get(team).stream().filter(c -> !c.isAlive()).count();
    }
}
