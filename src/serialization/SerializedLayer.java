package serialization;

import com.google.gson.annotations.SerializedName;

public class SerializedLayer {
  public enum Type {
    @SerializedName("tilelayer")
    TILE_LAYER,
    @SerializedName("objectgroup")
    OBJECT_GROUP,
    @SerializedName("imagelayer")
    IMAGE_LAYER;
  }

  private int x;
  private int y;
  private int width;
  private int height;

  private String name;
  private SerializedLayer.Type type;
  private boolean visible;
  private double opacity;

  private int[] data;

  @Override
  public String toString() {
    return String.format("SerializedLayer: {name: %s, type: %s, w: %d, h: %d}", name, type, width, height);
  }
}
