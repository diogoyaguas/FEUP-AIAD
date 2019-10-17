package main;

import agents.ExampleAgent;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main{
    public static void main(String[] args) throws StaleProxyException, ProfileException {
        System.out.println("Hello world");
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        p1.setParameter(Profile.GUI,"true");
        ContainerController mainContainer = rt.createMainContainer(p1);
        Profile p2 = new ProfileImpl();
        p2.setParameter(Profile.GUI,"true");
        ContainerController container = rt.createAgentContainer(p2);
        AgentController ac1 = container.acceptNewAgent("name1", new ExampleAgent());
        ac1.start();
        Object[] agentArgs = new Object[0];
        AgentController ac2 = container.createNewAgent("name2", "agents.ExampleAgent", agentArgs);
        ac2.start();
    }
}
