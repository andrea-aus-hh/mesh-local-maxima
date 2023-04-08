package org.challenge.model;

import java.util.Set;
import lombok.Builder;
import org.apache.commons.collections.CollectionUtils;

@Builder
public class Element {
  public int id;

  public Set<Integer> nodes;
  public Set<Integer> neighbours;
  public Double height;

  @Builder.Default public ExplorationState explorationState = ExplorationState.UNEXPLORED;

  public boolean bordersWith(Element secondElement) {
    return CollectionUtils.intersection(nodes, secondElement.nodes).size() != 0;
  }

  public boolean hasBeenExplored() {
    return explorationState != ExplorationState.UNEXPLORED;
  }
}
