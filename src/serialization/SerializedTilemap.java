package serialization;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Map;

public class SerializedTilemap {
  public enum RenderOrder {
    @SerializedName("right-down")
    RIGHT_DOWN;
  }

  public enum Orientation {
    @SerializedName("orthogonal")
    ORTHOGONAL;
  }

  private int version;

  @SerializedName("nextobjectid")
  private int nextObjectId;
  @SerializedName("renderorder")
  private SerializedTilemap.RenderOrder renderOrder;
  private SerializedTilemap.Orientation orientation;
  private Map<String, String> properties;

  private int width;
  private int height;

  @SerializedName("tilewidth")
  private int tileWidth;
  @SerializedName("tileheight")
  private int tileHeight;

  private SerializedLayer[] layers;
  private SerializedTileset[] tilesets;

  @Override
  public String toString() {
    return String.format("SerializedTilemap: {w: %d, h: %d, layers: %s, tilesets: %s}", width, height,
      Arrays.toString(layers), Arrays.toString(tilesets));
  }
}