package agents.types;

import agents.GameAgent;

public class ReligiousAgent extends GameAgent {

    @Override
    public void start() {
        System.out.println("Religious Agent Hello with name :" + getLocalName());
        //System.out.println(Game.singleton);
    }

    public void takeDown() {
        System.out.println("Exiting");
    }
}
