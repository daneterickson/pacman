package ooga.view.userProfileView;

import static ooga.view.startupView.GameStartupPanel.EXAMPLES_PATH;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.controller.IO.User;
import ooga.view.popups.ErrorPopups;

/**
 * Purpose: Represents the frontend for user information, allowing the player to see the information
 * for the currently logged in profile and edit the editable information (ex. they cannot edit their
 * high score since that is determined by the games they play) Dependencies: File, IOException,
 * Method, ActionEvent, EventHandler, Scene, Button, ListView, TextInputDialog, Image, ImageView,
 * GridPane, Text, FileChooser, Stage, Controller, User, ErrorPopups Example: Instantiate this class
 * when a button is clicked with a label indicating that it will pop up some user information
 *
 * @author Evan Kenyon
 */
public class UserInformationView {

  private static final int SCREEN_WIDTH = 400;
  private static final int SCREEN_HEIGHT = 425;
  public static final String STARTUP_PACKAGE = "ooga.view.startupView.";
  public static final String DEFAULT_STYLESHEET = String.format("/%sGameStartupPanel.css",
      STARTUP_PACKAGE.replace(".", "/"));

  private Stage stage;
  private Controller controller;
  private String myLanguage;

  /**
   * Purpose: Instantiate this class with a controller, a stage, and a language Assumptions: the
   * controller has the desired user as its current user (ex. if there is an indication that
   * clicking a button will show the information for a user named Dane, then the controller's
   * current user should be that user)
   *
   * @param controller the controller with the desired user as its current user
   * @param stage      the stage on which to set a scene
   * @param language   the language for text
   */
  @Deprecated
  public UserInformationView(Controller controller, User user, Stage stage, String language) {
    this.stage = stage;
    this.controller = controller;
    myLanguage = language;
    reset(user);
  }

  /**
   * Purpose: Instantiate this class with a controller, a stage, and a language Assumptions: the
   * controller has the desired user as its current user (ex. if there is an indication that
   * clicking a button will show the information for a user named Dane, then the controller's
   * current user should be that user)
   *
   * @param controller the controller with the desired user as its current user
   * @param stage      the stage on which to set a scene
   * @param language   the language for text
   */
  public UserInformationView(Controller controller, Stage stage, String language) {
    this.stage = stage;
    this.controller = controller;
    myLanguage = language;
    reset(controller.getUser());
  }

  private Scene createStartupScene(User user) {
    GridPane root = new GridPane();
    root.getStyleClass().add("grid-pane");
    addProfileImage(root, user);
    Button editImageButton = makeButton("Edit Image", e -> editFile("Image"));
    root.add(editImageButton, 2, 1);
    addTextInfo(root, "Username", user.username(), 1, 2);
    Button editUsernameButton = makeButton("Edit Username",
        e -> editForm("Username", "Please enter a new username"));
    root.add(editUsernameButton, 2, 2);
    Button editPasswordButton = makeButton("Edit Password",
        e -> editForm("Password", "Please enter a new password"));
    root.add(editPasswordButton, 2, 3);
    addTextInfo(root, "High Score", String.valueOf(user.highScore()), 1, 3);
    addTextInfo(root, "Number of wins", String.valueOf(user.wins()), 1, 4);
    addTextInfo(root, "Number of losses", String.valueOf(user.losses()), 1, 5);
    root.add(new Text("Favorite files: "), 1, 6);
    ListView<String> favoriteFiles = new ListView<>();
    if (user.favorites() != null) {
      for (String file : user.favorites()) {
        System.out.println(file);
        favoriteFiles.getItems().add(file);
      }
    }
    root.add(favoriteFiles, 2, 6);
    Button addFavoriteFileButton = makeButton("Add favorite file", e -> editFile("Favorite"));
    root.add(addFavoriteFileButton, 1, 8);
    Button removeFavoriteFileButton = makeButton("Remove favorite file", e -> {
      try {
        controller.removeFile(favoriteFiles.getSelectionModel().getSelectedItem());
        reset(controller.getUser());
      } catch (IOException ex) {
        new ErrorPopups(ex, myLanguage, "RemoveFile");
      }
    });
    root.add(removeFavoriteFileButton, 2, 8);
    Scene myScene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets().add(getClass().getResource(DEFAULT_STYLESHEET).toExternalForm());
    return myScene;
  }

  private void reset(User user) {
    this.stage.setScene(createStartupScene(user));
    this.stage.setTitle("USER INFO");
    Image favicon = new Image(new File("data/images/pm_favicon.png").toURI().toString());
    this.stage.getIcons().add(favicon);
    this.stage.show();
  }

  private void addProfileImage(GridPane root, User user) {
    ImageView profileImage = new ImageView(
        new Image(new File(controller.getUser().imagePath()).toURI().toString()));
    setImgWidth(profileImage, 100);
    root.add(profileImage, 1, 1);
  }

  private void addTextInfo(GridPane root, String key, String value, int columnIndex, int rowIndex) {
    Text textInfo = new Text(String.format("%s: %s", key, value));
    textInfo.setId(key);
    root.add(textInfo, columnIndex, rowIndex);
  }

  private void setImgWidth(ImageView img, int width) {
    img.setPreserveRatio(true);
    img.setFitWidth(width);
  }

  private void editFile(String type) {
    FileChooser myFileChooser = new FileChooser();
    myFileChooser.setInitialDirectory(new File(EXAMPLES_PATH));
    try {
      controller.updateFile(myFileChooser.showOpenDialog(stage), type);
    } catch (Exception e) {
      new ErrorPopups(e, myLanguage, "EditProfile");
    }
    reset(controller.getUser());
  }

  private void editForm(String title, String header) {
    try {
      Method updateMethod = Controller.class.getDeclaredMethod(String.format("update%s", title),
          String.class);
      updateMethod.invoke(controller, makeTextInputDialog(title, header));
//      controller.updateString(makeTextInputDialog(title, header), title);
    } catch (Exception e) {
      new ErrorPopups(e, myLanguage, "EditProfile");
    }
    reset(controller.getUser());
  }

  private String makeTextInputDialog(String title, String header) {
    TextInputDialog textInput = new TextInputDialog();
    textInput.setTitle(title);
    textInput.setHeaderText(header);
    textInput.showAndWait();
    return textInput.getEditor().getText();
  }

  private Button makeButton(String label, EventHandler<ActionEvent> handler) {
    Button button = new Button();
    button.setOnAction(handler);
    button.setText(label);
    button.setId(label.replace(" ", ""));
    return button;
  }
}
