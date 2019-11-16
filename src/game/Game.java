package game;

import com.sun.xml.internal.ws.util.StringUtils;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private MainContainer main_container;
    private AgentController sniffer;
    private AgentController game_controller;
    private Container game_container;
    private ArrayList<AgentController> players;
    private int player_amount=3;
    private int width;
    private int height;

    /**
     * Creates a new game. Doesn't start it
     */
    public Game()
    {
        this.readPlayerInput();
        main_container = new MainContainer();
        game_container = new Container("PlayersContainer");
        players = new ArrayList<>();
        try {

            sniffer = main_container.createAgent("Sniffer", "jade.tools.sniffer.Sniffer");
            sniffer.start();
            game_controller = main_container.createAgent("GameController","agents.GameController",player_amount,width,height);
            game_controller.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Reads the user desired number of player, width and height
     */
    private void readPlayerInput()
    {
        Scanner number = new Scanner( System.in );
        System.out.print( "Enter number of players: (Maximum: 10)\n::: " );
        String playersString = number.next();
        while(!playersString.matches("^[0-9]+$")) {
            System.out.print( "\nEnter valid number of players: (Maximum: 10)\n::: " );
            playersString = number.next();
        }
        int playersNumber = Integer.parseInt(playersString);
        if(playersNumber > 10) {
            System.out.println("\n<<< Invalid number of players. Assuming 10 players. >>>\n");
            playersNumber = 10;
        } else if(playersNumber < 2) {
            System.out.println("\n<<< Invalid number of players. Assuming 2 players. >>>\n");
            playersNumber = 2;
        }
        this.player_amount = playersNumber;

        System.out.print( "\nEnter the size of the map (width & height): \n::: " );
        String width = number.next();
        System.out.print("::: ");
        String height = number.next();
        while(!width.matches("^[0-9]+$") || !height.matches("^[0-9]+$")) {
            System.out.print( "\nEnter valid size of the map: \n::: " );
            width = number.next();
            System.out.print("::: ");
            height = number.next();
        }

        this.width= Integer.parseInt(width);
        this.height= Integer.parseInt(height);

    }

    /**
     * Start the game by creating new agents. Doesn't start agents
     */
    public void start()
    {
        this.addAgents(player_amount);
        this.startAgents();
    }

    /**
     * Adds new agents to the game
     * @param numberPlayers The number of agents I want to add. Type will be randomly generated
     */
    private void addAgents(int numberPlayers) {
        int counter = 0;
        while (counter < numberPlayers) {
            AgentController agent = game_container.addAgent("Player" + ++counter, getRandomPlayerType(),width,height);
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
