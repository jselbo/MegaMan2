package graphics.objects.bosses;

import graphics.Animation;

import java.awt.Image;

import application.Utility;

public class Quickman extends Boss {
  public Quickman() {
    super(loadAnimations());
  }

  private static Animation[] loadAnimations() {
    Animation[] anims = new Animation[NUM_STATES];

    String base = "res/images/bosses/quickman/";

    Image idle = Utility.loadImage(base + "idle.png");

    anims[STATE_IDLE] = new Animation();
    anims[STATE_IDLE].addFrame(idle, 0);

    anims[STATE_FALL] = new Animation();
    anims[STATE_FALL].addFrame(Utility.loadImage(base + "fall.png"), 0);

    anims[STATE_TAUNT] = new Animation(false);
    anims[STATE_TAUNT].addFrame(idle, 1000);
    anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "taunt1.png"), 83);
    anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "taunt2.png"), 83);
    anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "taunt3.png"), 83);
    anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "taunt4.png"), 83);
    anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "taunt5.png"), 83);
    anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "taunt6.png"), 83);
    anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "taunt7.png"), 83);
    anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "taunt2.png"), 83);

    return anims;
  }

  @Override
  public BossType getType() {
    return BossType.QUICKMAN;
  }
}