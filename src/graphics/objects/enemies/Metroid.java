package graphics.objects.enemies;

import graphics.Animation;
import application.GameDifficulty;
import application.GameState;
import application.Utility;

public class Metroid extends Enemy {
  public static final int STATE_FLYING = 1;

  private long time;
  private float w, a, xAxis;

  public Metroid(float xAxis, float period, float amplitude, GameDifficulty difficulty, float vx) {
    super(loadAnimations());

    setState(STATE_FLYING);

    if (difficulty == GameDifficulty.DIFFICULT)
      this.vx = vx;
    else
      this.vx = vx/2;

    this.x = -this.getWidth();
    y = xAxis;
    this.xAxis = xAxis;
    w = 2*3.14f / period / 1000f;
    a = amplitude;
  }

  private static Animation[] loadAnimations() {
    Animation[] anims = new Animation[2];

    String base = "res/images/enemies/";

    anims[STATE_EXPLODING] = new Animation(false);
    anims[STATE_EXPLODING].addFrame(Utility.loadImage(base + "explode1.png"), 200);
    anims[STATE_EXPLODING].addFrame(Utility.loadImage(base + "explode2.png"), 200);

    anims[STATE_FLYING] = new Animation(true);
    anims[STATE_FLYING].addFrame(Utility.loadImage(base + "metroid1.png"), 200);
    anims[STATE_FLYING].addFrame(Utility.loadImage(base + "metroid2.png"), 200);

    return anims;
  }

  @Override
  public void update(long elapsedTime) {
    super.update(elapsedTime);

    time += elapsedTime;
    y = xAxis + (float)(a*Math.sin(w * time));
  }

  @Override
  public String toString() {
    return "Metroid";
  }
}