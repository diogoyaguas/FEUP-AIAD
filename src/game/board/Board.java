package game.board;

import agents.GameAgent;

import java.util.ArrayList;

public class Board {

    private ArrayList<ArrayList<City>> board;

    public Board(int width,int height)
    {
        board = new ArrayList<ArrayList<City>>();
        for(int i=0;i<width;i++)
        {
            board.add(new ArrayList<City>());
            for(int j=0;j<height;j++)
            {
                board.get(0).add(new City(null));
            }
        }
    }

    public City getRegion(int x,int y)
    {
        return this.board.get(x).get(y);
    }

    public void setRegion(int x,int y,City region)
    {
        this.board.get(x).set(y,region);
    }

    public void setCityOwner(int x, int y, GameAgent owner)
    {
        this.board.get(x).get(y).setOwner(owner);
    }

    public ArrayList<ArrayList<City>> getBoard()
    {
        return this.board;
    }

}
