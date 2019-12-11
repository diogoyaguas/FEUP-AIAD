package game;

import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private MainContainer main_container;
    private Container game_container;

    private AgentController sniffer;
    private AgentController game_controller;

    private ArrayList<AgentController> players;

    private int player_amount = 3;
    private int width;
    private int height;

    private int economicsNumber = 0;
    private int militaryNumber = 0;
    private int religiousNumber = 0;
    private ArrayList<String> types = new ArrayList<>(player_amount);

    /**
     * Creates a new game by creating the main and game container.
     */
    public Game() {
        this.readPlayerInput();
        helper();
    }

    public Game(int players, int width, int height) {
        this.player_amount = players;
        this.width = width;
        this.height = height;
        System.out.println(this.player_amount + " players, " + this.width + " width, " + this.height + " height");
        helper();
    }

    public void helper() {
        main_container = new MainContainer();
        game_container = new Container("PlayersContainer");
        players = new ArrayList<>();
        for(int i = 0; i < player_amount; i++) {
            types.add(getRandomPlayerType());
        }
        try {
            game_controller = main_container.createAgent("GameController", "agents.GameController", player_amount, width, height, economicsNumber, militaryNumber, religiousNumber, types);
            game_controller.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Reads the user desired number of player, and width and height of the map.
     */
    private void readPlayerInput() {
        Scanner number = new Scanner(System.in);
        System.out.print("Enter number of players: (Maximum: 10)\n::: ");
        String playersString = number.next();
        while (!playersString.matches("^[0-9]+$")) {
            System.out.print("\nEnter valid number of players: (Maximum: 10)\n::: ");
            playersString = number.next();
        }
        int playersNumber = Integer.parseInt(playersString);
        if (playersNumber > 10) {
            System.out.println("\n<<< Invalid number of players. Assuming 10 players. >>>\n");
            playersNumber = 10;
        } else if (playersNumber < 2) {
            System.out.println("\n<<< Invalid number of players. Assuming 2 players. >>>\n");
            playersNumber = 2;
        }
        this.player_amount = playersNumber;

        System.out.print("\nEnter the size of the map (width & height, max: 20x20): \n::: ");
        String width = number.next();
        System.out.print("::: ");
        String height = number.next();
        while (!width.matches("^[0-9]+$") || !height.matches("^[0-9]+$") || Integer.parseInt(width) > 20 || Integer.parseInt(height) > 20) {
            System.out.print("\nEnter valid size of the map: \n::: ");
            width = number.next();
            System.out.print("::: ");
            height = number.next();
        }
        this.width = Integer.parseInt(width);
        this.height = Integer.parseInt(height);

        if (this.width < Math.sqrt(this.player_amount) || this.height < Math.sqrt(this.player_amount)) {
            this.width = (int) (Math.ceil(Math.sqrt(this.player_amount)));
            this.height = (int) (Math.ceil(Math.sqrt(this.player_amount)));
        }

    }

    /**
     * Start the game by creating new agents.
     */
    public void start() {
        this.addAgents(player_amount);
        this.startAgents();
    }

    /**
     * Adds new agents to the game
     *
     * @param numberPlayers The number of agents I want to add. Type will be randomly generated
     */
    private void addAgents(int numberPlayers) {
        int counter = 0;
        while (counter < numberPlayers) {
            String type = types.get(counter);
            AgentController agent = game_container.addAgent("Player" + counter, type, width, height);
            if (agent != null) {
                players.add(agent);
                System.out.println("Player" + counter + ": " + type);
            } else {
                System.err.println("Error has ocurred while trying to create new players");
                System.exit(2);
            }
            counter++;
        }

    }

    /**
     * Starts all game agents
     */
    private void startAgents() {
        try {
            for (AgentController player : players) {
                player.start();
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }

    /**
     * Creates a random agent type
     *
     * @return The type of agent created
     */
    private String getRandomPlayerType() {
        Random rnd = new Random();
        int type = rnd.nextInt(3) + 1;
        switch (type) {
            case 1:
                militaryNumber++;
                return "agents.types.MilitaryAgent";
            case 2:
                economicsNumber++;
                return "agents.types.EconomicsAgent";
            case 3:
                religiousNumber++;
                return "agents.types.ReligiousAgent";
        }
        return null;
    }
}
