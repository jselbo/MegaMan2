package graphics.objects.bosses;

import graphics.Animation;
import util.IOUtils;

import java.awt.Image;

public class Quickman extends Boss {
  public Quickman() {
    super(loadAnimations());
  }

  private static Animation[] loadAnimations() {
    Animation[] anims = new Animation[NUM_STATES];

    String base = "res/images/bosses/quickman/";

    Image idle = IOUtils.loadImage(base + "idle.png");

    anims[STATE_IDLE] = new Animation();
    anims[STATE_IDLE].addFrame(idle, 0);

    anims[STATE_FALL] = new Animation();
    anims[STATE_FALL].addFrame(IOUtils.loadImage(base + "fall.png"), 0);

    anims[STATE_TAUNT] = new Animation(false);
    anims[STATE_TAUNT].addFrame(idle, 1000);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt1.png"), 83);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt2.png"), 83);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt3.png"), 83);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt4.png"), 83);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt5.png"), 83);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt6.png"), 83);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt7.png"), 83);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt2.png"), 83);

    return anims;
  }

  @Override
  public BossType getType() {
    return BossType.QUICKMAN;
  }
}