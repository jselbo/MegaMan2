package graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Sprite {
  protected Animation[] anims;
  protected int currIndex;

    protected Direction direction = Direction.RIGHT; // must be LEFT or RIGHT
    protected float x, y;
    protected float vx, vy; // pixels per millisecond

    protected int width, height; // set only by first image

    public Sprite() {
      this(null);
    }

    public Sprite(Animation[] anims) {
      this.anims = anims;
      currIndex = 0;

      x = 0;
      y = 0;
      vx = 0;
      vy = 0;
    }

    public void setState(int index) {
      if (index != currIndex && index >= 0 && index < anims.length) {
        x += (anims[currIndex].getImage().getWidth(null) - anims[index].getImage().getWidth(null))/2;
            y += anims[currIndex].getImage().getHeight(null) - anims[index].getImage().getHeight(null);

        currIndex = index;

        anims[currIndex].start();
      }
    }

    public int getState() {
      return currIndex;
    }

    public Animation getAnimation() {
      return anims[currIndex];
    }

    // Allow subclasses access to all animations
    protected Animation[] getAnimations() {
      return anims;
    }

    public void update(long elapsedTime) {
        x += vx * elapsedTime;
        y += vy * elapsedTime;

        int initialFrame = anims[currIndex].getCurrentIndex();
        anims[currIndex].update(elapsedTime);
        int finalFrame = anims[currIndex].getCurrentIndex();

        if (initialFrame != finalFrame && anims[currIndex].getImage() != null && anims[currIndex].getImage(initialFrame) != null) {
            width = anims[currIndex].getImage().getWidth(null);
            height = anims[currIndex].getImage().getHeight(null);

            x += (anims[currIndex].getImage(initialFrame).getWidth(null) - width)/2;
            y += anims[currIndex].getImage(initialFrame).getHeight(null) - height;
        }
    }

    public void paint(Graphics2D g2) {
      Image image = anims[currIndex].getImage();
      if (image != null) {
        if (direction == Direction.RIGHT)
          g2.drawImage(image, (int)x, (int)y, null);
            else
                g2.drawImage(image, (int)x + image.getWidth(null), (int)y, (int)x, (int)y + image.getHeight(null),
                    0, 0, image.getWidth(null), image.getHeight(null), null);
      }
    }

    public void setPosition(float x, float y) {
      this.x = x;
      this.y = y;
    }

    public void setPosition(Point2D location) {
      if (location != null) {
        this.x = (float)location.getX();
        this.y = (float)location.getY();
      }
    }

    public Point2D getPosition() {
      return new Point2D.Float(x, y);
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
      return anims[currIndex].getImage().getWidth(null);
    }

    public int getHeight() {
      return anims[currIndex].getImage().getHeight(null);
    }

    public Rectangle2D getBounds() {
      return new Rectangle2D.Float(x, y, width, height);
    }

    public void setVelocityX(float vx) {
        this.vx = vx;
    }

    public float getVelocityX() {
        return vx;
    }

    public void setVelocityY(float vy) {
        this.vy = vy;
    }

    public float getVelocityY() {
        return vy;
    }

    public void setDirection(Direction direction) {
      if (direction == Direction.RIGHT || direction == Direction.LEFT)
        this.direction = direction;
    }

    public Direction getDirection() {
      return direction;
    }

    public Image getImage() {
        return anims[currIndex].getImage();
    }
}