package game;

import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

public class Game {

    private int agent_number = 1;
    private Container game_container;

    private ArrayList<AgentController> agent_list = new ArrayList<AgentController>();

    public static Game singleton;

    public Game(int number_of_agents) {
        Game.singleton = this;
        game_container = new Container();
        for (int i = 0; i < number_of_agents; i++) {
            createNewAgent("agent" + agent_number, "MilitarAgent");
            agent_number++;
        }
    }

    public void startAllAgents() throws StaleProxyException {
        for (AgentController agent : agent_list) {
            agent.start();
        }
    }

    public void startAgent(int index) throws StaleProxyException {
        agent_list.get(index).start();
    }

    public void createNewAgent(String name, String type) {
        AgentController ac = game_container.addAgentContainer(name, type);
        agent_list.add(ac);
    }
}
