package main;

import game.Container;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;
import jade.wrapper.StaleProxyException;

public class Main{
    public static void main(String[] args) {

        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        p1.setParameter(Profile.GUI,"true");
        ContainerController mainContainer = rt.createMainContainer(p1);

        Object[] agentArgs = new Object[0];
        try {
            AgentController gc = mainContainer.createNewAgent("GameController","agents.GameController", agentArgs);
            gc.start();
//            mainContainer.createNewAgent("p1","agents.types.MilitarAgent", agentArgs);

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
//        addAgents();

    }

    private static void addAgents() {
        int counter = 0;
        Container gameContainer = new Container();

        gameContainer.addAgent("Player" + ++counter, "agents.types.MilitarAgent");
        gameContainer.addAgent("Player" + ++counter, "agents.types.MilitarAgent");
        gameContainer.addAgent("Player" + ++counter, "agents.types.MilitarAgent");
        gameContainer.addAgent("Player" + ++counter, "agents.types.MilitarAgent");
        gameContainer.addAgent("Player" + ++counter, "agents.types.MilitarAgent");
    }
}
