package graphics.objects;

import graphics.Animation;
import graphics.Sprite;
import application.Utility;

public class Health extends Sprite {
  public Health(float x, float y) {
    super(loadAnimations());

    this.x = x;
    this.y = y;
    vy = -0.5f;
  }

  private static Animation[] loadAnimations() {
    Animation[] anims = new Animation[1];

    anims[0] = new Animation(true);
    anims[0].addFrame(Utility.loadImage("res/images/tilesets/temp/health1.png"), 200);
    anims[0].addFrame(Utility.loadImage("res/images/tilesets/temp/health2.png"), 200);

    return anims;
  }
}