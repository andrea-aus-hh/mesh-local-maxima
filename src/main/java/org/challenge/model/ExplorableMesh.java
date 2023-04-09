package org.challenge.model;

import static org.challenge.model.ExplorationState.*;

import java.util.*;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

@Builder
@Value
public class ExplorableMesh {
  List<Element> elementsWithValue;

  Map<Integer, HashSet<Element>> nodesToElementsMap;

  private Set<Element> findNeighbours(Element element) {
    return element.nodes.stream()
        .map(nodesToElementsMap::get)
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
  }

  private boolean isLocalMaximum(Element requestedElement) {
    if (requestedElement.explorationState == LOCAL_MAXIMUM) return true;
    if (requestedElement.explorationState == NOT_LOCAL_MAXIMUM) return false;

    var elements =
        requestedElement.nodes.stream()
            .map(nodesToElementsMap::get)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());

    return elements.stream()
        .noneMatch(currentElement -> currentElement.height > requestedElement.height);
  }

  private @Nullable Element findHigherNeighbourOrRandomise(
      Element currentElement, Set<Element> neighbours) {
    Set<Element> unexploredNeighbours =
        neighbours.stream().filter(n -> !n.hasBeenExplored()).collect(Collectors.toSet());
    Optional<Element> optionalHigherNeighbour =
        unexploredNeighbours.stream()
            .filter(neighbour -> neighbour.height.compareTo(currentElement.height) > 0)
            .findFirst();
    return optionalHigherNeighbour.orElseGet(
        () -> unexploredNeighbours.stream().findFirst().orElseGet(this::getHighestUnexplored));
  }

  private @Nullable Element getHighestUnexplored() {
    return elementsWithValue.stream().filter(n -> !n.hasBeenExplored()).findFirst().orElse(null);
  }

  public ArrayList<Element> findLocalMaxima(int requiredAmountOfMaxima) {
    var currentElement = elementsWithValue.stream().findFirst().get();

    var listOfMaxima = new ArrayList<Element>(List.of());

    while (listOfMaxima.size() < requiredAmountOfMaxima) {
      var neighbours = findNeighbours(currentElement);
      if (isLocalMaximum(currentElement)) {
        listOfMaxima.add(currentElement);
        currentElement.explorationState = LOCAL_MAXIMUM;
        neighbours.forEach(it -> it.explorationState = NOT_LOCAL_MAXIMUM);
        currentElement = neighbours.stream().findFirst().get();
      } else {
        currentElement.explorationState = NOT_LOCAL_MAXIMUM;
        currentElement = findHigherNeighbourOrRandomise(currentElement, neighbours);
        if (currentElement == null) {
          System.err.println(
              "Requested "
                  + requiredAmountOfMaxima
                  + " local maxima, but the mesh only has "
                  + listOfMaxima.size());
          break;
        }
      }
    }
    return listOfMaxima;
  }
}
