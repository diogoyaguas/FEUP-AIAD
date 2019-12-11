package main;

import game.Game;

public class Main {
    public static void main(String[] args) {
        Game game;
        if(args.length == 3)
            game = new Game(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        else
            game = new Game();
        game.start();

    }

}