package game;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Container {

    private ContainerController container;

    public Container(String name) {
        Runtime rt = Runtime.instance();
        Profile p2 = new ProfileImpl();
        p2.setParameter(Profile.CONTAINER_NAME, name);
        p2.setParameter(Profile.GUI, "true");
        container = rt.createAgentContainer(p2);
    }

    AgentController addAgent(String name, String agentType, int width, int height) {
        try {
            Object[] args = new Object[2];
            args[0] = width;
            args[1] = height;
            return container.createNewAgent(name, agentType, args);
        } catch (StaleProxyException ignored) {
            return null;
        }
    }
}
