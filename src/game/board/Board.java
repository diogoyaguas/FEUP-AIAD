package game.board;

import agents.GameAgent;

import java.util.ArrayList;
import java.util.Random;

public class Board {

    private ArrayList<ArrayList<City>> board;
    private int width;
    private int height;

    public Board(int width, int height) {
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

    public City getRegion(int x, int y) {
        return this.board.get(x).get(y);
    }

    public void setRegion(int x, int y, City region) {
        this.board.get(x).set(y, region);
    }

    public void setCityOwner(int x, int y, GameAgent owner) {
        this.board.get(x).get(y).setOwner(owner);
    }

    public ArrayList<ArrayList<City>> getBoard() {
        return this.board;
    }

    public int[] getRandomAvailable() {
        Random r = new Random();

        int w = r.nextInt(width), h = r.nextInt(height);
//        if(board.get(w).get(h).getOwner() == null) return getRandomAvailable();
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
