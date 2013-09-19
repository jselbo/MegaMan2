package panels;

import graphics.Direction;
import graphics.objects.Introman;
import graphics.objects.Remix;
import graphics.objects.Selector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import javax.sound.midi.Sequence;

import application.GameState;
import application.Utility;
import audio.Sound;

public class IntroPanel extends GamePanel
{
	private static final long serialVersionUID = 1L;
	private static final long FADE_TIME = 600L; // total time (milliseconds) for a complete fade-in or fade-out
	private static final long FADE_PAUSE = 4000L; // time (milliseconds) during which the text pauses between fades
	private static final int FADE_INCREMENTS = 3; // 1 increment = no fade, more increments = higher quality
	
	// Trigger variables
	private long totalTime; // total elapsed milliseconds, cumulative (used to check for triggers)
	private long[] triggers; // list of trigger times, in seconds
	private int triggerCount; // 0-based
	private final int FPS = 40; // only used for building rise
	
	private int key1; // trigger count at which camera begins panning up the building
	private int key2; // trigger count at which camera stops panning and shows title
	
	// Text variables
	private String[][] textData;
	private Point[][] textPoints;
	private int opacity; // 0 to 255
	private int stringCounter;
	
	// Audio variables
	private Sequence midi;
	private Sound blip, teleport;
	
	// City/building and title screen variables
	private boolean showBackground;
	
	private Image megaman;
	
	private Image titleText, titleNumber; // title pictures
	private Remix remix;
	private Selector selector; // arrow selector; selection 0=normal, 1=difficult
	private Introman introMan; // Mega Man on roof
	private String[] difficulties;
	private String start;
	private Image city;
	private float cityY;
	private float boxY;
	
	private Image building, notchlessBuilding, roof;
	private boolean showNotchless; // paint notchless bottom building?
	private int notchlessPosition; // don't worry about it
	private float buildingY; // y-value of top of topmost building section, range (-2*buildingHeight+6, 0] until camera approaches roof
	private float cityBoxSpeed; // speed of city and box, pixels per second
	private final float MAX_CITY_BOX_SPEED = 120.0f; // final speed of city and box, pixels per second
	private float buildingSpeed; // speed of building, pixels per second
	private final float MAX_BUILDING_SPEED = 240.0f; // final speed of building, pixels per second
	private final int RISE_TIME = 12; // time for which the background pans, in seconds
	
	private boolean riseDone;
	
	private boolean acceptKeyEvents;
	private boolean teleportCued;
	
	private long roofTime; // total elapsed milliseconds spent on roof, cumulative (resets after repeat)
	private boolean repeat; // should the intro repeat?
	
	public IntroPanel(GameState state)
	{
		super(state);
		
		setBackground(Color.black);
		
		initializeTextData();
		
		midi = midiPlayer.getSequence("resources/audio/midis/intro.mid");
		
		blip = soundManager.getSound("resources/audio/sound_effects/blip1.wav");
		teleport = soundManager.getSound("resources/audio/sound_effects/teleport.wav");
		
		triggers = new long[600]; // approximate number of triggers
		Arrays.fill(triggers, -1L); // fill with default value of -1
		initializeTriggers();
		
		// Load in images
		String base = "resources/images/intro/";
		city = Utility.loadImage(base + "city.png");
		building = Utility.loadImage(base + "building_section.png");
		notchlessBuilding = Utility.loadImage(base + "notchless_building_section.png");
		roof = Utility.loadImage(base + "building_roof.png");
		titleText = Utility.loadImage(base + "title_text.png");
		titleNumber = Utility.loadImage(base + "title_number.png");
		
		megaman = Utility.loadImage(base + "megaman.png");
		
		remix = new Remix(25, 15);
		
		selector = new Selector(1, 2 * Utility.SPRITE_SIZE);
		selector.setPosition(6 * Utility.SPRITE_SIZE, 2 + 19 * Utility.SPRITE_SIZE);
		
		introMan = new Introman();
		introMan.setDirection(Direction.LEFT);
	}
	
	@Override
	public void start()
	{
		super.start();
		
		midiPlayer.stop();
		
		totalTime = 0;
		triggerCount = 0;
		
		stringCounter = 0;
		opacity = 0;
		
		selector.reset();
		
		introMan.setPosition(25 * Utility.SPRITE_SIZE, -building.getHeight(null));
		introMan.reset();
		
		cityY = -82.0f;
		buildingY = -building.getHeight(null);	// start at this instead of 0 so we don't trip the showNotchless boolean on key 2
		
		boxY = dim.height - 120;
		
		showBackground = false;
		riseDone = false;
		acceptKeyEvents = true;
		teleportCued = false;
		
		showNotchless = true;
		notchlessPosition = 3;
		
		repeat = true;
		roofTime = 0;
		
		cityBoxSpeed = 0;
		buildingSpeed = 0;
	}
	
	private void initializeTextData()
	{
		textData = new String[6][]; // change to 6
		textData[0] = new String[9];
		textData[0][0] = "\u00A91988 CAPCOM CO. LTD";
		textData[0][1] = "TM AND \u00A91989 CAPCOM U.S.A., INC.";
		textData[0][2] = "LICENSED BY";
		textData[0][3] = "NINTENDO OF AMERICA, INC.";
		textData[0][4] = "";
		textData[0][5] = "";
		textData[0][6] = "Programmed by Joshua Selbo";
		textData[0][7] = "Inspired by Mega Man";
		textData[0][8] = "Perfected by some Asian guy";
		textData[1] = new String[2];
		/*textData[1][0] = "IN THE YEAR OF 200X,";
		textData[1][1] = "A SUPER ROBOT NAMED MEGAMAN";
		textData[2] = new String[2];
		textData[2][0] = "WAS CREATED.";
		textData[2][1] = "DR. LIGHT CREATED MEGAMAN";
		textData[3] = new String[2];
		textData[3][0] = "TO STOP THE EVIL DESIRES";
		textData[3][1] = "OF DR. WILY.";
		textData[4] = new String[2];
		textData[4][0] = "HOWEVER, AFTER HIS DEFEAT,";
		textData[4][1] = "DR. WILY CREATED EIGHT";
		textData[5] = new String[2];
		textData[5][0] = "OF HIS OWN ROBOTS";
		textData[5][1] = "TO COUNTER MEGAMAN.";*/
		
		textData[1][0] = "IT IS THE YEAR 3043. YOU ARE";
		textData[1][1] = "A SUPER ROBOT NAMED MEGAMAN.";
		textData[2] = new String[2];
		textData[2][0] = "YOU AWAKE IN A STRANGE UNIVERSE,";
		textData[2][1] = "OF CRAZY PHYSICS AND ALIENS.";
		textData[3] = new String[2];
		textData[3][0] = "YOU MUST FIGHT ENEMIES,";
		textData[3][1] = "STRIKE DOWN EVIL WITH JUSTICE!";
		textData[4] = new String[2];
		textData[4][0] = "USE W, A AND D TO RUN AND JUMP";
		textData[4][1] = "AND CLICK TO SHOOT BULLETS.";
		textData[5] = new String[1];
		textData[5][0] = "GOOD LUCK MEGAMAN.";
		
		textPoints = new Point[textData.length][];
		for (int i = 0; i < textData.length; i++)
		{
			textPoints[i] = new Point[textData[i].length];
			for (int j = 0; j < textData[i].length; j++)
			{
				int x = Utility.xCenterText(textData[i][j], gameFont, dim);
				int y = (i == 0) ? (2 * dim.height / 5) + (30 * j) : dim.height - 70 + (30 * j);
				textPoints[i][j] = new Point(x, y);
			}
		}
		
		difficulties = new String[GameState.NUM_DIFFICULTIES];
		for (int i = 0; i < difficulties.length; i++)
		{
			switch (i)
			{
			case 0:
				difficulties[i] = "NORMAL";
				break;
			case 1:
				difficulties[i] = "DIFFICULT";
				break;
			}
		}
		
		start = "PRESS START";
	}
	
	private void initializeTriggers()
	{
		int tempCount = 0;
		
		// Text fading triggers
		long fadeTime = FADE_TIME / FADE_INCREMENTS;
		for (int i = 0; i < textData.length; i++)
		{
			int max = (i == 0) ? FADE_INCREMENTS : FADE_INCREMENTS * 2 - 1;
			for (int j = 0; j < max; j++)
			{
				if (tempCount == 0)
					triggers[tempCount] = fadeTime;
				else
					triggers[tempCount] = triggers[tempCount - 1] + fadeTime;
				
				tempCount++;
			}
			
			triggers[tempCount] = triggers[tempCount - 1] + FADE_PAUSE;
			tempCount++;
		}
		
		// Fade out for final text
		for (int i = 0; i < FADE_INCREMENTS; i++)
		{
			triggers[tempCount] = triggers[tempCount - 1] + fadeTime;
			tempCount++;
		}
		
		key1 = tempCount;
		
		// Camera panning triggers
		long panTime = 1000L / FPS;
		for (int i = 0; i < RISE_TIME * FPS; i++)
		{
			triggers[tempCount] = triggers[tempCount - 1] + panTime;
			tempCount++;
		}
		
		key2 = tempCount;
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
		
		if (riseDone)
		{
			int xB = (dim.width - building.getWidth(null)), yB = (int) buildingY;
			for (int i = 0; i < 2; i++)
			{
				g2.drawImage(building, xB, yB, null);
				yB += building.getHeight(null);
			}
			
			int xR = dim.width - roof.getWidth(null), yR = (int) buildingY
					- roof.getHeight(null);
			g2.drawImage(roof, xR, yR, null);
			
			int xTT = Utility.xCenterImage(titleText, dim), yTT = 80;
			g2.drawImage(titleText, xTT, yTT, null);
			
			int xTN = Utility.xCenterImage(titleNumber, dim), yTN = 210;
			g2.drawImage(titleNumber, xTN, yTN, null);
			
			introMan.paint(g2);
			
			g2.setColor(Color.white);
			g2.drawString(difficulties[0], 8 * Utility.SPRITE_SIZE, 20 * Utility.SPRITE_SIZE);
			g2.drawString(difficulties[1], 8 * Utility.SPRITE_SIZE, 22 * Utility.SPRITE_SIZE);
			g2.drawString(start, 6 * Utility.SPRITE_SIZE, 25 * Utility.SPRITE_SIZE);
			
			selector.paint(g2);
			
			remix.paint(g2);
		}
		else
		{
			if (showBackground)
			{
				int x = 0, y = (int) cityY;
				g2.drawImage(city, x, y, null);
				
				int xB = (dim.width - building.getWidth(null)), yB = (int) buildingY;
				for (int i = 0; yB < boxY; i++)
				{
					if (showNotchless && i == notchlessPosition)
						g2.drawImage(notchlessBuilding, xB, yB, null);
					else
						g2.drawImage(building, xB, yB, null);
					yB += building.getHeight(null);
				}
				
				int xR = dim.width - roof.getWidth(null), yR = (int) buildingY - roof.getHeight(null);
				g2.drawImage(roof, xR, yR, null);
				
				introMan.paint(g2);
				
				if (boxY < dim.height)
				{
					g2.setColor(Color.black);
					g2.fill(new Rectangle2D.Float(0.0f, boxY, dim.width, dim.height - boxY));
				}
			}
			
			if (stringCounter < textData.length)
			{
				g2.setColor(new Color(255, 255, 255, opacity));
				for (int i = 0; i < textData[stringCounter].length; i++)
					g2.drawString(textData[stringCounter][i],
							(int) textPoints[stringCounter][i].getX(),
							(int) textPoints[stringCounter][i].getY());
			}
			
			if (stringCounter == 0)
				g2.drawImage(megaman, Utility.xCenterImage(megaman, dim), 15, null);
		}
	}
	
	@Override
	public void updateGame(long elapsedTime)
	{
		totalTime += elapsedTime;
		
		introMan.update(elapsedTime);
		
		if (acceptKeyEvents)
			processInput();
		
		if (riseDone)
		{
			roofTime += elapsedTime;
			// no key input, must repeat
			if (introMan.getState() != Introman.STATE_TELEPORTING && roofTime >= 44000L)
			{
				acceptKeyEvents = false;
				repeat = true;
				introMan.setState(Introman.STATE_TELEPORTING);
			}
			
			if (acceptKeyEvents)
				selector.update(elapsedTime);
			
			remix.update(elapsedTime);
			
			if (!teleportCued && introMan.getState() == Introman.STATE_TELEPORTING && introMan.launched())
			{
				teleportCued = true;
				soundManager.play(teleport);
			}
			
			if (introMan.teleported())
			{
				try {
					Thread.sleep(1500); // simulate slow NES loading times muahahaha
				} catch (InterruptedException e) {}
				
				if (repeat)
					start();
				else
					setDone(true);
			}
		}
		else
		{
			while (totalTime >= triggers[triggerCount])
			{
				performAction(triggerCount);
				triggerCount++;
			}
		}
	}
	
	private void performAction(int key)
	{
		if (key < key1)
		{
			opacity = (FADE_INCREMENTS - Math.abs((key + 1) % (FADE_INCREMENTS * 2) - FADE_INCREMENTS)) * 255 / FADE_INCREMENTS;
			if ((key + 1) % (FADE_INCREMENTS * 2) == 0)
			{
				if (stringCounter == 0)
					midiPlayer.play(midi, false); // begin intro música
				stringCounter++;
			}
			
			if ((key + 1) == 2 * FADE_INCREMENTS + 1)
				showBackground = true;
		}
		else if (key < key2)
		{
			if (cityBoxSpeed < MAX_CITY_BOX_SPEED)
				cityBoxSpeed += 3.0f;
			if (buildingSpeed < MAX_BUILDING_SPEED)
				buildingSpeed += 3.0f;
			
			float cbSpeed = cityBoxSpeed / FPS;
			cityY += cbSpeed;
			boxY += cbSpeed;
			float bSpeed = buildingSpeed / FPS;
			buildingY += bSpeed;
			
			if (key < key2 - FPS * 2 && buildingY >= 0)
			{
				buildingY = -building.getHeight(null);
				if (showNotchless)
				{
					if (notchlessPosition == 3)
						notchlessPosition = 4;
					else
						showNotchless = false;
				}
			}
			
			if (buildingY >= dim.height - building.getHeight(null) - 21)
			{
				buildingY = dim.height - building.getHeight(null) - 21;
				riseDone = true;
			}
			
			introMan.setPosition(25 * Utility.SPRITE_SIZE, buildingY - roof.getHeight(null) + Utility.SPRITE_SIZE);
		}
	}
	
	private void processInput()
	{
		if (inputListener.hardKeyQuery(KeyEvent.VK_UP))
		{
			if (riseDone)
			{
				selector.decrement();
				soundManager.play(blip);
			}
		}
		if (inputListener.hardKeyQuery(KeyEvent.VK_DOWN))
		{
			if (riseDone)
			{
				selector.increment();
				soundManager.play(blip);
			}
		}
		if (inputListener.hardKeyQuery(KeyEvent.VK_ENTER))
		{
			if (riseDone)
			{
				repeat = false;
				acceptKeyEvents = false;
				introMan.setState(Introman.STATE_TELEPORTING);
				midiPlayer.stop();

				if (selector.getIndex() == 0)
					state.setDifficulty(GameState.NORMAL);
				else
					state.setDifficulty(GameState.DIFFICULT);
			}
			else
			{
				riseDone = true;
				
				midiPlayer.play(midi, false, 37950000L);
				
				buildingY = 315; // hard coded
				introMan.setPosition(25 * Utility.SPRITE_SIZE, buildingY - roof.getHeight(null) + Utility.SPRITE_SIZE);
			}
		}
	}
}