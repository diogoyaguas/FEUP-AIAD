package main;

import agents.GameController;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.Scanner;

import static game.Game.addAgents;

public class Main{
    public static void main(String[] args) {

        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        p1.setParameter(Profile.CONTAINER_NAME, "GameContainer");
        p1.setParameter(Profile.GUI,"true");
        ContainerController mainContainer = rt.createMainContainer(p1);

        Object[] agentArgs = new Object[0];
        try {
            AgentController gc = mainContainer.createNewAgent("GameController","agents.GameController", agentArgs);
            gc.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

        Scanner number = new Scanner( System.in );

        System.out.print( "Enter number of players: (Maximum: 10)\n:::" );
        int players = number.nextInt();

        while(players >= 10 || (players <= 1)) {
            System.out.print( "Enter valid number of players: (Maximum: 10)\n::: " );
            players = number.nextInt();
        }

        addAgents(players);

    }

}