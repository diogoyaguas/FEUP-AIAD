package game;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private MainContainer main_container;
    private AgentController sniffer;
    private AgentController game_controller;
    private Container game_container;
    private ArrayList<AgentController> players;

    /**
     * Creates a new game. Doesn't start it
     */
    public Game()
    {
        main_container = new MainContainer();
        game_container = new Container("PlayersContainer");
        players = new ArrayList<AgentController>();
        try {

            sniffer = main_container.createAgent("Sniffer", "jade.tools.sniffer.Sniffer");
            sniffer.start();
            game_controller = main_container.createAgent("GameController","agents.GameController");
            game_controller.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Start the game by creating new agents. Doesn't start agents
     */
    public void start()
    {
        Scanner number = new Scanner( System.in );
        System.out.print( "Enter number of players: (Maximum: 10)\n:::" );
        int players = number.nextInt();
        while(players >= 10 || (players <= 1)) {
            System.out.print( "Enter valid number of players: (Maximum: 10)\n::: " );
            players = number.nextInt();
        }
        this.addAgents(players);
        this.startAgents();
    }

    /**
     * Adds new agents to the game
     * @param numberPlayers The number of agents I want to add. Type will be randomly generated
     */
    private void addAgents(int numberPlayers) {
        int counter = 0;
        while (counter < numberPlayers) {
            AgentController agent = game_container.addAgent("Player" + ++counter, getRandomPlayerType());
            if(agent!=null)
            {
                players.add(agent);
            }
            else
            {
                System.err.println("Error has ocurred while trying to create new players");
                System.exit(2);
            }
        }
    }

    /**
     * Starts all game agents
     */
    private void startAgents()
    {
        try {
            for (AgentController player:players) {
                    player.start();
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }

    /**
     * Creates a random agent type
     * @return The type of agent created
     */
    private String getRandomPlayerType() {
        Random rnd = new Random();
        int type = rnd.nextInt(3) + 1;
        switch (type) {
            case 1: return "agents.types.MilitaryAgent";
            case 2: return "agents.types.EconomicsAgent";
            case 3: return "agents.types.ReligiousAgent";
        }
        return null;
    }
}
