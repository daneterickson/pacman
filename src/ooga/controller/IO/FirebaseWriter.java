package ooga.controller.IO;

import com.google.firebase.database.DatabaseReference;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;
import ooga.model.GameEngine;
import org.json.JSONObject;

/**
 * @author Dania Fernandez dependencies: JSONObjectBuilder Saves a JSONObject of the current game
 * configuration to Firebase
 */
public class FirebaseWriter {

  private JSONConfigObjectBuilder builder;
  private GameEngine myGameEngine;
  private Firebase firebase;
  private FirebaseResponse response;
  private JSONObject object;
  private String userObjectName;

  /**
   * Sets up firebase and response based on the ooga Firebase url
   *
   * @throws FirebaseException
   * @throws UnsupportedEncodingException
   */
  public FirebaseWriter(GameEngine vanillaGame, String userInput) throws FirebaseException, UnsupportedEncodingException {
    // Borrowed code for basic setup from
    // https://github.com/bane73/firebase4j
    String firebase_baseUrl = "https://ooga-57bdb-default-rtdb.firebaseio.com/";
    firebase = new Firebase(firebase_baseUrl);
    response = firebase.get();
    myGameEngine = vanillaGame;
    builder = new JSONConfigObjectBuilder(myGameEngine);
    object = builder.setConfig();
    userObjectName = userInput;
  }
  // Borrowed code for basic setup from
  // https://github.com/bane73/firebase4j
  //FileInputStream fis = new FileInputStream("./data/ooga-57bdb-firebase-adminsdk-5d0am-78aa9ce61c.json");

  //FirebaseOptions options = FirebaseOptions.builder()
  // .setCredentials(GoogleCredentials.fromStream(fis))
  // .setDatabaseUrl("https://ooga-57bdb-default-rtdb.firebaseio.com/")
  // .build();

  //try {
  // FirebaseApp.initializeApp(options);
  //db = FirebaseDatabase.getInstance();
  //} catch (IllegalStateException e) {
  // db = FirebaseDatabase.getInstance();
  //}
  //configRef = db.getReference(); //no params
  //System.out.println(String.valueOf(configRef));
  //}

  /**
   * Saves the JSONObject of the current game configuration to Firebase
   * @throws JacksonUtilityException
   * @throws FirebaseException
   * @throws UnsupportedEncodingException
   */
  public void saveObject()
      throws JacksonUtilityException, FirebaseException, UnsupportedEncodingException {
    builder = new JSONConfigObjectBuilder(myGameEngine);
    object = builder.setConfig();
    Map<String, Object> objectMap = new HashMap<>();

    objectMap.put(userObjectName, object);
    firebase.put(userObjectName, object.toMap());

    //configRef.child("test0List").child("item1").setValueAsync(true);
    //configRef.push().setValue(object, (databaseError, databaseReference) -> {
    // if (databaseError != null) {
    //   System.out.println("SAVE FAILED :(");
    // }
    //});

  }

}
