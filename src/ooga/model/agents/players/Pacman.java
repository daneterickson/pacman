package ooga.model.agents.players;

import ooga.model.interfaces.AbstractAgent;
import ooga.model.interfaces.Controllable;
import ooga.model.util.Position;

public class Pacman extends AbstractAgent implements Controllable {

  private final static int DEAD_STATE = 0;
  private final static int ALIVE_STATE = 1;
  private final static int SUPER_STATE = 2;

  private String currentDirection;
  private int myState;
  private Position myPosition;

  public Pacman(int x, int y) {
    super(x, y, "PACMAN");
    myState = ALIVE_STATE;
  }

  public void step() {
    Position oldPosition = myPosition;
  }

  @Override
  public void setDirection(String direction) {
    currentDirection = direction;
  }
}
