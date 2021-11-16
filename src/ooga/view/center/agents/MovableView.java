package ooga.view.center.agents;

    import ooga.model.interfaces.Agent;
    import ooga.model.util.Position;
    import ooga.view.center.agents.AgentView;

public abstract class MovableView extends AgentView {

  public static final String IMAGE_PATH = "images/";

  protected abstract void moveX(int x);

  protected abstract void moveY(int y);

  protected abstract void updateState(int state);

//  protected abstract void consume(PlayerView prey);

  @Override
  public void updateAgent(Agent agent) {
    int newX = agent.getPosition()[0];
    int newY = agent.getPosition()[1];
    int newState = agent.getState();
    moveX(newX);
    moveY(newY);
    updateState(newState);
  }

}
