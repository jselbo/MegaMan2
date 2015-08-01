package serialization;

import com.google.gson.annotations.SerializedName;

public enum TileType {
  @SerializedName("0")
  BACKGROUND,
  @SerializedName("10")
  SOLID,
  @SerializedName("20")
  LADDER;
}