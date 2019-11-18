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

    /**
     * Create game GUI.
     */
    public GameGUI() {
        initialize();
    }

    /**
     * Initialize game GUI.
     */
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

        list.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String s = (String) value;
                Color color = boardPanel.getColor(s.split(" |@")[0]);
                setForeground(color);

                return c;
            }

        });

        panel.add(boardPanel);
        panel.add(scrollPane);

        frame.pack();
    }

    /**
     * Set board of game GUI.
     *
     * @param b board of GUI.
     */
    public void setBoard(Board b) {
        boardPanel.setBoard(b);
        boardPanel.repaint();
        scrollPane.update(frame.getGraphics());
    }

    /**
     * Add action to game GUI.
     *
     * @param a
     */
    public void addAction(String a) {
        ((DefaultListModel) list.getModel()).add(0, a);
    }
}
