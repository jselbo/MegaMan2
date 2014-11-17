package graphics.objects;

import graphics.Sprite;
import graphics.Animation;

import java.awt.Graphics2D;
import java.awt.Image;

import application.Utility;

public class Selector extends Sprite {
  private int position;
  private int max; // maximum range of selection
  private int ySpacing; // distance between selections
  private boolean enabled;

  public Selector(int max) {
    this(max, 0);
  }

  public Selector(int max, int ySpacing) {
    super(loadAnimations());

    position = 0;
    this.ySpacing = ySpacing;

    enabled = true;

    setMax(max);
  }

  private static Animation[] loadAnimations() {
    Animation[] anims = new Animation[1];

    Animation anim = new Animation();
    Image image = Utility.loadImage("res/images/general/selector.png");
    anim.addFrame(image, 133);
    anim.addFrame(null, 133);

    anims[0] = anim;

    return anims;
  }

  @Override
  public void paint(Graphics2D g2) {
    if (enabled)
      super.paint(g2);
  }

  public void setMax(int max) {
    if (max < 0)
      throw new IllegalArgumentException("Maximum selection cannot be negative.");
    this.max = max;
  }

  public int getMax() {
    return max;
  }

  public void increment() {
    position++;
    setY(getY() + ySpacing);

    if (position > max) {
      setY(getY() - ySpacing * position);
      position = 0;
    }
  }

  public void decrement() {
    position--;
    setY(getY() - ySpacing);

    if (position < 0) {
      setY(getY() + ySpacing * (max+1));
      position = max;
    }
  }

  public void reset() {
    position = 0;
    setY(getY() - ySpacing * position);
  }

  public int getIndex() {
    return position;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }
}