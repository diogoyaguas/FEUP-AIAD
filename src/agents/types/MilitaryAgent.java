package agents.types;

import agents.GameAgent;

public class MilitaryAgent extends GameAgent {

    @Override
    public void start() {
        System.out.println("Military Agent  with name :" + getLocalName());
        //System.out.println(Game.singleton);
    }

    public void takeDown() {
        System.out.println("Exiting");
    }
}
