package ooga.model.movement;

import ooga.model.GameState;
import ooga.model.interfaces.Movable;
import ooga.model.util.Position;

/**
 * Implements not moving for agents.
 */
public class Static implements Movable {

  @Override
  public Position move(GameState state, Position pos) {
    return pos;
  }
}
