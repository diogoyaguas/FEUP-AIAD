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
        p2.setParameter(Profile.GUI,"true");
        container = rt.createAgentContainer(p2);
    }

    /**
     *
     * @return Returns AgentController is accepts a new agent or null otherwise
     */
    public AgentController addAgentContainer(String name,String agent_type)
    {
        try {
            Object[] agentArgs = new Object[0];
            return container.createNewAgent(name, agent_type, agentArgs);
        } catch (StaleProxyException e) {
            return null;
        }
    }
}
