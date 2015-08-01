package application;

public class TransitionEventValueNotSetException extends RuntimeException {
  public TransitionEventValueNotSetException(String message) {
    super(message);
  }
}