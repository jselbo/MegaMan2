package graphics.objects.bosses;

import graphics.Animation;
import util.IOUtils;

import java.awt.Image;

public class Woodman extends Boss {
  public Woodman() {
    super(loadAnimations());
  }

  private static Animation[] loadAnimations() {
    Animation[] anims = new Animation[NUM_STATES];

    String base = "res/images/bosses/woodman/";

    Image idle = IOUtils.loadImage(base + "idle.png");

    anims[STATE_IDLE] = new Animation();
    anims[STATE_IDLE].addFrame(idle, 0);

    anims[STATE_FALL] = new Animation();
    anims[STATE_FALL].addFrame(idle, 0);

    anims[STATE_TAUNT] = new Animation(false);
    anims[STATE_TAUNT].addFrame(idle, 1000);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt1.png"), 133);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt2.png"), 133);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt1.png"), 133);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt2.png"), 133);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt1.png"), 133);
    anims[STATE_TAUNT].addFrame(IOUtils.loadImage(base + "taunt2.png"), 133);
    anims[STATE_TAUNT].addFrame(idle, 100);

    return anims;
  }

  @Override
  public BossType getType() {
    return BossType.WOODMAN;
  }
}