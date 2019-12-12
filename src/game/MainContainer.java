package game;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

/**
 * Has the main container of the game.
 */
class MainContainer {

    private ContainerController mainContainer;

    /**
     * Create main container of the game.
     */
    MainContainer() {

        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        p1.setParameter(Profile.CONTAINER_NAME, "GameContainer");
//        p1.setParameter(Profile.GUI, "true");
        mainContainer = rt.createMainContainer(p1);
    }

    /**
     * Create a new agent.
     *
     * @param name      name of the agent.
     * @param className name of class of the agent.
     * @return New Agent
     * @throws StaleProxyException
     */
    AgentController createAgent(String name, String className) throws StaleProxyException {
        Object[] args = new Object[0];
        return mainContainer.createNewAgent(name, className, args);
    }

    /**
     * Create a new agent.
     *
     * @param name          name of the agent.
     * @param className     name of class of the agent.
     * @param player_amount number of players of the game.
     * @param width         width of the map of the game.
     * @param height        height of the map of the game.
     * @return New Agent.
     * @throws StaleProxyException
     */
    AgentController createAgent(String name, String className, int player_amount, int width, int height, int e, int m, int r, ArrayList<String> types) throws StaleProxyException {
        Object[] args = new Object[7];
        args[0] = player_amount;
        args[1] = width;
        args[2] = height;
        args[3] = e;
        args[4] = m;
        args[5] = r;
        args[6] = types;
        return mainContainer.createNewAgent(name, className, args);
    }
}
