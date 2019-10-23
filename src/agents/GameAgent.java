package agents;

import jade.core.Agent;

public abstract class GameAgent extends Agent {

    public void setup()
    {
        //Comunicar o game controller da minha existencia para me ser atribuida uma cidade
        start();
    }
    public abstract void start();
    public abstract void takeDown();
}
