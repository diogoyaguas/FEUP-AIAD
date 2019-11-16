package game.gui;

import game.board.Board;

import javax.swing.*;
import java.awt.*;


public class GameGUI {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private JFrame frame;
    private BoardPanel boardPanel;
    private JScrollPane scrollPane;
    private JPanel panel;
    private JList list;

    public GameGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        frame.add(panel);

        boardPanel = new BoardPanel();

        list = new JList(new DefaultListModel<String>());
        scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.add(boardPanel);
        panel.add(scrollPane);

        frame.pack();
    }

    public void setBoard(Board b) {
        boardPanel.setBoard(b);
        boardPanel.repaint();
    }

    public void addAction(String a) {
        ((DefaultListModel) list.getModel()).add(0, a);
    }
}
