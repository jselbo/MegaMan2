package application;

import application.dialog.AboutDialog;
import application.dialog.AudioDialog;
import audio.MidiPlayer;
import audio.SoundManager;
import input.InputListener;
import panels.GamePanel;
import panels.IntroPanel;
import panels.LevelPlayPanel;
import panels.LevelSelectPanel;
import panels.PasswordPanel;

import javax.sound.sampled.AudioFormat;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GameManager implements Runnable {
  // -- Global constants --

  // Total window border thickness on Windows 7 (thin border variant)
  public static final int WINDOW_BORDER = 10;
  // width and height of each game block, or sprite
  public static final int SPRITE_SIZE = 16;
  public static final int BLOCK_SIZE = 32;

  // (1 second) divided by (frames per second)
  private static final int SLEEP_TIME = (int)(1000f / 60f);
  private static final int START_PANEL = 0;

  // uncompressed, 44100Hz, 16-bit, stereo, signed, little-endian
  private static final AudioFormat PLAYBACK_FORMAT = new AudioFormat(44100, 16, 2, true, false);

  private static final GamePanel.Type STARTING_PANEL_TYPE = GamePanel.Type.LEVEL_PLAY_PANEL;

  // GUI elements
  private JFrame frame;
  private JMenuBar menuBar;
  private JMenuItem audioMenuItem, resetFlagMenuItem, aboutMenuItem;

  private JDialog audioDialog, aboutDialog;

  // Game state
  private Properties gameProperties;
  private GamePanel currentPanel;
  private GameState gameState;

  private boolean resetFlag;
  private boolean running;

  // Input elements
  private InputListener inputListener;

  // Audio elements
  private SoundManager soundManager;
  private MidiPlayer midiPlayer;

  public GameManager(JFrame frame) {
    this.frame = frame;

    menuBar = new JMenuBar();
    initializeMenuBar();

    audioDialog = null;

    running = false;

    inputListener = new InputListener();

    soundManager = new SoundManager(PLAYBACK_FORMAT);
    midiPlayer = new MidiPlayer();

    Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
    gameState = new GameState(frame, screenDim, new Dimension(256 * 2, 240 * 2),
        inputListener, soundManager, midiPlayer);

    loadProperties();

    removeCurrentPanelAndLoad(GameTransitionEvent.emptyTransitionEvent(STARTING_PANEL_TYPE));
  }

  private void loadProperties() {
    gameProperties = new Properties();

    FileInputStream in = null;
    try {
      in = new FileInputStream("res/config.properties");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    try {
      gameProperties.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }

    int volume = Integer.parseInt(gameProperties.getProperty("volumeLevel"));
    midiPlayer.setVolume(volume);

    boolean muted = Boolean.parseBoolean(gameProperties.getProperty("volumeMuted"));
    midiPlayer.setMuted(muted);
  }

  private void initializeMenuBar() {
    ActionListener listener = new MenuItemListener();

    JMenu gameMenu = new JMenu("Game");
    gameMenu.setMnemonic('o');

    audioMenuItem = new JMenuItem("Audio...");
    audioMenuItem.addActionListener(listener);
    audioMenuItem.setMnemonic('a');
    resetFlagMenuItem = new JMenuItem("Reset");
    resetFlagMenuItem.addActionListener(listener);
    resetFlagMenuItem.setMnemonic('r');
    aboutMenuItem = new JMenuItem("About...");
    aboutMenuItem.addActionListener(listener);
    aboutMenuItem.setMnemonic('b');

    //optionsMenu.add(audioMenuItem);
    gameMenu.add(resetFlagMenuItem);
    gameMenu.add(aboutMenuItem);

    menuBar.add(gameMenu);
  }

  public JMenuBar getJMenuBar() {
    return menuBar;
  }

  public void start() {
    running = true;
    new Thread(this).start();
  }

  /**
   * Game loop.
   */
  public void run() {
    long previousTime = System.currentTimeMillis();

    while (running) {
      long currentTime = System.currentTimeMillis();
      long elapsedTime = currentTime - previousTime;
      previousTime = currentTime;

      updateGame(elapsedTime);

      try {
        Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException ie) {}
    }
  }

  private void updateGame(long elapsedTime) {
    if (resetFlag) {
      resetFlag = false;
      removeCurrentPanelAndLoad(GameTransitionEvent.emptyTransitionEvent(STARTING_PANEL_TYPE));
      return;
    }

    if (currentPanel.getTransitionFlag()) {
      removeCurrentPanelAndLoad(currentPanel.getTransitionEvent());
    } else {
      currentPanel.updateGame(elapsedTime);
      currentPanel.paintImmediately(0, 0, currentPanel.getWidth(), currentPanel.getHeight());
    }
  }

  private void removeCurrentPanelAndLoad(GameTransitionEvent event) {
    // Remove the current panel
    if (currentPanel != null) {
      currentPanel.removeKeyListener(inputListener);
      currentPanel.removeMouseListener(inputListener);
      currentPanel.removeMouseMotionListener(inputListener);
      currentPanel.removeMouseWheelListener(inputListener);

      frame.getContentPane().remove(currentPanel);
    }

    inputListener.clearKeyStates();

    GamePanel destinationPanel = createGamePanelFromType(event.getTargetPanelType());
    destinationPanel.addKeyListener(inputListener);
    destinationPanel.addMouseListener(inputListener);
    destinationPanel.addMouseMotionListener(inputListener);
    destinationPanel.addMouseWheelListener(inputListener);

    // Add the new panel
    frame.getContentPane().add(destinationPanel);
    frame.validate();

    destinationPanel.requestFocus();
    destinationPanel.prepareForUpdates(event);

    currentPanel = destinationPanel;
  }

  private GamePanel createGamePanelFromType(GamePanel.Type type) {
    switch (type) {
      case INTRO_PANEL:
        return new IntroPanel(gameState);
      case PASSWORD_PANEL:
        return new PasswordPanel(gameState);
      case LEVEL_SELECT_PANEL:
        return new LevelSelectPanel(gameState);
      case LEVEL_PLAY_PANEL:
        return new LevelPlayPanel(gameState);
    }
    throw new IllegalStateException("Invalid panel type");
  }

  private class MenuItemListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object source = e.getSource();
      if (source.equals(audioMenuItem)) {
        if (audioDialog == null)
          audioDialog = new AudioDialog(frame, "Audio Options", true);

        audioDialog.setLocationRelativeTo(frame);
        audioDialog.setVisible(true);
      } else if (source.equals(resetFlagMenuItem)) {
        resetFlag = true;
      } else if (source.equals(aboutMenuItem)) {
        if (aboutDialog == null)
          aboutDialog = new AboutDialog(frame);

        aboutDialog.setLocationRelativeTo(frame);
        aboutDialog.setVisible(true);
      }
    }
  }
}