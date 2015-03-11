package engine;

public class GameEngine {
  private TileLayer backgroundLayer;
  private TileLayer foregroundLayer;

  public GameEngine() {
    backgroundLayer = new TileLayer();
    foregroundLayer = new TileLayer();
  }
}
