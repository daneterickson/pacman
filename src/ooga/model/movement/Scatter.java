package ooga.model.movement;

import java.util.List;
import java.util.Random;
import ooga.model.GameState;
import ooga.model.interfaces.Movable;
import ooga.model.util.Position;

/**
 * Implements scattering movement for an agent.
 */
public class Scatter implements Movable {

  @Override
  public Position move(GameState state, Position currentPos) {
    Position pacPos = state.getPacman().getPosition();
    List<Position> availablePositions = null;
    if (pacPos.getCoords()[0] - currentPos.getCoords()[0] < 0) {
      availablePositions = List.of(
          new Position(currentPos.getCoords()[0] + 1, currentPos.getCoords()[1]),
          new Position(currentPos.getCoords()[0], currentPos.getCoords()[1] + 1));
    } else {
      availablePositions = List.of(
          new Position(currentPos.getCoords()[0] - 1, currentPos.getCoords()[1]),
          new Position(currentPos.getCoords()[0], currentPos.getCoords()[1] - 1));
    }
    Random rand = new Random();
    return availablePositions.get(rand.nextInt(availablePositions.size()));
  }

}
