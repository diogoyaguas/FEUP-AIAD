package game;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Container {

    private ContainerController container;

    public Container(String name)
    {
        Runtime rt = Runtime.instance();
        Profile p2 = new ProfileImpl();
        p2.setParameter(Profile.CONTAINER_NAME, name);
        p2.setParameter(Profile.GUI,"true");
        container = rt.createAgentContainer(p2);
    }

    public AgentController addAgent(String name, String agentType)
    {
        try {
            Object[] agentArgs = new Object[0];
            return container.createNewAgent(name, agentType, agentArgs);
        } catch (StaleProxyException ignored) {
            return null;
        }
    }
}
