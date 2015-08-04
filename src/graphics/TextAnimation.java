package graphics;

import java.awt.Color;
import java.util.ArrayList;

public class TextAnimation {
    private ArrayList<AnimFrame> frames;
    private int currFrameIndex;
    private long animTime;
    private long totalDuration;
    private boolean repeat;

    private String text;

    public TextAnimation(String text) {
        this(text, true);
    }

    public TextAnimation(String text, boolean repeat) {
      this(new ArrayList<AnimFrame>(), text, 0, repeat);
    }

    private TextAnimation(ArrayList<AnimFrame> frames, String text, long totalDuration, boolean repeat) {
        this.frames = frames;
        this.text = text;
        this.totalDuration = totalDuration;
        this.repeat = repeat;
        start();
    }

    @Override
    public Object clone() {
        return new TextAnimation(frames, text, totalDuration, repeat);
    }

    public synchronized void addFrame(long duration, Color color) {
        totalDuration += duration;
        frames.add(new AnimFrame(totalDuration + duration, color));
    }

    public void setText(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }

    public synchronized void start() {
        animTime = 0;
        currFrameIndex = 0;
    }

    public synchronized void update(long elapsedTime) {
        if (frames.size() > 1) {
          if (!repeat && currFrameIndex >= frames.size() - 1)
            return;

            animTime += elapsedTime;

            if (animTime >= totalDuration) {
                animTime = animTime % totalDuration;
                currFrameIndex = 0;
            }

            while (animTime > getFrame(currFrameIndex).endTime)
                currFrameIndex++;
        }
    }

    public synchronized AnimFrame getFrame() {
        if (frames.size() == 0)
            return null;
        else
            return getFrame(currFrameIndex);
    }

    public synchronized AnimFrame getFrame(int frameIndex) {
      if (frameIndex < 0 || frameIndex > frames.size() - 1)
        return null;
      else
        return frames.get(frameIndex);
    }

    public int getNumberFrames() {
      return frames.size();
    }

    public int getCurrentIndex() {
      return currFrameIndex;
    }

    public boolean repeats() {
      return repeat;
    }

    public class AnimFrame {
        long endTime;
        Color color;

        public AnimFrame(long endTime, Color color) {
            this.endTime = endTime;
            this.color = color;
        }
    }
}
