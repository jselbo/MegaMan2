package panels;

import graphics.Direction;
import graphics.TextAnimation;
import graphics.TextSprite;
import graphics.objects.PasswordGrid;
import graphics.objects.Selector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import javax.sound.midi.Sequence;

import application.GameState;
import application.GameTransitionEvent;
import application.Utility;

import audio.Sound;

import input.InputListener;

public class PasswordPanel extends GamePanel {
  // number of increments for a complete fade
  private static final int FADE_INCREMENTS = 3;
  // time (milliseconds) for each fade increment
  private static final int FADE_TIME = 67;
  private static final float TRANS_SPEED = (float)Utility.SPRITE_SIZE / 50.0f;
  private long runningFadeTime;
  private boolean fading;

  // set to true after game started, read in start()
  private boolean firstRun;

  // defines behavior in paintComponent, update, and key listener methods
  private FrameState frameState;
  // false=left, true=right
  private boolean frame;
  private boolean transitioning;

  private Selector selector;

  private Image selectorBox, passwordBox, orbBox;
  private Image weaponBox, secondaryBox;
  private Point selectorPos, passwordPos, orbPos;
  private Point weaponPos, secondaryPos;

  private PasswordGrid pwGrid;

  private Sequence pwMidi;
  private Sound blip;

  private Image tileBackground;
  private Image orb;
  private TextSprite orbCount;

  private String[] startOptions;

  private int opacity; // 0 to 255, used for fading

  private boolean acceptKeyEvents;
  private int upTime, downTime, leftTime, rightTime;

  public PasswordPanel(GameState gameState) {
    super(gameState);
    setBackground(Color.black);

    firstRun = true;

    selector = new Selector(1, 2 * Utility.SPRITE_SIZE);

    String midiBase = "res/audio/midis/";
    pwMidi = gameState.getMidiPlayer().getSequence(midiBase + "password.mid");

    String soundBase = "res/audio/sound_effects/";
    blip = gameState.getSoundManager().getSound(soundBase + "blip1.wav");

    String imageBase = "res/images/overworld/";

    tileBackground = Utility.loadImage(imageBase + "tile_background.png");

    orb = Utility.loadImage(imageBase + "orb.png");

    selectorBox = Utility.loadImage(imageBase + "selector_box.png");
    selectorPos = new Point();
    passwordBox = Utility.loadImage(imageBase + "password_box.png");
    passwordPos = new Point();
    orbBox = Utility.loadImage(imageBase + "orb_box.png");
    orbPos = new Point();
    weaponBox = Utility.loadImage(imageBase + "weapon_box.png");
    weaponPos = Utility.centerImage(weaponBox, gameState.getGameDimension());
    secondaryBox = Utility.loadImage(imageBase + "secondary_box.png");
    secondaryPos = Utility.centerImage(secondaryBox, gameState.getGameDimension());

    pwGrid = new PasswordGrid(5, 5, 9); // 5x5 grid with a max of 9 orbs

    TextAnimation ta = new TextAnimation(Integer.toString(pwGrid.maxOrbs() - pwGrid.numOrbs()));
    ta.addFrame(100, new Color(255, 255, 255, 255));
    ta.addFrame(67, new Color(255, 255, 255, 170));
    ta.addFrame(67, new Color(255, 255, 255, 85));
    ta.addFrame(67, new Color(255, 255, 255, 0));
    ta.addFrame(67, new Color(255, 255, 255, 85));
    ta.addFrame(67, new Color(255, 255, 255, 170));
    orbCount = new TextSprite(ta);

    startOptions = new String[2];
    startOptions[0] = "START";
    startOptions[1] = "PASSWORD";
  }

  @Override
  public void start() {
    if (firstRun) {
      frameState = FrameState.PASSWORD;

      selector.setPosition(11 * Utility.SPRITE_SIZE, 12 * Utility.SPRITE_SIZE);
      selector.reset();
      selectorPos.setLocation(10 * Utility.SPRITE_SIZE, 10 * Utility.SPRITE_SIZE);
      passwordPos.setLocation((4+32) * Utility.SPRITE_SIZE, 4 * Utility.SPRITE_SIZE);
      orbPos.setLocation((24+32) * Utility.SPRITE_SIZE, 18 * Utility.SPRITE_SIZE);
      pwGrid.setCorner(new Point(passwordPos.x + 4 * Utility.SPRITE_SIZE,
          passwordPos.y + 4 * Utility.SPRITE_SIZE));
      pwGrid.reset();
      orbCount.getAnimation().setText(Integer.toString(pwGrid.maxOrbs() - pwGrid.numOrbs()));

      opacity = 255;

      upTime = 0;
      downTime = 0;
      leftTime = 0;
      rightTime = 0;

      frame = false;
      transitioning = false;
      fading = false;
      runningFadeTime = 0;

      acceptKeyEvents = true;

      getGameState().getMidiPlayer().play(pwMidi, true);
    } else {
      frameState = FrameState.LEVEL_END;
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;

    GameState gameState = getGameState();
    Dimension gameDimension = gameState.getGameDimension();
    Dimension screenDimension = gameState.getScreenDimension();
    double tx = screenDimension.width/2 - gameDimension.width/2, ty = screenDimension.height/2 - gameDimension.height/2 - 80;
    g2.setColor(Color.white);
    g2.fillRoundRect((int)tx - 10, (int)ty - 10, gameDimension.width + 20, gameDimension.height + 20, 8, 8);
    g2.setColor(Color.black);
    g2.fillRect((int)tx, (int)ty, gameDimension.width, gameDimension.height);

    g2.translate(tx, ty);
    g2.clipRect(0, 0, gameDimension.width, gameDimension.height);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setFont(getGameFont());

    g2.drawImage(tileBackground, 0, 0, null);


    if (frameState != null) {
      if (transitioning) {
        paintLeftFrame(g2);
        paintRightFrame(g2);
      } else {
        if (frame) {
          paintRightFrame(g2);
        } else {
          paintLeftFrame(g2);
        }
      }
    }
  }

  private void paintLeftFrame(Graphics2D g2) {
    switch (frameState) {
      case PASSWORD:
        g2.setColor(this.getBackground());
        g2.fillRect(selectorPos.x, selectorPos.y, selectorBox.getWidth(null) - Utility.SPRITE_SIZE,
            selectorBox.getHeight(null) - Utility.SPRITE_SIZE);
        g2.drawImage(selectorBox, selectorPos.x, selectorPos.y, null);

        g2.setColor(Color.white);
        g2.drawString(startOptions[0], selectorPos.x + 3 * Utility.SPRITE_SIZE,
            selectorPos.y + 3 * Utility.SPRITE_SIZE);
        g2.drawString(startOptions[1], selectorPos.x + 3 * Utility.SPRITE_SIZE,
            selectorPos.y + 5 * Utility.SPRITE_SIZE);

        selector.paint(g2);

        // Opacity filter
        g2.setColor(new Color(0, 0, 0, 255 - opacity));
        g2.fillRect(selectorPos.x + Utility.SPRITE_SIZE, selectorPos.y + Utility.SPRITE_SIZE,
            selectorBox.getWidth(null) - 3*Utility.SPRITE_SIZE,
            selectorBox.getHeight(null) - 3*Utility.SPRITE_SIZE);
        break;
      case LEVEL_END:
        break;
      }
  }

  private void paintRightFrame(Graphics2D g2) {
    switch (frameState) {
      case PASSWORD:
        g2.setColor(this.getBackground());
        g2.fillRect(passwordPos.x, passwordPos.y, passwordBox.getWidth(null) - Utility.SPRITE_SIZE,
            passwordBox.getHeight(null) - Utility.SPRITE_SIZE);
        g2.drawImage(passwordBox, passwordPos.x, passwordPos.y, null);
        pwGrid.paint(g2);

        // Orb box
        g2.setColor(this.getBackground());
        g2.fillRect(orbPos.x, orbPos.y, orbBox.getWidth(null) - Utility.SPRITE_SIZE,
            orbBox.getHeight(null) - Utility.SPRITE_SIZE);
        g2.drawImage(orbBox, orbPos.x, orbPos.y, null);
        g2.drawImage(orb, orbPos.x + (int)(1.5*Utility.SPRITE_SIZE), orbPos.y + 2*Utility.SPRITE_SIZE, null);
        orbCount.paint(g2);

        // Opacity filter
        g2.setColor(new Color(0, 0, 0, 255 - opacity));
        g2.fillRect(passwordPos.x + Utility.SPRITE_SIZE, passwordPos.y + Utility.SPRITE_SIZE,
            passwordBox.getWidth(null) - 3*Utility.SPRITE_SIZE,
            passwordBox.getHeight(null) - 3*Utility.SPRITE_SIZE);
        g2.fillRect(orbPos.x + Utility.SPRITE_SIZE, orbPos.y + Utility.SPRITE_SIZE,
            orbBox.getWidth(null) - 3*Utility.SPRITE_SIZE,
            orbBox.getHeight(null) - 3*Utility.SPRITE_SIZE);
        break;
      case LEVEL_END:
        break;
    }
  }

  @Override
  public void updateGame(long elapsedTime) {
    if (acceptKeyEvents) {
      processInput(elapsedTime);
    }

    switch (frameState) {
      case PASSWORD:
        if (fading) {
          runningFadeTime += elapsedTime;
          int interval = (int)(runningFadeTime / FADE_TIME);
          if (transitioning)
            opacity = 255 - (255 / FADE_INCREMENTS * interval);
          else
            opacity = 255 / FADE_INCREMENTS * interval;

          if (interval >= FADE_INCREMENTS) {
            fading = false;
            runningFadeTime = 0;
            if (!transitioning) {
              acceptKeyEvents = true;
            }
          }
        } else if (transitioning) {
          float dx = TRANS_SPEED * elapsedTime;
          dx *= frame ? 1 : -1; // if on left frame, transition right, else left
          selectorPos.setLocation(selectorPos.x + dx, selectorPos.y);
          passwordPos.setLocation(passwordPos.x + dx, passwordPos.y);
          orbPos.setLocation(orbPos.x + dx, orbPos.y);

          if (passwordPos.x <= 4 * Utility.SPRITE_SIZE) { // done transitioning
            selectorPos.setLocation((10-32) * Utility.SPRITE_SIZE, selectorPos.y);
            passwordPos.setLocation(4 * Utility.SPRITE_SIZE, passwordPos.y);
            orbPos.setLocation(24 * Utility.SPRITE_SIZE, orbPos.y);

            pwGrid.setCorner(new Point(passwordPos.x + 4 * Utility.SPRITE_SIZE,
                passwordPos.y + 4 * Utility.SPRITE_SIZE));
            orbCount.setLocation(orbPos.x + (2*Utility.SPRITE_SIZE), orbPos.y + 5*Utility.SPRITE_SIZE);

            selector.setEnabled(true);
            frame = true;
            transitioning = false;
            fading = true;
          }
        }

        if (frame) {
          pwGrid.update(elapsedTime);
          orbCount.update(elapsedTime);
        } else {
          selector.update(elapsedTime);
        }
        break;
      case LEVEL_END:
        break;
    }
  }

  private void processInput(long elapsedTime) {
    GameState gameState = getGameState();
    InputListener inputListener = gameState.getInputListener();
    switch (frameState) {
      case PASSWORD:
        if (!frame) { // start/password selection
          if (inputListener.hardKeyQuery(KeyEvent.VK_DOWN)) {
            gameState.getSoundManager().play(blip);
            selector.increment();
          }
          if (inputListener.hardKeyQuery(KeyEvent.VK_UP)) {
            gameState.getSoundManager().play(blip);
            selector.decrement();
          }
          if (inputListener.hardKeyQuery(KeyEvent.VK_ENTER)) {
            if (selector.getIndex() == 0) { // start
              acceptKeyEvents = false;
              gameState.getMidiPlayer().stop();
              
              GameTransitionEvent event = new GameTransitionEvent.Builder(GamePanel.Type.LEVEL_SELECT_PANEL).build();
              declareTransitionEvent(event);
              //firstRun = false;
            } else if (selector.getIndex() == 1) { // password
              /*acceptKeyEvents = false;
              selector.setEnabled(false);
              fading = true;
              transitioning = true;*/
            }
          }
        } else { // password grid
          if (inputListener.softKeyQuery(KeyEvent.VK_LEFT)) {
            leftTime += elapsedTime;
            if (leftTime >= 267) {
              leftTime = 0;
              pwGrid.move(Direction.LEFT);
            }
          } else if (leftTime > 0) {
            leftTime = 0;
            pwGrid.move(Direction.LEFT);
          }

          if (inputListener.softKeyQuery(KeyEvent.VK_RIGHT)) {
            rightTime += elapsedTime;
            if (rightTime >= 267) {
              rightTime = 0;
              pwGrid.move(Direction.RIGHT);
            }
          } else if (rightTime > 0) {
            rightTime = 0;
            pwGrid.move(Direction.RIGHT);
          }

          if (inputListener.softKeyQuery(KeyEvent.VK_DOWN)) {
            downTime += elapsedTime;
            if (downTime >= 267) {
              downTime = 0;
              pwGrid.move(Direction.DOWN);
            }
          } else if (downTime > 0) {
            downTime = 0;
            pwGrid.move(Direction.DOWN);
          }

          if (inputListener.softKeyQuery(KeyEvent.VK_UP)) {
            upTime += elapsedTime;
            if (upTime >= 267) {
              upTime = 0;
              pwGrid.move(Direction.UP);
            }
          } else if (upTime > 0) {
            upTime = 0;
            pwGrid.move(Direction.UP);
          }

          if (inputListener.hardKeyQuery(KeyEvent.VK_Z)) {
            pwGrid.removeOrb();
            orbCount.getAnimation().setText(Integer.toString(pwGrid.maxOrbs() - pwGrid.numOrbs()));
          }
          if (inputListener.hardKeyQuery(KeyEvent.VK_X)) {
            pwGrid.addOrb();
            orbCount.getAnimation().setText(Integer.toString(pwGrid.maxOrbs() - pwGrid.numOrbs()));
            if (pwGrid.numOrbs() >= pwGrid.maxOrbs()) {
              acceptKeyEvents = false;
              pwGrid.setSelectorEnabled(false);
              orbCount.setEnabled(false);

              /*GameState newState = pwGrid.getGameState();
              if (newState == null) {

              } else {
                state = newState;
              }*/
            }
          }
        }
        break;
      case LEVEL_END:
        break;
    }
  }

  private enum FrameState {
    PASSWORD, LEVEL_END;
  }
}