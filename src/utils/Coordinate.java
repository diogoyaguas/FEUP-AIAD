package utils;

import java.util.ArrayList;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(String xy) {
        String[] c = xy.split("_");
        this.x = Integer.parseInt(c[0]);
        this.y = Integer.parseInt(c[1]);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinate getTop(int width, int height) {
        if (y - 1 < 0) return null;
        return new Coordinate(x, y - 1);
    }

    public Coordinate getButton(int width, int height) {
        if (y + 1 >= height) return null;
        return new Coordinate(x, y + 1);
    }

    public Coordinate getLeft(int width, int height) {
        if (x - 1 < 0) return null;
        return new Coordinate(x - 1, y);
    }

    public Coordinate getRight(int width, int height) {
        if (x + 1 >= width) return null;
        return new Coordinate(x + 1, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;
    }

    public ArrayList<Coordinate> adjacents() {
        ArrayList ret = new ArrayList();
        ret.add(new Coordinate(x - 1, y));
        ret.add(new Coordinate(x + 1, y));
        ret.add(new Coordinate(x, y - 1));
        ret.add(new Coordinate(x, y + 1));
        return ret;
    }

    @Override
    public String toString() {
        return x + "_" + y;
    }
}
