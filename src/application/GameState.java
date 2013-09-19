package application;

import input.InputListener;

import java.awt.Dimension;

import javax.swing.JFrame;

import audio.MidiPlayer;
import audio.SoundManager;

public class GameState
{
	public static final int NUM_BOSSES = 9;
	public static final int BUBBLEMAN = 0, AIRMAN = 1, QUICKMAN = 2, HEATMAN = 3,
			DR_WILY = 4, WOODMAN = 5, METALMAN = 6, FLASHMAN = 7, CRASHMAN = 8;
	
	public static final int NUM_DIFFICULTIES = 2;
	public static final int NORMAL = 0, DIFFICULT = 1;
	
	private JFrame parent;
	private Dimension screenDim, dim;
	private InputListener inputListener;
	private SoundManager soundManager;
	private MidiPlayer midiPlayer;
	
	private boolean[] bosses;
	private int difficulty;
	private int energyTanks;
	
	public GameState(JFrame parent, Dimension screenDim, Dimension dim, InputListener inputListener, 
						SoundManager soundManager, MidiPlayer midiPlayer)
	{
		this.screenDim = screenDim;
		this.dim = dim;
		
		this.inputListener = inputListener;
		this.soundManager = soundManager;
		this.midiPlayer = midiPlayer;
		
		bosses = new boolean[NUM_BOSSES];
		difficulty = NORMAL; // default
		energyTanks = 0;
	}
	
	public JFrame getParent()
	{
		return parent;
	}
	
	public Dimension getScreenDimension()
	{
		return screenDim;
	}
	
	public Dimension getDimension()
	{
		return dim;
	}
	
	public InputListener getInputListener()
	{
		return inputListener;
	}
	
	public SoundManager getSoundManager()
	{
		return soundManager;
	}
	
	public MidiPlayer getMidiPlayer()
	{
		return midiPlayer;
	}
	
	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}
	
	public int getDifficulty()
	{
		return difficulty;
	}
	
	public void setBeaten(int boss, boolean beaten)
	{
		bosses[boss] = beaten;
	}
	
	public boolean isBeaten(int boss)
	{
		return bosses[boss];
	}
	
	public void setEnergyTanks(int energyTanks)
	{
		this.energyTanks = energyTanks;
	}
	
	public int energyTanks()
	{
		return energyTanks;
	}
}