package ooga.controller.IO;

import java.io.IOException;
import org.junit.jupiter.api.Test;

public class SaveGameTest {

  @Test
  void defaultSaveGame() throws IOException {
    SaveGame.saveGame();
  }

}
