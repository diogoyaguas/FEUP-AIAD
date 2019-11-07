package game;

public class Game {

    public static void addAgents(int numberPlayers) {
        int counter = 0;
        Container gameContainer = new Container();

        while (counter < numberPlayers) {
            gameContainer.addAgent("Player" + ++counter, "agents.types.MilitaryAgent");
        }
    }
}
