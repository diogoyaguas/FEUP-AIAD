package game.board;

import jade.core.AID;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {

    private final Color defaultColor = new Color(200,200,200);

    private ArrayList<ArrayList<City>> board;
    private HashMap<AID, Color> players;
    private int width;
    private int height;

    public Board(int width, int height) {
        players = new HashMap<>();

        this.width = width;
        this.height = height;
        board = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < height; j++) {
                board.get(i).add(new City(null));
            }
        }
    }

    public City getCity(int x, int y) {
        return this.board.get(x).get(y);
    }

    public void setCity(int x, int y, City city) {
        this.board.get(x).set(y, city);
    }

    public void setCityOwner(int x, int y, AID owner) {
        this.board.get(x).get(y).setOwner(owner);
        if(!players.containsKey(owner)) {
            players.put(owner, getRandomColor());
        }
    }

    private Color getRandomColor() {
        Color c = new Color((int)(Math.random() * 0x1000000));
        if(players.containsValue(c) || c == defaultColor) {
            return getRandomColor();
        }
        return c;
    }

    public Color getColor(int x, int y) {
        AID owner = getCity(x,y).getOwner();
        if(players.containsKey(owner))
            return players.get(owner);
        return defaultColor;
    }

    public ArrayList<ArrayList<City>> getBoard() {
        return this.board;
    }

    public int[] getRandomAvailable() {
        Random r = new Random();

        int w = r.nextInt(width), h = r.nextInt(height);
        if(board.get(w).get(h).getOwner() != null) return getRandomAvailable();
        return new int[]{w, h};
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumberOfCities() {
        return width * height;
    }
}
