package application;

import javax.swing.JFrame;

public class Main {
  public static void main(String[] args) {
    JFrame frame = new JFrame("Mega Man 2");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    GameManager gameManager = new GameManager(frame);
    frame.setJMenuBar(gameManager.getJMenuBar());

    frame.pack();
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    frame.setResizable(true);

    gameManager.start();
  }
}