package panels;

import application.GameState;
import application.Utility;

import input.InputListener;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;

import java.io.File;
import java.io.IOException;

import audio.SoundManager;
import audio.MidiPlayer;

import javax.swing.JPanel;

public abstract class GamePanel extends JPanel {
  private static final long serialVersionUID = 1L;

  protected GameState state;

  protected Dimension screenDim, dim;
  protected Font gameFont;

  protected InputListener inputListener;

  protected SoundManager soundManager;
  protected MidiPlayer midiPlayer;

  private boolean done;

  public GamePanel(GameState state) {
    done = false;

    this.state = state;

    this.screenDim = state.getScreenDimension();
    this.dim = state.getDimension();
    setPreferredSize(new Dimension(dim.width - Utility.WINDOW_BORDER, dim.height - Utility.WINDOW_BORDER));
    setDoubleBuffered(true);

    this.inputListener = state.getInputListener();

    this.soundManager = state.getSoundManager();
    this.midiPlayer = state.getMidiPlayer();

    try {
      Font font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/game_font.ttf"));
      gameFont = font.deriveFont(16f);
    }
    catch (IOException ioex) {
      ioex.printStackTrace();
    }
    catch (FontFormatException ffex) {
      ffex.printStackTrace();
    }

    setFocusable(true);
  }

  protected void setDone(boolean done) {
    this.done = done;
  }

  public boolean isDone() {
    return done;
  }

  public void start() {
    done = false;
  }

  /**
   * All inputs should be processed by this method using the <code>inputListener</code>.
   * @param elapsedTime
   */
  public abstract void updateGame(long elapsedTime);
}