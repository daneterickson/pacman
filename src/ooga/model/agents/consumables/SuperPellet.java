package ooga.model.agents.consumables;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import ooga.model.agents.AbstractAgent;
import ooga.model.interfaces.Agent;
import ooga.model.interfaces.Consumable;
import ooga.model.movement.Static;
import ooga.model.util.Position;

/**
 * SuperPellet class for Pacman.
 */
public class SuperPellet extends AbstractAgent implements Consumable {

  private final static int PELLET_POINT = 10;

  private final static int EATEN_STATE = 0;
  private final static int UNEATEN_STATE = 1;

  private int myState;
  protected List<Consumer<Agent>> stateConsumers;

  /**
   * abstract constructor for super pellet
   *
   * @param x int x position
   * @param y int y position
   */
  public SuperPellet(int x, int y) {
    super(x, y);
    myState = UNEATEN_STATE;
    stateConsumers = new ArrayList<>();
    setStrategy(new Static());
  }

  @Override
  public int getState() {
    return myState;
  }

  @Override
  public void setCoords(Position newPosition) {
    setPosition(newPosition.getCoords());
  }

  @Override
  public void setState(int i) {
    myState = i;
    updateConsumer();
  }

  /**
   * Add superpellet consumers
   *
   * @param consumer
   */
  public void addConsumer(Consumer<Agent> consumer) {
    stateConsumers.add(consumer);
  }

  /**
   * update superpellet consumers
   */
  public void updateConsumer() {
    for (Consumer<Agent> consumer : stateConsumers) {
      consumer.accept(this);
    }
  }

  @Override
  public int getConsumed() {
    if (myState != EATEN_STATE) {
      myState = EATEN_STATE;
      getRunnable().run();
      updateConsumer();
      return PELLET_POINT;
    } else {
      return 0;
    }
  }
}
