package panels;

import graphics.Animation;
import graphics.Sprite;
import graphics.objects.bosses.Airman;
import graphics.objects.bosses.Boss;
import graphics.objects.bosses.Bubbleman;
import graphics.objects.bosses.Crashman;
import graphics.objects.bosses.Flashman;
import graphics.objects.bosses.Heatman;
import graphics.objects.bosses.Metalman;
import graphics.objects.bosses.Quickman;
import graphics.objects.bosses.Woodman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import javax.sound.midi.Sequence;

import application.GameState;
import application.Utility;
import audio.Sound;

public class LevelSelectPanel extends GamePanel
{
  private static final long serialVersionUID = 1L;
  private static final float[] STAR_SPEEDS = { 0.5f, 0.15f, 0.02f }; // pixels per millisecond
  private static final float FALL_SPEED = 0.6f;

  private int frameState; // 0=level select, 1=level intro with boss animation

  private Sequence selectMidi, levelIntroMidi;

  private Sound blip;

  private Image background;

  private BossIcon[][] icons;
  private Sprite boxSelector;
  private int col, row;

  private Image banner;
  private Image[] stars; // length 6: first 3 are top layers, last 3 are bottom layers
  private float[] starPos; // length 3: x-positions of corresponding top and bottom layer pair
  private long starTime;
  private int bossState; // (0=on top, 1=falling...)
  private Boss boss;
  private String name; // starts as an empty string and adds letters as starTime increases

  private boolean flashing; // true when flashing
  private boolean flash; // only used when flashing, alternates
  private long flashTime;

  public LevelSelectPanel(GameState state)
  {
    super(state);
    setBackground(Color.black);

    String audioBase = "res/audio/midis/";
    selectMidi = midiPlayer.getSequence(audioBase + "select_stage.mid");
    levelIntroMidi = midiPlayer.getSequence(audioBase + "level_intro.mid");

    blip = soundManager.getSound("res/audio/sound_effects/blip1.wav");

    String imageBase = "res/images/overworld/";
    background = Utility.loadImage(imageBase + "select_background.png");

    icons = new BossIcon[3][3];
    /*icons[0][0] = new BossIcon(Utility.loadImage(imageBase + "bubbleman.png"), "BUBBLE", "MAN", state.isBeaten(GameState.BUBBLEMAN));
    icons[0][1] = new BossIcon(Utility.loadImage(imageBase + "airman.png"), "AIR", "MAN", state.isBeaten(GameState.AIRMAN));
    icons[0][2] = new BossIcon(Utility.loadImage(imageBase + "quickman.png"), "QUICK", "MAN", state.isBeaten(GameState.QUICKMAN));
    icons[1][0] = new BossIcon(Utility.loadImage(imageBase + "heatman.png"), "HEAT", "MAN", state.isBeaten(GameState.HEATMAN));
    icons[1][1] = new BossIcon(Utility.loadImage(imageBase + "dr_wily.png"), "DR.", "WILY", state.isBeaten(GameState.DR_WILY));
    icons[1][2] = new BossIcon(Utility.loadImage(imageBase + "woodman.png"), "WOOD", "MAN", state.isBeaten(GameState.WOODMAN));
    icons[2][0] = new BossIcon(Utility.loadImage(imageBase + "metalman.png"), "METAL", "MAN", state.isBeaten(GameState.METALMAN));
    icons[2][1] = new BossIcon(Utility.loadImage(imageBase + "flashman.png"), "FLASH", "MAN", state.isBeaten(GameState.FLASHMAN));
    icons[2][2] = new BossIcon(Utility.loadImage(imageBase + "crashman.png"), "CRASH", "MAN", state.isBeaten(GameState.CRASHMAN));*/
    icons[0][0] = new BossIcon(Utility.loadImage(imageBase + "bubbleman.png"), "", "", state.isBeaten(GameState.BUBBLEMAN));
    icons[0][1] = new BossIcon(Utility.loadImage(imageBase + "airman.png"), "", "", state.isBeaten(GameState.AIRMAN));
    icons[0][2] = new BossIcon(Utility.loadImage(imageBase + "quickman.png"), "", "", state.isBeaten(GameState.QUICKMAN));
    icons[1][0] = new BossIcon(Utility.loadImage(imageBase + "heatman.png"), "", "", state.isBeaten(GameState.HEATMAN));
    icons[1][1] = new BossIcon(Utility.loadImage(imageBase + "dr_wily.png"), "BEGIN", "FIGHT", state.isBeaten(GameState.DR_WILY));
    icons[1][2] = new BossIcon(Utility.loadImage(imageBase + "woodman.png"), "", "", state.isBeaten(GameState.WOODMAN));
    icons[2][0] = new BossIcon(Utility.loadImage(imageBase + "metalman.png"), "", "", state.isBeaten(GameState.METALMAN));
    icons[2][1] = new BossIcon(Utility.loadImage(imageBase + "flashman.png"), "", "", state.isBeaten(GameState.FLASHMAN));
    icons[2][2] = new BossIcon(Utility.loadImage(imageBase + "crashman.png"), "", "", state.isBeaten(GameState.CRASHMAN));

    banner = Utility.loadImage(imageBase + "banner.png");

    stars = new Image[6];
    String starBase = "res/images/overworld/stars/";
    for (int i = 0; i < 3; i++)
    {
      stars[i] = Utility.loadImage(starBase + "top" + (i+1) + ".png");
      stars[i+3] = Utility.loadImage(starBase + "bottom" + (i+1) + ".png");
    }

    starPos = new float[3];

    Animation anim = new Animation();
    anim.addFrame(Utility.loadImage(imageBase + "box_selector.png"), 133);
    anim.addFrame(null, 133);
    boxSelector = new Sprite(new Animation[] {anim});
  }

  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;

    double tx = screenDim.width/2 - dim.width/2, ty = screenDim.height/2 - dim.height/2 - 80;
    g2.setColor(Color.white);
    g2.fillRoundRect((int)tx - 10, (int)ty - 10, dim.width + 20, dim.height + 20, 8, 8);
    g2.setColor(Color.black);
    g2.fillRect((int)tx, (int)ty, dim.width, dim.height);

    g2.translate(tx, ty);
    g2.clipRect(0, 0, dim.width, dim.height);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setFont(gameFont);

    if (frameState == 0)
    {
      if (flashing)
      {
        if (flash)
          g2.setXORMode(Color.gray);
        else
          g2.setPaintMode();
      }

      g2.drawImage(background, 0, 0, null);

      g2.setColor(Color.white);
      for (int i = 0; i < icons.length; i++)
      {
        for (int j = 0; j < icons[i].length; j++)
        {
          int x = 5*Utility.SPRITE_SIZE + 8*Utility.SPRITE_SIZE*j;
          int y = (int)(3.25*Utility.SPRITE_SIZE) + 8*Utility.SPRITE_SIZE*i;
          g2.drawImage(icons[i][j].icon, x, y, null);

          boxSelector.paint(g2);

          if (icons[i][j].beaten)
          {
            g2.setColor(Color.black);
            g2.fillRect(x + Utility.SPRITE_SIZE, y + Utility.SPRITE_SIZE,
                4 * Utility.SPRITE_SIZE, 4 * Utility.SPRITE_SIZE);
          }
          else
          {
            if (icons[i][j].line1.length() > 3)
              g2.drawString(icons[i][j].line1,
                  x - (int)(0.25*Utility.SPRITE_SIZE),
                  y + (int)(6.7*Utility.SPRITE_SIZE));
            else
              g2.drawString(icons[i][j].line1, x + (int)(0.8*Utility.SPRITE_SIZE), y + (int)(6.7*Utility.SPRITE_SIZE));

            g2.drawString(icons[i][j].line2,
                Utility.rightJustify(icons[i][j].line2, gameFont, x + 5*Utility.SPRITE_SIZE) + (int)(0.7*Utility.SPRITE_SIZE),
                y + (int)(7.75*Utility.SPRITE_SIZE));
          }
        }
      }
    }
    else if (frameState == 1)
    {
      for (int i = 0; i < starPos.length; i++)
      {
        g2.drawImage(stars[i], (int)starPos[i] - stars[i].getWidth(null), 0, null);
        g2.drawImage(stars[i], (int)starPos[i], 0, null);
        g2.drawImage(stars[i+3], (int)starPos[i] - stars[i+3].getWidth(null), dim.height - stars[i+3].getHeight(null), null);
        g2.drawImage(stars[i+3], (int)starPos[i], dim.height - stars[i+3].getHeight(null), null);
      }

      g2.drawImage(banner, 0, stars[0].getHeight(null), null);

      boss.paint(g2);

      if (bossState >= 3)
      {
        g2.setColor(Color.white);
        g2.drawString(name, Utility.xCenterText(name, gameFont, dim), dim.height/2 + 25);
      }
    }
  }

  @Override
  public void start()
  {
    super.start();

    for (int i = 0; i < GameState.NUM_BOSSES; i++)
      if (state.isBeaten(i))
        icons[i/3][i%3] = null;

    frameState = 0;
    flashing = false;

    row = 1;
    col = 1;

    bossState = 0;
    starTime = 0;

    boxSelector.setPosition(5*Utility.SPRITE_SIZE + 8*Utility.SPRITE_SIZE*col,
        (int)(3.25*Utility.SPRITE_SIZE) + 8*Utility.SPRITE_SIZE*row);

    midiPlayer.play(selectMidi, true);
  }

  @Override
  public void updateGame(long elapsedTime)
  {
    if (frameState == 0)
    {
      if (flashing)
      {
        flashTime += elapsedTime;
        flash = ((flashTime / 67) % 2 == 0);

        if (flashTime > 1000)
        {
          flashing = false;

          midiPlayer.play(levelIntroMidi, false);

          frameState = 1;
        }
      }
      else
      {
        boxSelector.update(elapsedTime);
        processInput();
      }
    }
    else // frameState == 1
    {
      for (int i = 0; i < starPos.length; i++)
      {
        starPos[i] += STAR_SPEEDS[i] * elapsedTime;
        if (starPos[i] >= dim.width)
          starPos[i] = 0;
      }

      starTime += elapsedTime;
      switch (bossState)
      {
      case 0:
        if (starTime > 500)
        {
          boss.setState(Boss.STATE_FALL);

          starTime = 0;
          bossState++;
        }
        break;
      case 1:
        boss.setY(boss.getY() + elapsedTime * FALL_SPEED);
        int destY = stars[0].getHeight(null) + banner.getHeight(null)/2 - boss.getHeight()/2 - 15;
        if (boss.getY() >= destY)
        {
          boss.setY(destY);
          boss.setState(Boss.STATE_TAUNT);

          starTime = 0;
          bossState++;
        }
        break;
      case 2:
        if (starTime > 2500)
        {
          name = "";
          starTime = 0;
          bossState++;
        }
        break;
      case 3:
        int letters = (int)starTime / 200;
        if (letters < boss.getName().length())
          name = boss.getName().substring(0, letters+1);
        else if (letters > boss.getName().length() + 4)
        {
          starTime = 0;
          bossState++;
        }
        break;
      case 4:
        if (starTime > 2000)
          setDone(true);
        break;
      }

      boss.update(elapsedTime);
    }
  }

  private void processInput()
  {
    if (inputListener.hardKeyQuery(KeyEvent.VK_ENTER))
    {
      if (!icons[row][col].beaten && row == 1 && col == 1)
      {
        flashing = true;
        midiPlayer.stop();

        boss = loadBoss();
        boss.setPosition(dim.width/2 - boss.getWidth()/2, -boss.getHeight());

        return;
      }
    }

    boolean moved = false;
    if (inputListener.hardKeyQuery(KeyEvent.VK_DOWN))
    {
      moved = true;
      if (row < 2)
        row++;
    }
    if (inputListener.hardKeyQuery(KeyEvent.VK_UP))
    {
      moved = true;
      if (row > 0)
        row--;
    }
    if (inputListener.hardKeyQuery(KeyEvent.VK_RIGHT))
    {
      moved = true;
      if (col < 2)
        col++;
    }
    if (inputListener.hardKeyQuery(KeyEvent.VK_LEFT))
    {
      moved = true;
      if (col > 0)
        col--;
    }

    if (moved)
    {
      boxSelector.setPosition(5*Utility.SPRITE_SIZE + 8*Utility.SPRITE_SIZE*col,
          (int)(3.25*Utility.SPRITE_SIZE) + 8*Utility.SPRITE_SIZE*row);
      soundManager.play(blip);
    }
  }

  private Boss loadBoss()
  {
    if (row == 0)
    {
      if (col == 0)
        return new Bubbleman();
      else if (col == 1)
        return new Airman();
      else
        return new Quickman();
    }
    else if (row == 1)
    {
      if (col == 0)
        return new Heatman();
      else if (col == 1)
        return new Airman();
      else
        return new Woodman();
    }
    else
    {
      if (col == 0)
        return new Metalman();
      else if (col == 1)
        return new Flashman();
      else
        return new Crashman();
    }
  }

  private class BossIcon
  {
    final Image icon;
    final String line1, line2;
    boolean beaten;

    public BossIcon(Image icon, String line1, String line2, boolean beaten)
    {
      this.icon = icon;
      this.line1 = line1;
      this.line2 = line2;
      this.beaten = beaten;
    }
  }
}