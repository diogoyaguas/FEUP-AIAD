package game.gui;

import game.board.Board;

import java.awt.*;

public class BoardPanel extends Canvas {

    private Board board;

    public BoardPanel() {
        super();
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public void update(Graphics g) {
        if(board == null) return;
        super.update(g);
        int addx = this.getWidth() / board.getWidth();
        int addy = this.getHeight() / board.getHeight();

        int x = 0, y = 0;
        for(int i = 0; i < board.getHeight(); i++) {
            for(int j = 0; j < board.getWidth(); j++) {
                g.setColor(board.getColor(i,j));
                g.fillRect(x, y, addx, addy);
                x+=addx;
            }
            x=0;
            y+=addy;
        }
    }

}
