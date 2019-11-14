package game;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Container {

    private ContainerController container;

    public Container()
    {
        Runtime rt = Runtime.instance();
        Profile p2 = new ProfileImpl();
        p2.setParameter(Profile.CONTAINER_NAME, "PlayersContainer");
        p2.setParameter(Profile.GUI,"true");
        container = rt.createAgentContainer(p2);
    }
    
    void addAgent(String name, String agentType)
    {
        try {
            Object[] agentArgs = new Object[0];
            AgentController pc = container.createNewAgent(name, agentType, agentArgs);
            pc.start();
        } catch (StaleProxyException ignored) {
        }
    }
}
