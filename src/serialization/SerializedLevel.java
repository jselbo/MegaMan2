package serialization;

import com.google.gson.annotations.SerializedName;

public class SerializedLevel {
  private int version;

  private int width, height;

  @SerializedName("tilewidth")
  private int tileWidth;

  @Override
  public String toString() {
    return String.format("SerializedLevel: {w: %d, h: %d}", width, height);
  }
}