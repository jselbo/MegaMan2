package graphics.objects.bosses;

import graphics.Animation;

import java.awt.Image;

import application.Utility;

public class Bubbleman extends Boss {
  public Bubbleman() {
    super(loadAnimations());
  }

  private static Animation[] loadAnimations() {
    Animation[] anims = new Animation[NUM_STATES];

    String base = "res/images/bosses/bubbleman/";

    Image idle = Utility.loadImage(base + "idle.png");

    anims[STATE_IDLE] = new Animation();
    anims[STATE_IDLE].addFrame(idle, 0);

    anims[STATE_FALL] = new Animation();
    anims[STATE_FALL].addFrame(idle, 0);

    anims[STATE_TAUNT] = new Animation(false);
    anims[STATE_TAUNT].addFrame(idle, 1500);
    anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "fist1.png"), 200);
    anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "fist2.png"), 1000);

    return anims;
  }

  @Override
  public String getName() {
    return "Bubbleman";
  }
}