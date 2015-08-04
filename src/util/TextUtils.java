package util;

import application.GameManager;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Utility methods for rendering text.
 */
public class TextUtils {
  public static int rightJustify(String string, Font font, int x) {
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
    Rectangle2D bounds = font.getStringBounds(string, frc);
    return (int)(x - bounds.getWidth());
  }

  public static int xCenterText(String string, Font font, Dimension dim) {
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
    Rectangle2D bounds = font.getStringBounds(string, frc);
    return (int)(((dim.width + GameManager.WINDOW_BORDER) / 2) - (bounds.getWidth() / 2));
  }

  public static int yCenterText(String string, Font font, Dimension dim) {
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
    Rectangle2D bounds = font.getStringBounds(string, frc);
    return (int)(((dim.height + GameManager.WINDOW_BORDER) / 2) - (bounds.getHeight() / 2));
  }

  public static Point centerText(String string, Font font, Dimension dim) {
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
    Rectangle2D bounds = font.getStringBounds(string, frc);
        int x = (int)(((dim.width + GameManager.WINDOW_BORDER) / 2) - (bounds.getWidth() / 2));
        int y = (int)(((dim.height + GameManager.WINDOW_BORDER) / 2) - (bounds.getHeight() / 2));
        return new Point(x, y);
  }

  public static int xCenterImage(Image image, Dimension dim) {
    return ((dim.width + GameManager.WINDOW_BORDER) / 2) - (image.getWidth(null) / 2);
  }

  public static int yCenterImage(Image image, Dimension dim) {
    return ((dim.height + GameManager.WINDOW_BORDER) / 2) - (image.getHeight(null) / 2);
  }

  public static Point centerImage(Image image, Dimension dim) {
    return new Point(xCenterImage(image, dim), yCenterImage(image, dim));
  }
}