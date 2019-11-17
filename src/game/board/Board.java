package game.board;

import jade.core.AID;
import utils.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {

    private final Color defaultColor = new Color(200, 200, 200);

    private ArrayList<ArrayList<City>> board;
    private HashMap<AID, Color> players;
    private int width;
    private int height;

    /**
     * Create a new board game.
     *
     * @param width  width of the map.
     * @param height height of the map.
     */
    public Board(int width, int height) {
        players = new HashMap<>();

        this.width = width;
        this.height = height;
        board = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < height; j++) {
                board.get(i).add(new City(null, new Coordinate(i, j)));
            }
        }
    }

    /**
     * Return city with specific coordinates.
     *
     * @param x x coordinate of the city.
     * @param y y coordinate of the city.
     * @return city with specific coordinate.
     */
    public City getCity(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return null;
        return this.board.get(x).get(y);
    }

    /**
     * Set city with specific coordinates.
     *
     * @param x    x coordinate of the city.
     * @param y    y coordinate of the city.
     * @param city new city to set.
     */
    public void setCity(int x, int y, City city) {
        this.board.get(x).set(y, city);
    }

    /**
     * Set owner of the city with specific coordinates.
     *
     * @param x     x coordinate of the city.
     * @param y     y coordinate of the city.
     * @param owner new owner of the city.
     */
    public void setCityOwner(int x, int y, AID owner) {
        this.board.get(x).get(y).setOwner(owner);
        if (!players.containsKey(owner)) {
            players.put(owner, getRandomColor());
        }
    }

    /**
     * Get new random color.
     *
     * @return new random color.
     */
    private Color getRandomColor() {
        Color c = new Color((int) (Math.random() * 0x1000000));
        if (players.containsValue(c) || c == defaultColor) {
            return getRandomColor();
        }
        return c;
    }

    /**
     * Get color of player.
     *
     * @param x x coordinate of the city.
     * @param y y coordinate of the city.
     * @return color of the player.
     */
    public Color getColor(int x, int y) {
        AID owner = getCity(x, y).getOwner();
        if (players.containsKey(owner))
            return players.get(owner);
        return defaultColor;
    }

    /**
     * Get board.
     *
     * @return actual board.
     */
    public ArrayList<ArrayList<City>> getBoard() {
        return this.board;
    }

    /**
     * Get random coordinates.
     *
     * @return random coordinates.
     */
    public int[] getRandomAvailable() {
        Random r = new Random();

        int w = r.nextInt(width), h = r.nextInt(height);
        if (board.get(w).get(h).getOwner() != null) return getRandomAvailable();
        return new int[]{w, h};
    }

    /**
     * Get width of the map.
     *
     * @return with of the map.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get height of the map.
     *
     * @return height of the map.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get number of cities.
     *
     * @return dimensions of the map.
     */
    public int getNumberOfCities() {
        return width * height;
    }
}
