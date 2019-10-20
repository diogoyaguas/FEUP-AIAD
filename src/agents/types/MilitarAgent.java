package agents.types;

import agents.GameAgent;

public class MilitarAgent extends GameAgent {
    @Override
    public void setup() {
        System.out.println("Test Agent Hello with name :" + getLocalName());
    }
}
