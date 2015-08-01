package panels;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.Sequence;

import application.GameState;
import application.GameTransitionEvent;
import application.GameTransitionEventKey;
import application.Utility;

import audio.Sound;

import graphics.Animation;
import graphics.Sprite;
import graphics.objects.bosses.Airman;
import graphics.objects.bosses.Boss;
import graphics.objects.bosses.BossType;
import graphics.objects.bosses.Bubbleman;
import graphics.objects.bosses.Crashman;
import graphics.objects.bosses.Flashman;
import graphics.objects.bosses.Heatman;
import graphics.objects.bosses.Metalman;
import graphics.objects.bosses.Quickman;
import graphics.objects.bosses.Woodman;

import input.InputListener;

public class LevelSelectPanel extends GamePanel {
  private static final BossType[][] BOSS_LAYOUT =
    new BossType[][] {
      { BossType.BUBBLEMAN, BossType.AIRMAN, BossType.QUICKMAN },
      { BossType.HEATMAN, BossType.DR_WILY, BossType.WOODMAN },
      { BossType.METALMAN, BossType.FLASHMAN, BossType.CRASHMAN }
    };

  private static final float[] STAR_SPEEDS = { 0.5f, 0.15f, 0.02f }; // pixels per millisecond
  private static final float FALL_SPEED = 0.6f;

  private int frameState; // 0=level select, 1=level intro with boss animation

  private Sequence selectMidi, levelIntroMidi;

  private Sound blip;

  private Image background;

  private Map<BossType, BossIcon> bossTypeIconMap;
  private Sprite boxSelector;
  private int selectedColumn;
  private int selectedRow;

  private Image banner;
  private Image[] stars; // length 6: first 3 are top layers, last 3 are bottom layers
  private float[] starPos; // length 3: x-positions of corresponding top and bottom layer pair
  private long starTime;
  private int bossState; // (0=on top, 1=falling...)
  private Boss selectedBoss;
  private String name; // starts as an empty string and adds letters as starTime increases

  private boolean flashing; // true when flashing
  private boolean flash; // only used when flashing, alternates
  private long flashTime;

  public LevelSelectPanel(GameState gameState) {
    super(gameState);
    setBackground(Color.black);

    selectedRow = 1;
    selectedColumn = 1;

    String audioBase = "res/audio/midis/";
    selectMidi = gameState.getMidiPlayer().getSequence(audioBase + "select_stage.mid");
    levelIntroMidi = gameState.getMidiPlayer().getSequence(audioBase + "level_intro.mid");

    blip = gameState.getSoundManager().getSound("res/audio/sound_effects/blip1.wav");

    String imageBase = "res/images/overworld/";
    background = Utility.loadImage(imageBase + "select_background.png");

    bossTypeIconMap = new ImmutableMap.Builder<BossType, BossIcon>()
      .put(BossType.BUBBLEMAN, new BossIcon(Utility.loadImage(imageBase + "bubbleman.png"), "BUBBLE", "MAN"))
      .put(BossType.AIRMAN, new BossIcon(Utility.loadImage(imageBase + "airman.png"), "AIR", "MAN"))
      .put(BossType.QUICKMAN, new BossIcon(Utility.loadImage(imageBase + "quickman.png"), "QUICK", "MAN"))
      .put(BossType.HEATMAN, new BossIcon(Utility.loadImage(imageBase + "heatman.png"), "HEAT", "MAN"))
      .put(BossType.DR_WILY, new BossIcon(Utility.loadImage(imageBase + "dr_wily.png"), "DR.", "WILY"))
      .put(BossType.WOODMAN, new BossIcon(Utility.loadImage(imageBase + "woodman.png"), "WOOD", "MAN"))
      .put(BossType.METALMAN, new BossIcon(Utility.loadImage(imageBase + "metalman.png"), "METAL", "MAN"))
      .put(BossType.FLASHMAN, new BossIcon(Utility.loadImage(imageBase + "flashman.png"), "FLASH", "MAN"))
      .put(BossType.CRASHMAN, new BossIcon(Utility.loadImage(imageBase + "crashman.png"), "CRASH", "MAN"))
      .build();
    for (Map.Entry<BossType, BossIcon> entry : bossTypeIconMap.entrySet()) {
      BossIcon icon = entry.getValue();
      icon.isCleared = gameState.isBossCleared(entry.getKey());
    }

    banner = Utility.loadImage(imageBase + "banner.png");

    stars = new Image[6];
    String starBase = "res/images/overworld/stars/";
    for (int i = 0; i < 3; i++) {
      stars[i] = Utility.loadImage(starBase + "top" + (i+1) + ".png");
      stars[i+3] = Utility.loadImage(starBase + "bottom" + (i+1) + ".png");
    }

    starPos = new float[3];

    Animation anim = new Animation();
    anim.addFrame(Utility.loadImage(imageBase + "box_selector.png"), 133);
    anim.addFrame(null, 133);
    boxSelector = new Sprite(new Animation[] {anim});
    boxSelector.setPosition(5*Utility.SPRITE_SIZE + 8*Utility.SPRITE_SIZE*selectedColumn,
        (int)(3.25*Utility.SPRITE_SIZE) + 8*Utility.SPRITE_SIZE*selectedRow);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;

    Dimension screenDimension = getGameState().getScreenDimension();
    Dimension gameDimension = getGameState().getGameDimension();
    double tx = screenDimension.width/2 - gameDimension.width/2, ty = screenDimension.height/2 - gameDimension.height/2 - 80;
    g2.setColor(Color.white);
    g2.fillRoundRect((int)tx - 10, (int)ty - 10, gameDimension.width + 20, gameDimension.height + 20, 8, 8);
    g2.setColor(Color.black);
    g2.fillRect((int)tx, (int)ty, gameDimension.width, gameDimension.height);

    g2.translate(tx, ty);
    g2.clipRect(0, 0, gameDimension.width, gameDimension.height);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setFont(getGameFont());

    if (frameState == 0) {
      if (flashing) {
        if (flash)
          g2.setXORMode(Color.gray);
        else
          g2.setPaintMode();
      }

      g2.drawImage(background, 0, 0, null);

      g2.setColor(Color.white);
      for (int i = 0; i < BOSS_LAYOUT.length; i++) {
        for (int j = 0; j < BOSS_LAYOUT[i].length; j++) {
          BossIcon bossIcon = bossTypeIconMap.get(BOSS_LAYOUT[i][j]);
          int x = 5*Utility.SPRITE_SIZE + 8*Utility.SPRITE_SIZE*j;
          int y = (int)(3.25*Utility.SPRITE_SIZE) + 8*Utility.SPRITE_SIZE*i;
          g2.drawImage(bossIcon.icon, x, y, null);

          boxSelector.paint(g2);

          if (bossIcon.isCleared) {
            g2.setColor(Color.black);
            g2.fillRect(x + Utility.SPRITE_SIZE, y + Utility.SPRITE_SIZE,
                4 * Utility.SPRITE_SIZE, 4 * Utility.SPRITE_SIZE);
          } else {
            if (bossIcon.topTitleName.length() > 3)
              g2.drawString(bossIcon.topTitleName,
                  x - (int)(0.25*Utility.SPRITE_SIZE),
                  y + (int)(6.7*Utility.SPRITE_SIZE));
            else
              g2.drawString(bossIcon.topTitleName, x + (int)(0.8*Utility.SPRITE_SIZE), y + (int)(6.7*Utility.SPRITE_SIZE));

            g2.drawString(bossIcon.bottomTitleName,
                Utility.rightJustify(bossIcon.bottomTitleName, getGameFont(), x + 5*Utility.SPRITE_SIZE) + (int)(0.7*Utility.SPRITE_SIZE),
                y + (int)(7.75*Utility.SPRITE_SIZE));
          }
        }
      }
    } else if (frameState == 1) {
      for (int i = 0; i < starPos.length; i++) {
        g2.drawImage(stars[i], (int)starPos[i] - stars[i].getWidth(null), 0, null);
        g2.drawImage(stars[i], (int)starPos[i], 0, null);
        g2.drawImage(stars[i+3], (int)starPos[i] - stars[i+3].getWidth(null), gameDimension.height - stars[i+3].getHeight(null), null);
        g2.drawImage(stars[i+3], (int)starPos[i], gameDimension.height - stars[i+3].getHeight(null), null);
      }

      g2.drawImage(banner, 0, stars[0].getHeight(null), null);

      selectedBoss.paint(g2);

      if (bossState >= 3) {
        g2.setColor(Color.white);
        g2.drawString(name, Utility.xCenterText(name, getGameFont(), gameDimension), gameDimension.height/2 + 25);
      }
    }
  }

  @Override
  public void prepareForUpdates(GameTransitionEvent transitionEvent) {
    getGameState().getMidiPlayer().play(selectMidi, true);
  }

  @Override
  public void updateGame(long elapsedTime) {
    if (frameState == 0) {
      if (flashing) {
        flashTime += elapsedTime;
        flash = ((flashTime / 67) % 2 == 0);

        if (flashTime > 1000) {
          flashing = false;

          getGameState().getMidiPlayer().play(levelIntroMidi, false);

          frameState = 1;
        }
      } else {
        boxSelector.update(elapsedTime);
        processInput();
      }
    } else {
      for (int i = 0; i < starPos.length; i++) {
        starPos[i] += STAR_SPEEDS[i] * elapsedTime;
        if (starPos[i] >= getGameState().getGameDimension().width)
          starPos[i] = 0;
      }

      starTime += elapsedTime;
      switch (bossState) {
      case 0:
        if (starTime > 500) {
          selectedBoss.setState(Boss.STATE_FALL);

          starTime = 0;
          bossState++;
        }
        break;
      case 1:
        selectedBoss.setY(selectedBoss.getY() + elapsedTime * FALL_SPEED);
        int destY = stars[0].getHeight(null) + banner.getHeight(null)/2 - selectedBoss.getHeight()/2 - 15;
        if (selectedBoss.getY() >= destY) {
          selectedBoss.setY(destY);
          selectedBoss.setState(Boss.STATE_TAUNT);

          starTime = 0;
          bossState++;
        }
        break;
      case 2:
        if (starTime > 2500) {
          name = "";
          starTime = 0;
          bossState++;
        }
        break;
      case 3:
        int letters = (int)starTime / 200;
        String bossName = selectedBoss.getType().getBossName();
        if (letters < bossName.length()) {
          name = bossName.substring(0, letters+1);
        } else if (letters > bossName.length() + 4) {
          starTime = 0;
          bossState++;
        }
        break;
      case 4:
        if (starTime > 2000) {
          GameTransitionEvent event = new GameTransitionEvent.Builder(GamePanel.Type.LEVEL_PLAY_PANEL)
            .putValue(GameTransitionEventKey.LEVEL_SELECT_BOSS_TYPE, selectedBoss.getType())
            .build();
          declareTransitionEvent(event);
        }
        break;
      }

      selectedBoss.update(elapsedTime);
    }
  }

  private void processInput() {
    GameState gameState = getGameState();
    InputListener inputListener = gameState.getInputListener();
    if (inputListener.hardKeyQuery(KeyEvent.VK_ENTER)) {
      Preconditions.checkElementIndex(selectedRow, BOSS_LAYOUT.length);
      Preconditions.checkElementIndex(selectedColumn, BOSS_LAYOUT[selectedRow].length);

      boolean shouldLoadBoss;
      BossType selectedBossType = BOSS_LAYOUT[selectedRow][selectedColumn];
      if (selectedBossType == BossType.DR_WILY) {
        shouldLoadBoss = isFinalBossUnlocked();
      } else {
        shouldLoadBoss = !gameState.isBossCleared(selectedBossType);
      }
      if (shouldLoadBoss) {
        flashing = true;
        gameState.getMidiPlayer().stop();

        selectedBoss = BossType.createBoss(selectedBossType);
        Dimension gameDimension = gameState.getGameDimension();
        selectedBoss.setPosition(gameDimension.width/2 - selectedBoss.getWidth()/2, -selectedBoss.getHeight());

        return;
      }
    }

    boolean moved = false;
    if (inputListener.hardKeyQuery(KeyEvent.VK_DOWN)) {
      moved = true;
      if (selectedRow < 2)
        selectedRow++;
    }
    if (inputListener.hardKeyQuery(KeyEvent.VK_UP)) {
      moved = true;
      if (selectedRow > 0)
        selectedRow--;
    }
    if (inputListener.hardKeyQuery(KeyEvent.VK_RIGHT)) {
      moved = true;
      if (selectedColumn < 2)
        selectedColumn++;
    }
    if (inputListener.hardKeyQuery(KeyEvent.VK_LEFT)) {
      moved = true;
      if (selectedColumn > 0)
        selectedColumn--;
    }

    if (moved) {
      boxSelector.setPosition(5*Utility.SPRITE_SIZE + 8*Utility.SPRITE_SIZE*selectedColumn,
          (int)(3.25*Utility.SPRITE_SIZE) + 8*Utility.SPRITE_SIZE*selectedRow);
      gameState.getSoundManager().play(blip);
    }
  }

  private boolean isFinalBossUnlocked() {
    for (BossType type : bossTypeIconMap.keySet()) {
      if (type != BossType.DR_WILY && !getGameState().isBossCleared(type)) {
        return false;
      }
    }
    return true;
  }

  private class BossIcon {
    final Image icon;
    final String topTitleName;
    final String bottomTitleName;
    boolean isCleared;

    public BossIcon(Image icon, String topTitleName, String bottomTitleName) {
      this.icon = icon;
      this.topTitleName = topTitleName;
      this.bottomTitleName = bottomTitleName;
    }
  }
}