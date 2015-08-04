package graphics.objects.bosses;

import graphics.Animation;
import util.IOUtils;

import java.awt.Image;

public class Airman extends Boss {
  public Airman() {
    super(loadAnimations());
  }

  private static Animation[] loadAnimations() {
    Animation[] anims = new Animation[NUM_STATES];

    String base = "res/images/bosses/airman/";

    Image idle = IOUtils.loadImage(base + "idle.png");
    Image blow1 = IOUtils.loadImage(base + "blow1.png");
    Image blow2 = IOUtils.loadImage(base + "blow2.png");

    anims[STATE_IDLE] = new Animation();
    anims[STATE_IDLE].addFrame(idle, 0);

    anims[STATE_FALL] = new Animation();
    anims[STATE_FALL].addFrame(IOUtils.loadImage(base + "fall.png"), 0);

    anims[STATE_TAUNT] = new Animation(false);
    anims[STATE_TAUNT].addFrame(idle, 1000);
    anims[STATE_TAUNT].addFrame(blow1, 50);
    anims[STATE_TAUNT].addFrame(blow2, 50);
    anims[STATE_TAUNT].addFrame(blow1, 50);
    anims[STATE_TAUNT].addFrame(blow2, 50);
    anims[STATE_TAUNT].addFrame(idle, 300);
    anims[STATE_TAUNT].addFrame(blow1, 50);
    anims[STATE_TAUNT].addFrame(blow2, 50);
    anims[STATE_TAUNT].addFrame(blow1, 50);
    anims[STATE_TAUNT].addFrame(blow2, 50);
    anims[STATE_TAUNT].addFrame(idle, 1000);

    return anims;
  }

  @Override
  public BossType getType() {
    return BossType.AIRMAN;
  }
}