package serialization;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SerializedTileset {
  private int firstgid;
  private int margin;
  private int spacing;

  private Map<String, String> properties;

  private String image;
  private String name;

  @SerializedName("imagewidth")
  private int imageWidth;
  @SerializedName("imageheight")
  private int imageHeight;

  @SerializedName("tilewidth")
  private int tileWidth;
  @SerializedName("tileheight")
  private int tileHeight;

  @Override
  public String toString() {
    return String.format("SerializedTileset: {name: %s, image: %s}", name, image);
  }
}
