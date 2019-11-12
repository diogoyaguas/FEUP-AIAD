package game;

import java.util.Random;

public class Game {

    public static void addAgents(int numberPlayers) {
        int counter = 0;
        Container gameContainer = new Container();

        while (counter < numberPlayers) {
            gameContainer.addAgent("Player" + ++counter, getRandomPlayerType());
        }

    }

    private static String getRandomPlayerType() {

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
