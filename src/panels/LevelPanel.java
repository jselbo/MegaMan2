package panels;

import static application.Utility.BLOCK_SIZE;
import graphics.Direction;
import graphics.Sprite;
import graphics.objects.Block;
import graphics.objects.Bullet;
import graphics.objects.Health;
import graphics.objects.Megaman;
import graphics.objects.enemies.Bird;
import graphics.objects.enemies.Enemy;
import graphics.objects.enemies.Metroid;
import graphics.objects.enemies.Spring;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.sound.midi.Sequence;

import application.GameState;
import application.Utility;

public class LevelPanel extends GamePanel
{
  private static final long serialVersionUID = 1L;

  // Keyboard constants
  private static final int JUMP_KEY = KeyEvent.VK_W;

  // Physics constants
  private static final float GRAVITY = 0.0009f;
  private static final float RUN_VELOCITY = 0.2f;
  private static final float FRICTION = 0.02f;
  private static final float JUMP_VELOCITY = 0.45f;
  private static final float JUMP_THRESHOLD = 0.1f;
  private static final float BULLET_VELOCITY = 1.25f;

  // Audio
  private Sequence[] midis;
  private int wave;
  private long waveTime;

  private Block[][][] grid; // screens X rows X columns
  private Megaman megaman;
  private int screen;

  private List<Bullet> bullets;
  private List<Enemy> enemies;
  private Health health;

  private Random rand;
  private long birdTimer, metroidTimer, springTimer;
  private long birdSpawn, metroidSpawn, springSpawn;

  private int score;
  private boolean gameOver;

  // Input variables
  private int inputDirection; // (-1=left, 0=idle, 1=right)
  private boolean inputJump;
  private boolean inputClick;
  private int mouseX, mouseY; // -1 if not clicked

  private double tx, ty;

  private int mState; // Megaman's state
  private boolean jumping;

  public LevelPanel(GameState state)
  {
    super(state);

    grid = new Block[1][][];
    grid[0] = new Block[16][16];

    Image[] reg = new Image[8], stk = new Image[8];
    for (int i = 0; i < reg.length; i++)
    {
      reg[i] = Utility.loadImage("res/images/tilesets/temp/block" + (i+1) + ".png");
      stk[i] = Utility.loadImage("res/images/tilesets/temp/sticky" + (i+1) + ".png");
    }

    grid[0][0][0] = new Block(0 * BLOCK_SIZE, 0, reg[5]);
    for (int i = 1; i < 12; i++)
      grid[0][0][i] = new Block(i * BLOCK_SIZE, 0, reg[2]);
    for (int i = 12; i < 15; i++)
      grid[0][0][i] = new Block(i * BLOCK_SIZE, 0, stk[2]);
    grid[0][0][15] = new Block(15 * BLOCK_SIZE, 0, stk[0]);


    for (int i = 0; i < grid[0][9].length; i++)
      if (i < 5 || i > 10)
        grid[0][9][i] = new Block(i * BLOCK_SIZE, 9 * BLOCK_SIZE, reg[2]);

    for (int i = 1; i < grid[0].length - 1; i++)
      grid[0][i][0] = new Block(0, i * BLOCK_SIZE, reg[4]);
    for (int i = 1; i < grid[0][15].length - 1; i++)
      grid[0][i][15] = new Block(15*BLOCK_SIZE, i * BLOCK_SIZE, stk[4]);
    for (int i = 1; i < grid[0][13].length - 1; i++)
      grid[0][13][i] = new Block(i * BLOCK_SIZE, 13 * BLOCK_SIZE, reg[2]);

    bullets = new ArrayList<Bullet>(3);
    enemies = new ArrayList<Enemy>();

    tx = screenDim.width/2 - dim.width/2;
    ty = screenDim.height/2 - dim.height/2 - 80;

    rand = new Random();

    midis = new Sequence[8];
    for (int i = 0; i < midis.length; i++)
      midis[i] = midiPlayer.getSequence("res/audio/midis/wave" + (i+1) + ".mid");

    megaman = new Megaman();

    setBackground(Color.black);
    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
  }

  @Override
  public void start()
  {
    super.start();

    megaman.reset();
    megaman.setPosition(75, 100);

    bullets.clear();
    enemies.clear();
    health = null;

    birdTimer = 0;
    metroidTimer = 0;
    springTimer = 0;
    birdSpawn = 2000;
    metroidSpawn = 5000;
    springSpawn = 500;

    inputDirection = 0;
    inputClick = false;

    score = 0;
    gameOver = false;

    screen = 0;

    waveTime = 0;
    wave = 0;
    midiPlayer.stop();
    midiPlayer.play(midis[wave], false);
  }

  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;

    g2.setColor(Color.white);
    g2.fillRoundRect((int)tx - 10, (int)ty - 10, dim.width + 20, dim.height + 20, 8, 8);
    g2.setColor(Color.black);
    g2.fillRect((int)tx, (int)ty, dim.width, dim.height);

    g2.translate(tx, ty);
    g2.clipRect(0, 0, dim.width, dim.height);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setFont(gameFont);

    BufferedImage bg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2n = bg.createGraphics();

    for (int i = 0; i < grid[screen].length; i++)
    {
      for (int j = 0; j < grid[screen][i].length; j++)
      {
        if (grid[screen][i][j] != null)
          grid[screen][i][j].paint(g2n);
      }
    }

    megaman.paint(g2n);

    for (Enemy e : enemies)
      e.paint(g2n);

    if (health != null)
      health.paint(g2n);

    for (Bullet b : bullets)
      b.paint(g2n);

    g2n.setColor(new Color(255, 165, 0, 220));
    g2n.fillRoundRect(dim.width/2 - 40, 5, 90, 20, 12, 12);
    Font ff = gameFont.deriveFont(12.0f);
    String ss = "Wave " + wave;
    g2n.setColor(Color.black);
    g2n.setFont(ff);
    g2n.drawString(ss, Utility.xCenterText(ss, ff, dim), 20);

    BufferedImageOp op = null;
    if (gameOver)
    {
      float[] matrix = new float[25];
      Arrays.fill(matrix, 1.0f/25.0f);
      op = new ConvolveOp(new Kernel(5, 5, matrix));
    }

    g2.drawImage(bg, op, 0, 0);

    g2.setColor(Color.black);
    String s = Integer.toString(score);
    g2.drawString(s, dim.width - s.length()*20, 20);

    if (gameOver)
    {
      g2.setColor(Color.white);
      String s1 = "Game Over";
      Font f1 = gameFont.deriveFont(48.0f);
      Point c1 = Utility.centerText(s1, f1, dim);
      g2.setFont(f1);
      g2.drawString(s1, c1.x, c1.y);

      String s2 = "Ctrl+R to restart";
      Font f2 = gameFont.deriveFont(22.0f);
      g2.setFont(f2);
      g2.drawString(s2, Utility.xCenterText(s2, f2, dim), 250);
    }
  }

  @Override
  public void updateGame(long elapsedTime)
  {
    processInputs();

    if (!gameOver)
    {
      updatePositions(elapsedTime);   // performs two tasks: does physics calculations and collision checks
                      // and then updates the positions of all objects accordingly
    }

    waveTime += elapsedTime;
    if (waveTime > 30000L)
    {
      midiPlayer.stop();
      waveTime = 0;
      wave++;
      midiPlayer.play(midis[wave], false);
    }
  }

  private void processInputs()
  {
    /* Key input */
    if (inputListener.hardKeyQuery(KeyEvent.VK_R) && inputListener.hardKeyQuery(KeyEvent.VK_CONTROL))
    {
      setDone(true);
      return;
    }

    // Process arrow keys
    boolean right = inputListener.softKeyQuery(KeyEvent.VK_D);
    boolean left = inputListener.softKeyQuery(KeyEvent.VK_A);

    if (right ^ left)
    {
      if (right)
        inputDirection = 1;
      else
        inputDirection = -1;
    }
    else
      inputDirection = 0;

    // Jump key
    inputJump = inputListener.softKeyQuery(JUMP_KEY);

    /* Mouse input */
    if (inputListener.hardMouseQuery())
    {
      mouseX = (int)(inputListener.getMouseX() - tx);
      mouseY = (int)(inputListener.getMouseY() - ty);
      inputClick = true;
    }
  }

  private void updatePositions(long elapsedTime)
  {
    float a_x = 0;
    float a_y = GRAVITY;
    mState = -1;

    /* Step 1: Physics */
    boolean F_N = false;

    int u = getRow(megaman.getY());
    int d = getRow(megaman.getY() + megaman.getHeight());
    int l = getCol(megaman.getX());
    int r = getCol(megaman.getX() + megaman.getWidth());

    int right = megaman.getVelocityX() > 0 ? 1 : (megaman.getVelocityX() < 0 ? -1 : 0); // (-1=left, 0=idle, 1=right)
    int up = megaman.getVelocityY() < 0 ? 1 : (megaman.getVelocityY() > 0 ? -1 : 0); // (-1=left, 0=idle, 1=right)

    // Block collisions
    if (right == 1)
    {
      if (grid[screen][u][r] != null && megaman.getY() < BLOCK_SIZE * (u+1) - 5)
      {
        megaman.setX(BLOCK_SIZE * r);
        megaman.setVelocityX(0);
      }
    }
    else if (right == -1)
    {
      if (grid[screen][u][l] != null && megaman.getY() < BLOCK_SIZE * (u+1) - 5
          /*(grid[screen][u][l] != null && megaman.getY() < BLOCK_SIZE * (u+1) - 5) ||
          (grid[screen][d][l] != null && megaman.getY() > BLOCK_SIZE * d + 5)*/)
      {
        megaman.setX(BLOCK_SIZE * (l+1));
        megaman.setVelocityX(0);
      }
    }

    if (up == 1)
    {
      if ((grid[screen][u][l] != null && megaman.getX() < BLOCK_SIZE * l) || (grid[screen][u][r] != null))
      {
        megaman.setY(BLOCK_SIZE * (u+1));
        megaman.setVelocityY(0);
      }
    }
    else if (up == -1)
    {

    }

    // Moving left and right
    if (inputDirection == 1)
    {
      megaman.setDirection(Direction.RIGHT);
      megaman.setVelocityX(RUN_VELOCITY);
    }
    else if (inputDirection == -1)
    {
      megaman.setDirection(Direction.LEFT);
      megaman.setVelocityX(-RUN_VELOCITY);
    }
    else
      megaman.setVelocityX(0);

    // normal force check
    if (inGrid(d, l) && inGrid(d, r) && megaman.getY() + megaman.getHeight() >= BLOCK_SIZE * d &&
        ((grid[screen][d][l] != null) ||
        (grid[screen][d][r] != null)))
    {
      F_N = true;
      a_y -= GRAVITY;
    }

    if (F_N)
    {
      megaman.setY(d * BLOCK_SIZE - megaman.getHeight());
      megaman.setVelocityY(0);

      if (inputJump)
      {
        jumping = true;

        megaman.setVelocityY(-JUMP_VELOCITY);

        mState = Megaman.STATE_FALL;
      }
      else if (megaman.getState() == Megaman.STATE_FALL)
      {
        mState = Megaman.STATE_IDLE;
      }
      else
      {
        if (inputDirection == 0)
        {
          mState = Megaman.STATE_IDLE;
          if (right != 0)
          {
            if (right == 1)
            {
              if (megaman.getVelocityX() < 0.01f)
                megaman.setVelocityX(0);
              else
                a_x -= FRICTION;
            }
            else if (right == -1)
            {
              if (megaman.getVelocityX() > -0.01f)
                megaman.setVelocityX(0);
              else
                a_x += FRICTION;
            }
          }
        }
        else
        {
          if (megaman.getState() == Megaman.STATE_IDLE && (megaman.getState() != Megaman.STATE_STEP && megaman.getState() != Megaman.STATE_RUN))
            mState = Megaman.STATE_RUN;
        }
      }
    }
    else
    {
      if (megaman.getState() != Megaman.STATE_FALL)
        mState = Megaman.STATE_FALL;
      if (jumping && !inputJump)
      {
        jumping = false;
        if (megaman.getVelocityY() < -JUMP_THRESHOLD)
          megaman.setVelocityY(-JUMP_THRESHOLD);
      }
    }

    // Bullets
    if (inputClick)
    {
      inputClick = false;
      if (bullets.size() < 3)
      {


        if (mouseX > megaman.getX())
          megaman.setDirection(Direction.RIGHT);
        else if (mouseX < megaman.getX())
          megaman.setDirection(Direction.LEFT);


        float x = megaman.getDirection() == Direction.RIGHT ? megaman.getX() + megaman.getWidth() : megaman.getX();

        double angle = Math.atan2(mouseY - megaman.getY() - 28, mouseX - x);
        bullets.add(new Bullet(x, megaman.getY() + 28,
            BULLET_VELOCITY * (float)Math.cos(angle),
            BULLET_VELOCITY * (float)Math.sin(angle),
            megaman.getDirection()));

        mState = Megaman.STATE_SHOOT;
      }
    }

    // Apply forces and state change
    megaman.setVelocityX(megaman.getVelocityX() + a_x * elapsedTime);
    megaman.setVelocityY(megaman.getVelocityY() + a_y * elapsedTime);

    if (megaman.getState() == Megaman.STATE_HIT)
    {
      if (megaman.getAnimation().isDone())
        megaman.setState(Megaman.STATE_IDLE);
    }
    else if (megaman.getState() == Megaman.STATE_SHOOT)
    {
      if (megaman.getAnimation().isDone())
        megaman.setState(Megaman.STATE_IDLE);
    }
    else if (megaman.getState() == Megaman.STATE_STEP && megaman.getAnimation().isDone())
      megaman.setState(Megaman.STATE_RUN);
    else if (mState != -1)
      megaman.setState(mState);

    // Call sprite updates
    megaman.update(elapsedTime);

    if (health != null)
    {
      health.update(elapsedTime);
      health.setVelocityY(health.getVelocityY() + GRAVITY/2 * elapsedTime);

      if (hit(health, megaman))
      {
        if (megaman.getHealth() < 10)
          megaman.setHealth(megaman.getHealth() + 1);
        health = null;
      }
      else if (health.getY() > dim.height)
        health = null;
    }

    for (int i = 0; i < bullets.size(); i++)
    {
      Bullet b = bullets.get(i);
      b.update(elapsedTime);

      if (b.getX() < 0 || b.getX() + b.getWidth() > dim.width ||
          b.getY() < 0 || b.getY() + b.getHeight() > dim.height)
        bullets.remove(i);
    }

    for (int i = 0; i < enemies.size(); i++)
    {
      Enemy e = enemies.get(i);
      e.update(elapsedTime);
      if (e.getState() == Enemy.STATE_EXPLODING)
      {
        if (e.getAnimation().isDone())
          enemies.remove(i);
      }
      else
      {
        // check off screen, then player collision, then bullet collision
        if (e.getX() < -100 || e.getX() + e.getWidth() > dim.width + 100 ||
          e.getY() < -100 || e.getY() + e.getHeight() > dim.height + 100)
          enemies.remove(i);
        else
        {
          for (int j = 0; j < bullets.size(); j++)
          {
            Bullet b = bullets.get(j);
            if (hit(b, e))
            {
              if (health == null && Math.random() * 15 < 1)
                health = new Health(e.getX(), e.getY());

              e.setState(Enemy.STATE_EXPLODING);
              e.setVelocityX(0);
              e.setVelocityY(-0.02f);
              bullets.remove(j);

              score += getScore(e);
            }
          }

          if (e.getState() != Enemy.STATE_EXPLODING && hit(e, megaman))
          {
            megaman.setHealth(megaman.getHealth() - 1);
            megaman.setState(Megaman.STATE_HIT);
            e.setState(Enemy.STATE_EXPLODING);
            e.setVelocityX(0);
            e.setVelocityY(-0.02f);

            if (megaman.getHealth() <= 0)
            {
              gameOver = true;
              midiPlayer.stop();
            }
          }
        }
      }
    }

    // bounds cheating
    if (megaman.getY() < BLOCK_SIZE)
      megaman.setY(BLOCK_SIZE);
    if (megaman.getX() < BLOCK_SIZE)
      megaman.setX(BLOCK_SIZE);
    else if (megaman.getX() + megaman.getWidth() > 15*BLOCK_SIZE)
      megaman.setX(15*BLOCK_SIZE - megaman.getWidth());

    // enemy spawning
    birdTimer += elapsedTime;
    metroidTimer += elapsedTime;
    springTimer += elapsedTime;

    if (birdTimer >= birdSpawn)
    {
      birdTimer = 0;
      int randY = (int)((Math.random()*megaman.getHeight() + 30) + megaman.getY() - megaman.getHeight()/2);
      Direction dir = Math.random() < 0.5 ? Direction.RIGHT : Direction.LEFT;
      int type = (int)(Math.random() * 3);
      if (type == 0 && Math.random() > 0.2)
        type = 2;
      enemies.add(new Bird(randY, dir, type, state.getDifficulty()));

      int delay = 500 - wave*100;
      birdSpawn = (int)(rand.nextGaussian() * 500 + delay) + 1500;
    }

    if (springTimer >= springSpawn)
    {
      springTimer = 0;

      Direction dir = Math.random() < 0.5 ? Direction.RIGHT : Direction.LEFT;
      int type;
      if (Math.random() > 0.8)
        type = 1;
      else
        type = 0;

      int delay = 350 - wave*70;

      int goTime = (int)(Math.random()*400 + delay);
      enemies.add(new Spring(14*BLOCK_SIZE - 22, dir, type, state.getDifficulty(), goTime));

      springSpawn = (int)(rand.nextGaussian() * 350 + delay) + 2000;
    }

    if (metroidTimer >= metroidSpawn)
    {
      metroidTimer = 0;

      float xAxis = (float)Math.random() * (dim.width - 100) + 50;
      float period = (float)Math.random() + 1.5f;
      float a = (float)Math.random() * 50 + 10;
      float vx = (float)Math.random() * 0.1f + 0.3f;
      enemies.add(new Metroid(xAxis, period, a, state.getDifficulty(), vx));

      int delay = 300 - wave*70;
      metroidSpawn = (int)(rand.nextGaussian() * 300 + delay) + 3000;
    }
  }

  private boolean inGrid(int row, int col)
  {
    return (row >= 0 && row < grid[screen].length &&
        col >= 0 && col < grid[screen][row].length);
  }

  private int getRow(float y)
  {
    return (int)(y / BLOCK_SIZE);
  }

  private int getCol(float x)
  {
    return (int)(x / BLOCK_SIZE);
  }

  private boolean hit(Sprite s1, Sprite s2)
  {
    return (s1.getX() < s2.getX() + s2.getWidth() && s1.getX() + s1.getWidth() > s2.getX() &&
        s1.getY() + s1.getHeight() > s2.getY() && s1.getY() < s2.getY() + s2.getHeight());
  }

  private int getScore(Enemy e)
  {
    if (e instanceof Bird)
    {
      Bird b = (Bird)e;
      if (b.getType() == 0)
        return 100;
      else if (b.getType() == 1)
        return 20;
      else
        return 10;
    }
    else if (e instanceof Metroid)
    {
      return 70;
    }
    else if (e instanceof Spring)
    {
      Spring s = (Spring)e;
      if (s.getType() == 0)
        return 50;
      else
        return 30;
    }

    return 0;
  }
}