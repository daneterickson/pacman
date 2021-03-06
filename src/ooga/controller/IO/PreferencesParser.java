package ooga.controller.IO;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import ooga.controller.IO.utils.JSONObjectParser;
// Decided to use this library after reading article from
// https://coderolls.com/parse-json-in-java/
import org.json.JSONObject;

/**
 * Purpose: This class's purpose is to do high level parsing of a preferences file that is first
 * converted into a JSONObject Dependencies: java-json, JSONObjectParser, Set, ResourceBundle, Map,
 * List, InputMismatchException, HashMap, ArrayList, BigDecimal, Method, InvocationTargetException,
 * IOException, File Example: Instantiate this class in Controller to parse a preferences file, and
 * use the getters in this class to instantiate a UserPreferences record to send to the frontend as
 * well as to send along the startingConfig to JsonParser to make the model
 *
 * @author Evan Kenyon
 */
public class PreferencesParser {

  private static final String DEFAULT_RESOURCE_PACKAGE =
      PreferencesParser.class.getPackageName() + ".resources.";
  private static final String POSSIBLE_PREFERENCES_FILENAME = "PossiblePreferences";
  private static final String PREFERENCES_VALUES_FILENAME = "PreferencesValues";
  private static final String EXCEPTION_MESSAGES_FILENAME = "Exceptions";
  private static final String MAGIC_VALUES_FILENAME = "PreferencesParserMagicValues";

  private JSONObject preferencesJson;
  private Map<String, String> imagePaths;
  private Map<String, List<Double>> colors;
  private File startingConfig;
  private String style;

  private ResourceBundle possiblePreferences;
  private ResourceBundle preferencesValues;
  private ResourceBundle exceptionMessages;
  private ResourceBundle magicValues;

  /**
   * Purpose: Instantiate a PreferencesParser by instantiating its data structures and resource
   * bundles
   */
  public PreferencesParser() {
    possiblePreferences = ResourceBundle.getBundle(
        String.format("%s%s", DEFAULT_RESOURCE_PACKAGE, POSSIBLE_PREFERENCES_FILENAME));
    preferencesValues = ResourceBundle.getBundle(
        String.format("%s%s", DEFAULT_RESOURCE_PACKAGE, PREFERENCES_VALUES_FILENAME));
    exceptionMessages = ResourceBundle.getBundle(
        String.format("%s%s", DEFAULT_RESOURCE_PACKAGE, EXCEPTION_MESSAGES_FILENAME));
    magicValues = ResourceBundle.getBundle(
        String.format("%s%s", DEFAULT_RESOURCE_PACKAGE, MAGIC_VALUES_FILENAME));
    imagePaths = new HashMap<>();
    colors = new HashMap<>();
  }

  /**
   * Purpose: Parse a JSONObject representing a preferences file into image paths, colors, a style,
   * and a startingConfig file
   *
   * @param json a prefernces file already converted into a JSONObject
   * @throws NoSuchMethodException  should not ever be thrown since method names are correct in
   *                                properties file that is used in this method
   * @throws InputMismatchException thrown if keys or values are incorrect (ex. invalid image path),
   *                                with specific error message depending on issue
   */
  public void parseJSON(JSONObject json)
      throws NoSuchMethodException, InputMismatchException {
    preferencesJson = json;
    checkForExtraKeys(preferencesJson.keySet());
    for (String key : preferencesJson.keySet()) {
      Method method = this.getClass()
          .getDeclaredMethod(String.format(magicValues.getString("addToMethod"),
              possiblePreferences.getString(key)), String.class);
      method.setAccessible(true);
      try {
        method.invoke(this, key);
      } catch (InvocationTargetException | IllegalAccessException e) {
        throw (InputMismatchException) e.getCause();
      }
    }
  }

  @Deprecated
  public void uploadFile(File file)
      throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InputMismatchException {
    preferencesJson = JSONObjectParser.parseJSONObject(file);
    checkForExtraKeys(preferencesJson.keySet());
    for (String key : preferencesJson.keySet()) {
      Method method = this.getClass()
          .getDeclaredMethod(String.format(magicValues.getString("addToMethod"),
              possiblePreferences.getString(key)), String.class);
      method.setAccessible(true);
      try {
        method.invoke(this, key);
      } catch (InvocationTargetException e) {
        throw (InputMismatchException) e.getCause();
      }

    }
  }

  /**
   * Purpose: Get the image paths that were parsed by parseJSON method call Assumptions: parseJSON
   * method is called first
   *
   * @return the image paths that were parsed by parseJSON method call
   */
  public Map<String, String> getImagePaths() {
    return imagePaths;
  }

  /**
   * Purpose: Get the colors that were parsed by parseJSON method call Assumptions: parseJSON method
   * is called first
   *
   * @return the colors that were parsed by parseJSON method call
   */
  public Map<String, List<Double>> getColors() {
    return colors;
  }

  /**
   * Purpose: Get the style that was parsed by parseJSON method call Assumptions: parseJSON method
   * is called first
   *
   * @return the style that was parsed by parseJSON method call
   */
  public String getStyle() {
    return style;
  }

  /**
   * Purpose: Get the starting config as a File that was parsed by parseJSON method call
   * Assumptions: parseJSON method is called first
   *
   * @return the starting config as a File that was parsed by parseJSON method call
   */
  public File getStartingConfig() {
    return startingConfig;
  }

  private void checkForExtraKeys(Set<String> keySet) throws InputMismatchException {
    for (String key : keySet) {
      if (!possiblePreferences.containsKey(key)) {
        throw new InputMismatchException(
            String.format(exceptionMessages.getString("NotEnoughKeys"), key));
      }
    }
  }

  private void addToStartingConfig(String key) {
    startingConfig = new File(preferencesJson.getString(key));
  }

  private void addToImage(String key) {
    if (!List.of(preferencesValues.getString("Image").split(magicValues.getString("Delimiter")))
        .contains(preferencesJson.getString(key))) {
      throw new InputMismatchException(
          String.format(exceptionMessages.getString("InvalidImagePath"), preferencesJson.get(key),
              key));
    }
    imagePaths.put(key, preferencesJson.getString(key));
  }

  private void addToColor(String key) {
    List<Double> rgb = new ArrayList<>();
    // Borrowed code for converting BigDecimal to double from
    // https://stackoverflow.com/questions/19650917/how-to-convert-bigdecimal-to-double-in-java
    preferencesJson.getJSONArray(key).iterator()
        .forEachRemaining(color -> rgb.add(((BigDecimal) color).doubleValue()));
    for (Double color : rgb) {
      if (color > 1 || color < 0) {
        throw new InputMismatchException(
            String.format(exceptionMessages.getString("InvalidRGB"), color));
      }
    }
    colors.put(key, rgb);
  }

  private void addToStyle(String key) {
    if (!List.of(preferencesValues.getString("Style").split(","))
        .contains(preferencesJson.getString(key))) {
      throw new InputMismatchException(
          String.format(exceptionMessages.getString("InvalidStyle"), style));
    }
    style = preferencesJson.getString(key);
  }

}
