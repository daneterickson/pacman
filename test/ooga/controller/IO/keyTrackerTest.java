package ooga.controller.IO;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class keyTrackerTest {

  private keyTracker tracker;
  private KeyEvent left;
  private KeyEvent right;
  private KeyEvent up;
  private KeyEvent down;
  private KeyEvent nonArrow;

  @BeforeEach
  void setUp() {
    tracker = new keyTracker();
  }

  @Test
  void testForCorrectKey() {
    left = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "A",
        "left", KeyCode.A, false, false, false,
        false);
    right = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "D",
        "right", KeyCode.D, false, false, false,
        false);
    up = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "W",
        "up", KeyCode.W, false, false, false,
        false);
    down = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "S",
        "down", KeyCode.S, false, false, false,
        false);
    Assertions.assertEquals("left", tracker.getPressedKey(left));
    Assertions.assertEquals("right", tracker.getPressedKey(right));
    Assertions.assertEquals("up", tracker.getPressedKey(up));
    Assertions.assertEquals("down", tracker.getPressedKey(down));
  }

  @Test
  void testForNonArrowKey() {
    nonArrow = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "N",
        "N", KeyCode.N, false, false, false,
        false);
    Assertions.assertEquals("not-arrow", tracker.getPressedKey(nonArrow));
  }

}
