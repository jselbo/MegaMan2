package graphics.objects;

import application.GameManager;
import graphics.Sprite;

import java.awt.Graphics2D;
import java.awt.Image;

public class Block extends Sprite {
  private Image img;

  public Block(int x, int y, Image img) {
    this.x = x;
    this.y = y;
    this.width = GameManager.BLOCK_SIZE;
    this.height = GameManager.BLOCK_SIZE;

    this.img = img;
  }

  @Override
  public void paint(Graphics2D g2) {
    g2.drawImage(img, (int)x, (int)y, null);
  }
}