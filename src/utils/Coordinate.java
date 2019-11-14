package utils;

import java.util.ArrayList;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {return x;}
    public int y() {return y;}

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
        ret.add(new Coordinate(x-1,y));
        ret.add(new Coordinate(x+1,y));
        ret.add(new Coordinate(x,y-1));
        ret.add(new Coordinate(x,y+1));
        return ret;
    }
}
