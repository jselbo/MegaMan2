package graphics.objects.enemies;

import application.GameDifficulty;
import graphics.Animation;
import graphics.Direction;
import util.IOUtils;

public class Bird extends Enemy {
  private static final int STATE_FLYING = 1;

  private static final float VELOCITIES[] = { 0.5f, 0.4f, 0.3f };

  private int type;

  public Bird(int y, Direction direction, int type, GameDifficulty difficulty) {
    super(loadAnimations(type));

    this.y = y;
    this.direction = direction;
    this.type = type;

    if (direction == Direction.RIGHT) {
      x = -width;
      if (difficulty == GameDifficulty.NORMAL)
        vx = VELOCITIES[type]/2;
      else
        vx = VELOCITIES[type];
    } else {
      x = 16*32;
      if (difficulty == GameDifficulty.NORMAL)
        vx = -VELOCITIES[type]/2;
      else
        vx = -VELOCITIES[type];
    }

    setState(STATE_FLYING);
  }

  private static Animation[] loadAnimations(int type) {
    Animation[] anims = new Animation[2];

    String base = "res/images/enemies/";

    char c = ' ';
    if (type == 0)
      c = 'a';
    else if (type == 1)
      c = 'b';
    else if (type == 2)
      c = 'c';

    anims[STATE_EXPLODING] = new Animation(false);
    anims[STATE_EXPLODING].addFrame(IOUtils.loadImage(base + "explode1.png"), 200);
    anims[STATE_EXPLODING].addFrame(IOUtils.loadImage(base + "explode2.png"), 200);

    anims[STATE_FLYING] = new Animation(true);
    anims[STATE_FLYING].addFrame(IOUtils.loadImage(base + "bird" + c + "1.png"), 400);
    anims[STATE_FLYING].addFrame(IOUtils.loadImage(base + "bird" + c + "2.png"), 400);

    return anims;
  }

  public int getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Bird";
  }
}