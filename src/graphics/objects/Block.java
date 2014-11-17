package graphics.objects;

import graphics.Sprite;

import java.awt.Graphics2D;
import java.awt.Image;

import application.Utility;

public class Block extends Sprite
{
  private Image img;

  public Block(int x, int y, Image img)
  {
    this.x = x;
    this.y = y;
    this.width = Utility.BLOCK_SIZE;
    this.height = Utility.BLOCK_SIZE;

    this.img = img;
  }

  @Override
  public void paint(Graphics2D g2)
  {
    g2.drawImage(img, (int)x, (int)y, null);
  }
}