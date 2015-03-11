package graphics.objects;

import graphics.Animation;
import graphics.Direction;
import graphics.Sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import application.Utility;

public class Megaman extends Sprite {
  private final static int NUM_STATES = 6;
  public final static int STATE_IDLE = 0;
  public final static int STATE_STEP = 1;
  public final static int STATE_RUN = 2;
  public final static int STATE_FALL = 3;
  public final static int STATE_SHOOT = 4;
  public final static int STATE_HIT = 5;

  private Image healthImg;
  private int health;

  public Megaman() {
    super(loadAnimations());

    reset();

    healthImg = Utility.loadImage("res/images/megaman/health.png");
  }

  public static Animation[] loadAnimations() {
    Animation[] anims = new Animation[NUM_STATES];

    String base = "res/images/megaman/";

    anims[STATE_IDLE] = new Animation(true);
    anims[STATE_IDLE].addFrame(Utility.loadImage(base + "idle1.png"), 1500);
    anims[STATE_IDLE].addFrame(Utility.loadImage(base + "idle2.png"), 150);

    anims[STATE_STEP] = new Animation(false);
    anims[STATE_STEP].addFrame(Utility.loadImage(base + "run1.png"), 117);

    anims[STATE_RUN] = new Animation(true);
    anims[STATE_RUN].addFrame(Utility.loadImage(base + "run2.png"), 117);
    anims[STATE_RUN].addFrame(Utility.loadImage(base + "run3.png"), 117);
    anims[STATE_RUN].addFrame(Utility.loadImage(base + "run4.png"), 117);

    anims[STATE_FALL] = new Animation(false);
    anims[STATE_FALL].addFrame(Utility.loadImage(base + "fall.png"), 117);

    anims[STATE_SHOOT] = new Animation(false);
    anims[STATE_SHOOT].addFrame(Utility.loadImage(base + "shoot.png"), 500);

    anims[STATE_HIT] = new Animation(false);
    anims[STATE_HIT].addFrame(Utility.loadImage(base + "hit1.png"), 250);
    anims[STATE_HIT].addFrame(Utility.loadImage(base + "hit2.png"), 250);

    return anims;
  }

  @Override
  public void paint(Graphics2D g2) {
    super.paint(g2);
  }

  public void reset() {
    setState(STATE_IDLE);
    setDirection(Direction.RIGHT);

    setVelocityX(0);
    setVelocityY(0);

    health = 10;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }
}