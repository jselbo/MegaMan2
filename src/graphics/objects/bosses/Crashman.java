package graphics.objects.bosses;

import graphics.Animation;
import util.IOUtils;

import java.awt.Image;

public class Crashman extends Boss {
  public Crashman() {
    super(loadAnimations());
  }

  private static Animation[] loadAnimations() {
    Animation[] anims = new Animation[NUM_STATES];

    String base = "res/images/bosses/crashman/";

    Image idle = IOUtils.loadImage(base + "idle.png");

    anims[STATE_IDLE] = new Animation();
    anims[STATE_IDLE].addFrame(idle, 0);

    anims[STATE_FALL] = new Animation();
    anims[STATE_FALL].addFrame(IOUtils.loadImage(base + "fall.png"), 0);

    anims[STATE_TAUNT] = new Animation(false);
    anims[STATE_TAUNT].addFrame(idle, 1000);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "pose1.png"), 300);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "pose2.png"), 100);

    return anims;
  }

  @Override
  public BossType getType() {
    return BossType.CRASHMAN;
  }
}