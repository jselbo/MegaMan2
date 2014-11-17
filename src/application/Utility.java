package application;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Font;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

/**
 * A general utility class with several useful methods and important global constants.
 */
public class Utility
{
  public static final int WINDOW_BORDER = 10; // Total window border thickness on Windows 7 (thin border variant)
  public static final int SPRITE_SIZE = 16; // width and height of each game block, or sprite
  public static final int BLOCK_SIZE = 32;

  public static Image loadImage(String filePath)
  {
    return (new ImageIcon(filePath)).getImage();
  }

  public static Sequence loadMidi(String filePath)
  {
    File midiFile = new File(filePath);
    if (!midiFile.exists())
      return null;

    Sequence midi = null;
    try {
      midi = MidiSystem.getSequence(midiFile);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return midi;
  }

  public static Clip loadClip(String filePath)
  {
    Clip clip = null;

    try
    {
      File clipFile = new File(filePath);
      if (!clipFile.exists())
        return null;

      AudioInputStream audioStream = AudioSystem.getAudioInputStream(clipFile);

      clip = AudioSystem.getClip();
      clip.open(audioStream);
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }

    return clip;
  }

  public static int rightJustify(String string, Font font, int x)
  {
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
    Rectangle2D bounds = font.getStringBounds(string, frc);
    return (int)(x - bounds.getWidth());
  }

  public static int xCenterText(String string, Font font, Dimension dim)
    {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
        Rectangle2D bounds = font.getStringBounds(string, frc);
        return (int)(((dim.width + WINDOW_BORDER) / 2) - (bounds.getWidth() / 2));
    }

  public static int yCenterText(String string, Font font, Dimension dim)
    {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
        Rectangle2D bounds = font.getStringBounds(string, frc);
        return (int)(((dim.height + WINDOW_BORDER) / 2) - (bounds.getHeight() / 2));
    }

  public static Point centerText(String string, Font font, Dimension dim)
  {
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
    Rectangle2D bounds = font.getStringBounds(string, frc);
        int x = (int)(((dim.width + WINDOW_BORDER) / 2) - (bounds.getWidth() / 2));
        int y = (int)(((dim.height + WINDOW_BORDER) / 2) - (bounds.getHeight() / 2));
        return new Point(x, y);
  }

  public static int xCenterImage(Image image, Dimension dim)
  {
    return ((dim.width + WINDOW_BORDER) / 2) - (image.getWidth(null) / 2);
  }

  public static int yCenterImage(Image image, Dimension dim)
  {
    return ((dim.height + WINDOW_BORDER) / 2) - (image.getHeight(null) / 2);
  }

  public static Point centerImage(Image image, Dimension dim)
  {
    return new Point(xCenterImage(image, dim), yCenterImage(image, dim));
  }
}