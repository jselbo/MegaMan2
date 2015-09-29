package util;

import graphics.objects.bosses.BossType;
import serialization.SerializedTilemap;

import com.google.gson.Gson;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility methods for reading and writing files.
 */
public class IOUtils {
  public static String readFileContents(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }

  public static SerializedTilemap loadLevel(BossType level) throws IOException {
    String levelName = level.getBossName().toLowerCase();
    String levelPath = "res/levels/" + levelName + "/overworld.json";
    String levelStr = IOUtils.readFileContents(levelPath, StandardCharsets.UTF_8);
    return new Gson().fromJson(levelStr, SerializedTilemap.class);
  }

  public static Image loadImage(String filePath) {
    return (new ImageIcon(filePath)).getImage();
  }

  public static Sequence loadMidi(String filePath) {
    File midiFile = new File(filePath);
    if (!midiFile.exists())
      return null;

    Sequence midi = null;
    try {
      midi = MidiSystem.getSequence(midiFile);
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return midi;
  }

  public static Clip loadClip(String filePath) {
    Clip clip = null;

    try {
      File clipFile = new File(filePath);
      if (!clipFile.exists())
        return null;

      AudioInputStream audioStream = AudioSystem.getAudioInputStream(clipFile);

      clip = AudioSystem.getClip();
      clip.open(audioStream);
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }

    return clip;
  }
}