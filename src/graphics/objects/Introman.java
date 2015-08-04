package graphics.objects;

import graphics.Animation;
import graphics.Sprite;
import util.IOUtils;

import java.awt.Image;

public class Introman extends Sprite {
  public final static int STATE_IDLE = 0;
  public final static int STATE_TELEPORTING = 1;

  private final static float VY = -0.6f; // pixels per millisecond

  private boolean launched; // set to true when his body coils (ergo, cue sound)
  private boolean teleported; // set to true when position is above screen

  public Introman() {
    super(loadAnimations());

    reset();
  }

  private static Animation[] loadAnimations() {
    Animation[] anim = new Animation[2];

    String base = "res/images/megaman/";

    anim[STATE_IDLE] = new Animation();
    final int IDLE_IMGS = 2;
    for (int i = 0; i < IDLE_IMGS; i++) {
      Image img = IOUtils.loadImage(base + "hair" + (i + 1) + ".png");
      anim[STATE_IDLE].addFrame(img, 83);
    }

    anim[STATE_TELEPORTING] = new Animation(false);
    Image[] teleportingImages = new Image[12];
    for (int i = 0; i < teleportingImages.length; i++)
      teleportingImages[i] = IOUtils.loadImage(base + "teleport" + (i+1) + ".png");
    anim[STATE_TELEPORTING].addFrame(teleportingImages[0], 67);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[1], 67);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[2], 67);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[3], 67);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[4], 200);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[5], 67);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[6], 67);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[7], 67);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[8], 67);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[9], 33);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[10], 33);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[11], 33);
    anim[STATE_TELEPORTING].addFrame(teleportingImages[9], 33);

    return anim;
  }

  public void reset() {
    setState(STATE_IDLE);
    setVelocityY(0);

    for (Animation anim : this.getAnimations())
      anim.start();

    launched = false;
    teleported = false;
  }

  @Override
  public void update(long elapsedTime) {
    super.update(elapsedTime);

    if (currIndex == STATE_TELEPORTING) {
      if (anims[currIndex].isDone()) {
        setVelocityY(VY);

        if (y + height * 2 < 0)
          teleported = true;
      }

      if (anims[currIndex].getCurrentIndex() >= 9)
        launched = true;
    }
  }

  public boolean teleported() {
    return teleported;
  }

  public boolean launched() {
    return launched;
  }
}