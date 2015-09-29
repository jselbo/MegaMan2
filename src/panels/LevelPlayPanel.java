package panels;

import application.GameState;
import application.GameTransitionEvent;
import application.GameTransitionEventKey;
import graphics.objects.Megaman;
import graphics.objects.bosses.BossType;
import serialization.SerializedTilemap;
import util.IOUtils;

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

    hero = new Megaman();
  }

  @Override
  public void prepareForUpdates(GameTransitionEvent transitionEvent) {
    bossType = (BossType) transitionEvent.getValue(GameTransitionEventKey.LEVEL_SELECT_BOSS_TYPE);
    
    try {
        SerializedTilemap serialized = IOUtils.loadLevel(bossType);
        System.out.println("its: " + serialized);
    } catch (IOException ioe) {
        ioe.printStackTrace();
    }
  }

  @Override
  public void updateGame(long elapsedTime) {
    hero.update(elapsedTime);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    hero.paint(g2);
  }
}
