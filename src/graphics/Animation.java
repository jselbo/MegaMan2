package graphics;

import java.awt.Image;
import java.util.ArrayList;

public class Animation {
  private ArrayList<AnimFrame> frames;
  private int frameIndex;

  private long animTime; // cumulative, values between 0 and totalDuration
  private long totalDuration;

  private final boolean repeat;
  private boolean done;

  public Animation() {
    this(true);
  }

  public Animation(boolean repeat) {
    this(new ArrayList<AnimFrame>(), 0, repeat);
  }

  private Animation(ArrayList<AnimFrame> frames, long totalDuration, boolean repeat) {
    this.frames = frames;
    this.totalDuration = totalDuration;
    this.repeat = repeat;
    start();
  }

  public synchronized void addFrame(Image image, long duration) {
    totalDuration += duration;
    frames.add(new AnimFrame(image, totalDuration));
  }

  public synchronized void start() {
    animTime = 0;
    frameIndex = 0;
    done = false;
  }

  public synchronized void update(long elapsedTime) {
    if (done)
    return;

    animTime += elapsedTime;

    if (frames.size() > 1) {
      if (animTime >= totalDuration) {
        if (repeat) {
          animTime = animTime % totalDuration;
          frameIndex = 0;
        } else {
        done = true;
        animTime = totalDuration;
        }
      }

      while (animTime > frames.get(frameIndex).endTime)
        frameIndex++;
    } else {
      if (animTime > totalDuration)
      done = true;
    }
  }

  public boolean isDone() {
    return done;
  }

  public synchronized Image getImage() {
    return getImage(frameIndex);
  }

  public synchronized Image getImage(int index) {
    if (index < 0 || index > frames.size() - 1)
      return null;

    return frames.get(index).image;
  }

  public int getNumberFrames() {
    return frames.size();
  }

  public int getCurrentIndex() {
    return frameIndex;
  }

  public final boolean repeats() {
    return repeat;
  }

  @Override
  public Object clone() {
    return new Animation(frames, totalDuration, repeat);
  }

  @Override
  public String toString() {
    return "Animation [frames: " + frames.size() + ", duration: " + totalDuration + "]";
  }

  private class AnimFrame {
    final Image image;
    final long endTime;

    public AnimFrame(Image image, long endTime) {
      this.image = image;
      this.endTime = endTime;
    }
  }
}
