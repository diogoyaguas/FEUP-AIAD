package agents.types;

import agents.GameAgent;
import agents.behaviours.single.Test;
import game.Game;

public class MilitarAgent extends GameAgent {

    @Override
    public void start() {
        System.out.println("Test Agent Hello with name :" + getLocalName());
        //System.out.println(Game.singleton);
        addBehaviour(new Test());
    }

    public void takeDown()
    {
        System.out.println("Exiting");
    }
}
