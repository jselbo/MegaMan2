package application;

import audio.MidiPlayer;
import audio.SoundManager;
import graphics.objects.bosses.BossType;
import input.InputListener;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

public class GameState {
  private JFrame parent;
  private Dimension screenDimension;
  private Dimension gameDimension;
  private InputListener inputListener;
  private SoundManager soundManager;
  private MidiPlayer midiPlayer;

  private Map<BossType, Boolean> bossClearFlags;
  private GameDifficulty difficulty;
  private int numEnergyTanks;

  public GameState(JFrame parent, Dimension screenDimension, Dimension gameDimension, InputListener inputListener,
      SoundManager soundManager, MidiPlayer midiPlayer) {
    this.screenDimension = screenDimension;
    this.gameDimension = gameDimension;

    this.inputListener = inputListener;
    this.soundManager = soundManager;
    this.midiPlayer = midiPlayer;

    bossClearFlags = new HashMap<BossType, Boolean>();
    for (BossType type : BossType.values()) {
      bossClearFlags.put(type, false);
    }
    difficulty = GameDifficulty.NORMAL;
    numEnergyTanks = 0;
  }

  public JFrame getParent() {
    return parent;
  }

  public Dimension getScreenDimension() {
    return screenDimension;
  }

  public Dimension getGameDimension() {
    return gameDimension;
  }

  public InputListener getInputListener() {
    return inputListener;
  }

  public SoundManager getSoundManager() {
    return soundManager;
  }

  public MidiPlayer getMidiPlayer() {
    return midiPlayer;
  }

  public void setDifficulty(GameDifficulty difficulty) {
    this.difficulty = difficulty;
  }

  public GameDifficulty getDifficulty() {
    return difficulty;
  }

  public void setBossCleared(BossType boss, boolean beaten) {
    bossClearFlags.put(boss, beaten);
  }

  public boolean isBossCleared(BossType boss) {
    return bossClearFlags.get(boss);
  }

  public void setEnergyTanks(int numEnergyTanks) {
    this.numEnergyTanks = numEnergyTanks;
  }

  public int getNumberEnergyTanks() {
    return numEnergyTanks;
  }
}