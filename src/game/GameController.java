package game;

import jade.core.Agent;

public class GameController extends Agent {

    private Game game;

    public void setup()
    {
        this.game=Game.singleton;
        System.out.println("Eu sou o agente que controla este jogo");
        //Começar a ouvir os pedidos dos outros
    }

    public void takedown()
    {

    }
}
