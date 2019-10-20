package agents.types;

import agents.GameAgent;
import agents.behaviours.single.Test;

public class MilitarAgent extends GameAgent {
    @Override
    public void setup() {
        System.out.println("Test Agent Hello with name :" + getLocalName());
        addBehaviour(new Test());
    }

    public void takeDown()
    {
        System.out.println("Exiting");
    }
}
