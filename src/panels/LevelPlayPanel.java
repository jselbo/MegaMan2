package panels;

import application.GameState;
import application.GameTransitionEvent;
import application.GameTransitionEventKey;
import graphics.objects.Megaman;
import graphics.objects.bosses.BossType;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;

public class LevelPlayPanel extends GamePanel {
  private BossType bossType;
  private Megaman hero;

  public LevelPlayPanel(GameState gameState) {
    super(gameState);

    setBackground(Color.BLACK);
  }

  @Override
  public void prepareForUpdates(GameTransitionEvent transitionEvent) {
    bossType = (BossType) transitionEvent.getValue(GameTransitionEventKey.LEVEL_SELECT_BOSS_TYPE);

    hero = new Megaman();

    String levelStr;
    
  }

  @Override
  public void updateGame(long elapsedTime) {
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    hero.paint(g2);
  }
}
