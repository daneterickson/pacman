package ooga.view.topView;

import static ooga.view.loginView.LoginView.SMALL_TEXT;
import static ooga.view.startupView.GameStartupPanel.RESOURCES_PATH;
import static ooga.view.bottomView.BottomView.ICON_SIZE;
import static ooga.view.center.BoardView.BOARD_HEIGHT;
import static ooga.view.center.BoardView.BOARD_WIDTH;
import static ooga.view.mainView.MainView.SCENE_HEIGHT;
import static ooga.view.mainView.MainView.BG_COLOR;

import java.io.File;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.function.Consumer;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ooga.controller.Controller;
import ooga.model.GameEngine;

/**
 * Class that creates the top view of the main game view, including the title image, a life counter,
 * and a scoreboard.
 *
 * @author Kat Cottrell, Dane Erickson
 */
public class TopView {

  public static final String TOPVIEW_PACKAGE = "ooga.view.topView.";
  public static final String STYLESHEET = String.format("/%sTopView.css",
      TOPVIEW_PACKAGE.replace(".", "/"));
  public static final double TOP_SPACING = (SCENE_HEIGHT - BOARD_HEIGHT) / 3;
  public static final String PM307 = "data/images/pac_man_307_header.png";
  public static final int MAX_HEARTS = 5;

  private BorderPane topGrid;
  private HBox scoreDisplay;
  private HBox lifeDisplay;
  private Label scoreNumber;
  private GameEngine myGame;
  private Controller myController;
  private Consumer<Integer> scoreConsumer = score -> updateScoreDisplay(score);
  private Consumer<Integer> livesConsumer = lives -> updateLivesDisplay(lives);
  private ResourceBundle myResources;
  private VBox topFull;
  private int currScore;


  /**
   * Constructor to create TopView object in view
   *
   * @param game     is the model game
   * @param language is the selected language
   */
  @Deprecated
  public TopView(GameEngine game, String language) {
    myResources = ResourceBundle.getBundle(String.format("%s%s", RESOURCES_PATH, language));
    myGame = game;
    myGame.getBoard().addScoreConsumer(scoreConsumer);
    myGame.getBoard().addLivesConsumer(livesConsumer);
    initiateTopView();
    topGrid.getStyleClass().add("root");
    topGrid.getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());
  }

  /**
   * Constructor to create TopView object in view
   *
   * @param game     is the model game
   * @param language is the selected language
   */
  public TopView(GameEngine game, Controller controller, String language) {
    myResources = ResourceBundle.getBundle(String.format("%s%s", RESOURCES_PATH, language));
    myGame = game;
    myController = controller;
    myGame.getBoard().addScoreConsumer(scoreConsumer);
    myGame.getBoard().addLivesConsumer(livesConsumer);
    initiateTopView();
    topGrid.getStyleClass().add("root");
    topGrid.getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());
  }

  private VBox initiateTopView() {
    topGrid = new BorderPane();
    topGrid.setBackground(
        new Background(new BackgroundFill(BG_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
    topGrid.setMaxWidth(BOARD_WIDTH);
    makeLifeDisplay();
    topGrid.setLeft(lifeDisplay);
    makeScoreDisplay();
    topGrid.setRight(scoreDisplay);
    topFull = new VBox();
    ImageView pacMan307 = new ImageView(new Image(new File(PM307).toURI().toString()));
    pacMan307.setPreserveRatio(true);
    pacMan307.setFitWidth(BOARD_WIDTH);
    topFull.getChildren().addAll(pacMan307, topGrid);
    topFull.setAlignment(Pos.CENTER);
    return topFull;
  }

  private void makeScoreDisplay() {
    String scoreLabelPath = "data/images/scoreLabel-" + myResources.getString("score") + ".png";
    ImageView scoreLabelImg = new ImageView(new Image(new File(scoreLabelPath).toURI().toString()));
    scoreLabelImg.setPreserveRatio(true);
    scoreLabelImg.setFitHeight(ICON_SIZE);
    scoreNumber = new Label();
    scoreNumber.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, ICON_SIZE));
    scoreNumber.setTextFill(Color.LIGHTGRAY);
    scoreDisplay = new HBox();
    scoreDisplay.setBackground(
        new Background(new BackgroundFill(BG_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
    scoreDisplay.setAlignment(Pos.BOTTOM_CENTER);
    scoreDisplay.getChildren().addAll(scoreLabelImg, scoreNumber);
    updateScoreDisplay(0);
  }

  private void makeLifeDisplay() {
    lifeDisplay = new HBox();
    lifeDisplay.setAlignment(Pos.CENTER);
    lifeDisplay.setBackground(
        new Background(new BackgroundFill(BG_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
    myController.pauseOrResume();
    int startLives = myGame.getBoard().getGameState().getLives();
    updateLivesDisplay(startLives);
    lifeDisplay.getStyleClass().add("lives");
    lifeDisplay.getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());
  }

  private ImageView makeIcon(String path) {
    ImageView image = new ImageView(new Image(new File(path).toURI().toString()));
    image.setPreserveRatio(true);
    image.setFitHeight(ICON_SIZE);
    return image;
  }

  private void updateScoreDisplay(int score) {
    currScore = score;
    scoreNumber.setText(String.format(String.valueOf(score)));
  }

  private void updateLivesDisplay(int lives) {
    myController.pauseOrResume();
    lifeDisplay.getChildren().clear();
    ImageView pacImg = makeIcon("data/images/PAC.png");
    ImageView livesLabel = makeIcon(
        "data/images/livesLabel-" + myResources.getString("lives") + ".png");
    lifeDisplay.getChildren().addAll(pacImg, livesLabel);
    for (int i=0; i<lives; i++) {
      if (i>MAX_HEARTS) {
        Text t = new Text("+");
        t.setFont(Font.font("Verdana", FontWeight.BOLD, ICON_SIZE));
        t.setFill(Color.LIGHTGRAY);
        lifeDisplay.getChildren().add(t);
        break;
      }
      lifeDisplay.getChildren().add(makeIcon("data/images/heart.png"));
    }
  }

  /**
   * Getter method to get the VBox in the topView
   *
   * @return VBox topFull
   */
  public VBox getTopViewGP() {
    return this.topFull;
  }

  /**
   * Getter method to get the current score. Used in the MainView to display the score in the
   * win/loss popups
   *
   * @return int currScore
   */
  public int getCurrScore() {
    return currScore;
  }

  // Used for testing
  protected HBox getLifeDisplay() { return lifeDisplay; }
}
