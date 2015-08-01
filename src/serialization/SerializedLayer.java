package serialization;

public class SerializedLayer {
  private int x;
  private int y;
  private int width;
  private int height;

  private String name;
  private String type;
  private boolean visible;
  private double opacity;

  private int[] data;

  @Override
  public String toString() {
    return String.format("SerializedLayer: {name: %s, w: %d, h: %d}", name, width, height);
  }
}
