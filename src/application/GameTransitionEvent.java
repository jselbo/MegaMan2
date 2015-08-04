package application;

import com.google.common.collect.ImmutableMap;
import panels.GamePanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Event indicating a transition between game panels. Use this to pass data between GamePanels.
 */
public class GameTransitionEvent {
  private GamePanel.Type targetPanelType;
  private ImmutableMap<GameTransitionEventKey, Object> eventData;

  private GameTransitionEvent(GamePanel.Type targetPanelType, ImmutableMap<GameTransitionEventKey, Object> eventData) {
    this.targetPanelType = targetPanelType;
    this.eventData = eventData;
  }

  public static GameTransitionEvent emptyTransitionEvent(GamePanel.Type targetPanelType) {
    return new GameTransitionEvent(targetPanelType, ImmutableMap.of());
  }

  public GamePanel.Type getTargetPanelType() {
    return targetPanelType;
  }

  public Object getValue(GameTransitionEventKey key) {
    if (!eventData.containsKey(key)) {
      throw new TransitionEventValueNotSetException("Value not set for key: " + key.name());
    }
    return eventData.get(key);
  }

  public static class Builder {
    private GamePanel.Type targetPanelType;
    private Map<GameTransitionEventKey, Object> eventData;

    public Builder(GamePanel.Type targetPanelType) {
      this.targetPanelType = targetPanelType;
      eventData = new HashMap<GameTransitionEventKey, Object>();
    }

    public Builder putValue(GameTransitionEventKey key, Object value) {
      eventData.put(key, value);
      return this;
    }

    public GameTransitionEvent build() {
      return new GameTransitionEvent(targetPanelType, ImmutableMap.copyOf(eventData));
    }
  }
}