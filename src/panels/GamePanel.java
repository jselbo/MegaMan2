package panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import application.GameState;
import application.GameTransitionEvent;
import application.Utility;

import audio.SoundManager;
import audio.MidiPlayer;

import input.InputListener;

public abstract class GamePanel extends JPanel {
  public enum Type {
    INTRO_PANEL,
    PASSWORD_PANEL,
    LEVEL_SELECT_PANEL,
    LEVEL_PLAY_PANEL;
  }

  private GameState gameState;
  private Font gameFont;

  private GameTransitionEvent transitionEvent;
  // When true, indicates transition should ocurr.
  private boolean transitionFlag;

  public GamePanel(GameState gameState) {
    this.gameState = gameState;

    Dimension gameDimension = gameState.getGameDimension();
    setPreferredSize(new Dimension(gameDimension.width - Utility.WINDOW_BORDER, gameDimension.height - Utility.WINDOW_BORDER));
    setDoubleBuffered(true);

    try {
      Font font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/game_font.ttf"));
      gameFont = font.deriveFont(16f);
    } catch (IOException ioex) {
      ioex.printStackTrace();
    } catch (FontFormatException ffex) {
      ffex.printStackTrace();
    }

    setFocusable(true);
  }

  public GameState getGameState() {
    return gameState;
  }

  public Font getGameFont() {
    return gameFont;
  }

  public boolean getTransitionFlag() {
    return transitionFlag;
  }

  public GameTransitionEvent getTransitionEvent() {
    return transitionEvent;
  }

  /**
   * Called before this panel starts receiving update events.
   * Use it to save event data, start a midi, start timers, etc.
   */
  public abstract void prepareForUpdates(GameTransitionEvent transitionEvent);

  /**
   * All inputs should be processed by this method using the <code>inputListener</code>.
   * @param elapsedTime
   */
  public abstract void updateGame(long elapsedTime);

  protected void declareTransitionEvent(GameTransitionEvent transitionEvent) {
    this.transitionEvent = transitionEvent;
    transitionFlag = true;
  }
}