package graphics.objects.bosses;

import graphics.Animation;
import graphics.Sprite;

public abstract class Boss extends Sprite {
  public final static int NUM_STATES = 3;
  public final static int STATE_IDLE = 0;
  public final static int STATE_FALL = 1;
  public final static int STATE_TAUNT = 2; // taunt animation played during level intro

  public Boss(Animation[] anims) {
    super(anims);

    if (anims.length != NUM_STATES)
      throw new IllegalArgumentException("Fix the boss animations, bro");
  }

  public abstract String getName();
}