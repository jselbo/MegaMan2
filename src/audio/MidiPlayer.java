package audio;

import java.io.*;
import javax.sound.midi.*;

public class MidiPlayer implements MetaEventListener {

  // Midi meta event
  public static final int END_OF_TRACK_MESSAGE = 47;

  private Sequencer sequencer;
  private boolean loop;
  private boolean paused;
  private boolean muted;
  private boolean done;
  private int volume;

  /**
   * Creates a new MidiPlayer object.
   */
  public MidiPlayer() {
    try {
      sequencer = MidiSystem.getSequencer(false);
      sequencer.open();
      sequencer.addMetaEventListener(this);
      sequencer.getTransmitter().setReceiver(MidiSystem.getReceiver());
    }
    catch (MidiUnavailableException ex) {
      sequencer = null;
    }
  }


  /**
   * Loads a sequence from the file system. Returns null if
   * an error occurs.
   */
  public Sequence getSequence(String filename) {
    try {
      return getSequence(new FileInputStream(filename));
    }
    catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }


  /**
   * Loads a sequence from an input stream. Returns null if
   * an error occurs.
   */
  public Sequence getSequence(InputStream is) {
    try {
      if (!is.markSupported()) {
        is = new BufferedInputStream(is);
      }
      Sequence s = MidiSystem.getSequence(is);
      is.close();
      return s;
    }
    catch (InvalidMidiDataException ex) {
      ex.printStackTrace();
      return null;
    }
    catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public void play(Sequence sequence, boolean loop) {
    play(sequence, loop, 0);
  }

  /**
   * Plays a sequence, optionally looping. This method returns
   * immediately. The sequence is not played if it is invalid.
   */
  public void play(Sequence sequence, boolean loop, long microsecondPosition) {
    done = false;
    if (sequencer != null && sequence != null && sequencer.isOpen()) {
      try {
        sequencer.setSequence(sequence);
        sequencer.setMicrosecondPosition(microsecondPosition);
        sequencer.start();

        new Thread() {
          @Override
          public void run() {
          try {
        Thread.sleep(5);
      } catch (InterruptedException e) {}

          forceVolumeSet();
          }
        }.run();

        this.loop = loop;
      }
      catch (InvalidMidiDataException ex) {
        ex.printStackTrace();
      }
    }
  }


  /**
   * This method is called by the sound system when a meta
   * event occurs. In this case, when the end-of-track meta
   * event is received, the sequence is restarted if
   * looping is on.
   */
  public void meta(MetaMessage event) {
    if (event.getType() == END_OF_TRACK_MESSAGE) {
      if (sequencer != null && sequencer.isOpen() && loop) {
        sequencer.setMicrosecondPosition(0);
        sequencer.start();
      } else {
        done = true;
      }
    }
  }

  /**
   * Stops the sequencer and resets its position to 0.
   */
  public void stop() {
     if (sequencer != null && sequencer.isOpen()) {
       sequencer.stop();
       sequencer.setMicrosecondPosition(0);
     }
  }

  /**
   * Closes the sequencer.
   */
  public void close() {
     if (sequencer != null && sequencer.isOpen()) {
       sequencer.close();
     }
  }

  /**
   * Sets the paused state. Music may not immediately pause.
   */
  public void setPaused(boolean paused) {
    if (this.paused != paused && sequencer != null && sequencer.isOpen()) {
      this.paused = paused;
      if (paused) {
        sequencer.stop();
      }
      else {
        sequencer.start();
      }
    }
  }

  /**
   * Returns the paused state.
   */
  public boolean isPaused() {
    return paused;
  }

  public boolean done() {
    return done;
  }

  public void setPosition(long position) {
    if (sequencer != null && sequencer.isOpen())
    sequencer.setMicrosecondPosition(position);
  }

  public void setMuted(boolean muted) {
  if (this.muted != muted) {
    this.muted = muted;

    if (sequencer != null && sequencer.isOpen() && sequencer.isRunning())
    forceVolumeSet();
  }
  }

  public boolean isMuted() {
  return muted;
  }

  public void setVolume(int volume) {
  if (this.volume != volume) {
    this.volume = volume;

    if (sequencer != null && sequencer.isOpen() && sequencer.isRunning())
    forceVolumeSet();
  }
  }

  private void forceVolumeSet() {
  try {
    int newVolume = muted ? 0 : volume;

    ShortMessage volumeMessage = new ShortMessage();
    for (int i = 0; i < 16; i++) {
    volumeMessage.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, newVolume);
    MidiSystem.getReceiver().send(volumeMessage, -1);
    }
  } catch (InvalidMidiDataException e) {
    e.printStackTrace();
  } catch (MidiUnavailableException e) {
    e.printStackTrace();
  }
  }
}
