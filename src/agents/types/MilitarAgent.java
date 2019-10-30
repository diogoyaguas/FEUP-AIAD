package agents.types;

import agents.GameAgent;

public class MilitarAgent extends GameAgent {

    @Override
    public void start() {
        System.out.println("Test Agent Hello with name :" + getLocalName());
        //System.out.println(Game.singleton);
    }

    public void takeDown()
    {
        System.out.println("Exiting");
    }
}
