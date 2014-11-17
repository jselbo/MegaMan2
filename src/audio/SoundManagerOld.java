package audio;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;

import javax.sound.sampled.Clip;

public class SoundManagerOld
{
  public final static int MIDI_INTRO = 0;

  // Audio output formatters
  private Sequencer sequencer;

  // Midis and Clips
  private Sequence[] midis;
  private Clip[] clips;
  private int currClip;

  private int volume = 127; // range 0 to 127
  private boolean muted;

  /**
   * Minimal initialization. Save most of it for initialize().
   */
  public SoundManagerOld()
  {
    currClip = 0;

    volume = 127; // default is maximum volume
    muted = false; // default is false
  }

  public void initialize()
  {
    try {
      sequencer = MidiSystem.getSequencer(false);
      sequencer.open();
      sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
      sequencer.getTransmitter().setReceiver(MidiSystem.getReceiver());
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  public void loadMidis(Sequence[] midis)
  {
    this.midis = midis;
  }

  public void setMidi(int key)
  {
    try {
      sequencer.setSequence(key == -1 ? null : midis[key]);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  public void playMidi()
  {
    if (sequencer.getSequence() == null)
      return;

    sequencer.start();

    try {
      Thread.sleep(5);
    } catch (InterruptedException e) {}

    forceVolumeSet();
  }

  public void stopMidi()
  {
    sequencer.stop();
  }

  public void setMidiRepeats(boolean repeats)
  {
    sequencer.setLoopCount(repeats ? Sequencer.LOOP_CONTINUOUSLY : 0);
  }

  public void loadClips(Clip[] clips)
  {
    this.clips = clips;
  }

  public void setClip(int key)
  {
    currClip = key;
  }

  public void playClip()
  {
    if (currClip != -1)
    {
      clips[currClip].stop();
      clips[currClip].setFramePosition(0);
      clips[currClip].start();
    }
  }

  public void stopClip()
  {
    if (currClip != -1)
      clips[currClip].stop();
  }

  private void forceVolumeSet()
  {
    try
    {
      int newVolume = muted ? 0 : volume;

      ShortMessage volumeMessage = new ShortMessage();
      for (int i = 0; i < 16; i++)
      {
        volumeMessage.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, newVolume);
        MidiSystem.getReceiver().send(volumeMessage, -1);
      }
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
  }

  public void setVolume(int volume)
  {
    if (this.volume != volume)
    {
      this.volume = volume;

      if (sequencer.isRunning())
        forceVolumeSet();
    }
  }

  public int getVolume()
  {
    return volume;
  }

  public void setMidiPosition(long microseconds)
  {
    sequencer.setMicrosecondPosition(microseconds);
  }

  public void setMidiEnd(long tick)
  {
    sequencer.setLoopEndPoint(tick);
  }

  public void setMuted(boolean muted)
  {
    if (this.muted != muted)
    {
      this.muted = muted;

      if (sequencer.isRunning())
        forceVolumeSet();
    }
  }

  public boolean isMuted()
  {
    return muted;
  }

  public boolean isRunning()
  {
    return sequencer.isRunning();
  }
}