package graphics.objects.bosses;

import graphics.Animation;
import util.IOUtils;

import java.awt.Image;

public class Flashman extends Boss {
  public Flashman() {
    super(loadAnimations());
  }

  private static Animation[] loadAnimations() {
    Animation[] anims = new Animation[NUM_STATES];

    String base = "res/images/bosses/flashman/";

    Image idle = IOUtils.loadImage(base + "idle.png");
    Image fall = IOUtils.loadImage(base + "fall.png");

    anims[STATE_IDLE] = new Animation();
    anims[STATE_IDLE].addFrame(idle, 0);

    anims[STATE_FALL] = new Animation();
    anims[STATE_FALL].addFrame(fall, 0);

    anims[STATE_TAUNT] = new Animation(false);
    anims[STATE_TAUNT].addFrame(idle, 1000);
    anims[STATE_TAUNT].addFrame(fall, 100);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "flash1.png"), 100);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "flash2.png"), 100);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "flash3.png"), 100);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "flash4.png"), 100);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "flash1.png"), 100);

    return anims;
  }

  @Override
  public BossType getType() {
    return BossType.FLASHMAN;
  }
}