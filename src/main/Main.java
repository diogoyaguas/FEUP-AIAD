package main;

import game.Game;
import jade.core.*;
import jade.wrapper.StaleProxyException;

public class Main{
    public static void main(String[] args) throws StaleProxyException, ProfileException {
        System.out.println("Game Started");
        Game game = new Game(3);
        game.startAllAgents();
    }
}
