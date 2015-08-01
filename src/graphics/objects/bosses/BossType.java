package graphics.objects.bosses;

/**
 * All bosses in the game.
 */
public enum BossType {
  BUBBLEMAN("Bubbleman"),
  AIRMAN("Airman"),
  QUICKMAN("Quickman"),
  HEATMAN("Heatman"),
  DR_WILY("Dr. Wily"),
  WOODMAN("Woodman"),
  METALMAN("Metalman"),
  FLASHMAN("Flashman"),
  CRASHMAN("Crashman");

  private String bossName;

  BossType(String bossName) {
    this.bossName = bossName;
  }

  public String getBossName() {
    return bossName;
  }

  public static Boss createBoss(BossType type) {
    switch (type) {
      case BUBBLEMAN:
        return new Bubbleman();
      case AIRMAN:
        return new Airman();
      case QUICKMAN:
        return new Quickman();
      case HEATMAN:
        return new Heatman();
      case WOODMAN:
        return new Woodman();
      case METALMAN:
        return new Metalman();
      case FLASHMAN:
        return new Flashman();
      case CRASHMAN:
        return new Crashman();
      case DR_WILY:
        // TODO implement Dr. Wily boss
        throw new UnsupportedOperationException();
    }
    throw new IllegalStateException("Invalid BossType");
  }
}