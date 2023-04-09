package org.challenge.model;

import java.util.Set;
import lombok.Builder;

@Builder
public class Element {
  public int id;

  public Set<Integer> nodes;
  public Set<Integer> neighbours;
  public Double height;

  @Builder.Default public ExplorationState explorationState = ExplorationState.UNEXPLORED;

  public boolean hasNotBeenExplored() {
    return explorationState == ExplorationState.UNEXPLORED;
  }
}
