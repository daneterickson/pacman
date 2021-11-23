package ooga.controller.IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import ooga.model.GameState;

public class SaveGame {

  private static int counter = 1;
  private static StringBuilder path = new StringBuilder();
  private static String openingBracket = "{\n";
  private static String colon = String.valueOf(':');
  private static String comma = ",";
  private static String titleLabel = "Title"; //must wrap all labels in double quotes
  private static String playerLabel = "Player";
  private static String requiredPelletsLabel = "RequiredPellets";
  private static String optionalPelletsLabel = "OptionalPellets";
  private static String powerUpsLabel = "PowerUps";
  private static String numberOfLives = "NumberOfLives";
  private static String opponentTypesLabel = "OpponentTypes";
  private static String difficultyLevelLabel = "Difficulty-Label";
  private static String wallMap = "WallMap";
  private static String closingBracket = String.valueOf('}');



  public static void saveGame(GameState game) throws IOException {

    clearBuilders();
    path.append("data/pac_man/user_file");
    path.append("_"+ String.valueOf(counter));
    path.append(".json");
    counter++;
    File jsonFile = new File(String.valueOf(path));

    try {
      FileWriter fileToSave = new FileWriter(jsonFile);
      fileToSave.write(String.valueOf(openingBracket));
      fileToSave.write(String.valueOf(closingBracket));


    } catch (IOException e) {
      System.out.println("SaveGame Exception");
    }


  }

  private static void clearBuilders() {
    path = new StringBuilder();
  }

}
