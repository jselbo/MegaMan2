package panels;

import application.GameState;
import serialization.SerializedLevel;
import util.IOUtil;

import com.google.gson.*;

import graphics.objects.Megaman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class LevelPanel extends GamePanel {
  private static final long serialVersionUID = 1L;

  private Megaman hero;

  public LevelPanel(GameState state) {
    super(state);

    setBackground(Color.BLACK);
    hero = new Megaman();

    String levelStr;
    try {
      levelStr = IOUtil.readFile("res/levels/woodman/overworld.json", StandardCharsets.UTF_8);
      Gson gson = new Gson();
      SerializedLevel level = gson.fromJson(levelStr, SerializedLevel.class);
      System.out.println("Level: " + level);
    } catch (IOException ie) {
      ie.printStackTrace();
    }
  }

  @Override
  public void start() {
    super.start();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    hero.paint(g2);
  }

  @Override
  public void updateGame(long elapsedTime) {
  }
}
