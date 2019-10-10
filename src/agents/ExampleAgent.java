package agents;

import jade.core.Agent;

public class ExampleAgent extends Agent {
    public void setup()
    {
        System.out.println("Test Agent Hello with name :" + getLocalName());
    }
}
