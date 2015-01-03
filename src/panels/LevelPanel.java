package panels;

import application.GameState;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class LevelPanel extends GamePanel {
  private static final long serialVersionUID = 1L;

  public LevelPanel(GameState state) {
    super(state);
  }

  @Override
  public void start() {
    super.start();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
  }

  @Override
  public void updateGame(long elapsedTime) {
  }
}