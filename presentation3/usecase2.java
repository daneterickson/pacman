public class usecase2 {
  public static final String LANGUAGE = "English";
  public static final String VIEW_MODE = "Dark";

  public void start(Stage stage) {
  {
    Controller controller = new Controller(LANGUAGE, stage, VIEW_MODE);
    // Calls public Set<String> getFileNames() in FirebaseReader
    Set<String> fileNames = controller.getFirebaseFilenames();
    List<String> files = new ArrayList<>(fileNames);
    // Calls public JSONObject getFile(String fileName), then uses PreferencesParser and/or
    // JsonParser to parse the necessary information to generate a UserPreferences record
    UserPreferences userPreferences = controller.uploadFirebaseFile(files.get(0));
    BoardView boardView = new BoardView(controller.getVanillaGame(), controller, userPreferences);
    GameSaver saver = new GameSaver();
    saver.saveGame(Controller.getVanillaGame(), "test_user_file")
//    public BoardView(GameEngine game, Controller controller, UserPreferences userPreferences) {
  }
}