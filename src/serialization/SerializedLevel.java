package serialization;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Map;

public class SerializedLevel {
  private int version;

  @SerializedName("nextobjectid")
  private int nextObjectId;
  @SerializedName("renderorder")
  private String renderOrder;
  private Map<String, String> properties;

  private int width;
  private int height;

  @SerializedName("tilewidth")
  private int tileWidth;
  @SerializedName("tileheight")
  private int tileHeight;
  private String orientation;

  private SerializedLayer[] layers;
  private SerializedTileset[] tilesets;

  @Override
  public String toString() {
    return String.format("SerializedLevel: {w: %d, h: %d, layers: %s, tilsets: %s}", width, height,
      Arrays.toString(layers), Arrays.toString(tilesets));
  }
}