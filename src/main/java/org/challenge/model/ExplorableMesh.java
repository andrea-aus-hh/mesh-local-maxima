package org.challenge.model;

import static org.challenge.model.ExplorationState.*;

import java.util.*;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ExplorableMesh {
  /** Elements with their attached height value, sorted in descending order. */
  List<Element> elementsWithValueSorted;

  /** Map each node to the set of elements it touches. */
  Map<Integer, HashSet<Element>> nodesToElementsMap;

  private Set<Element> findNeighbours(Element element) {
    return element.nodes.stream()
        .map(nodesToElementsMap::get)
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
  }

  private boolean isLocalMaximum(Element requestedElement, Set<Element> neighbours) {
    if (requestedElement.explorationState == LOCAL_MAXIMUM) return true;
    if (requestedElement.explorationState == NOT_LOCAL_MAXIMUM) return false;

    return neighbours.stream()
        .noneMatch(currentNeighbour -> currentNeighbour.height > requestedElement.height);
  }

  private Optional<Element> findAnyHigherUnexploredNeighbour(
      Element currentElement, Set<Element> neighbours) {
    Set<Element> unexploredNeighbours =
        neighbours.stream().filter(Element::hasNotBeenExplored).collect(Collectors.toSet());
    return unexploredNeighbours.stream()
        .filter(neighbour -> neighbour.height.compareTo(currentElement.height) > 0)
        .findFirst();
  }

  private Optional<Element> getHighestUnexploredElement() {
    return elementsWithValueSorted.stream().filter(Element::hasNotBeenExplored).findFirst();
  }

  public ArrayList<Element> findLocalMaxima(int requiredAmountOfMaxima) {
    var currentElement =
        elementsWithValueSorted.stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No elements are present in the mesh."));

    var listOfMaxima = new ArrayList<Element>(List.of());

    while (listOfMaxima.size() < requiredAmountOfMaxima) {
      var neighbours = findNeighbours(currentElement);
      Optional<Element> newElementOptional;
      if (isLocalMaximum(currentElement, neighbours)) {
        listOfMaxima.add(currentElement);
        currentElement.explorationState = LOCAL_MAXIMUM;
        neighbours.forEach(it -> it.explorationState = NOT_LOCAL_MAXIMUM);
        newElementOptional =
            neighbours.stream()
                .filter(Element::hasNotBeenExplored)
                .findFirst()
                .or(this::getHighestUnexploredElement);
      } else {
        currentElement.explorationState = NOT_LOCAL_MAXIMUM;
        newElementOptional =
            findAnyHigherUnexploredNeighbour(currentElement, neighbours)
                .or(this::getHighestUnexploredElement);
      }
      if (newElementOptional.isPresent()) {
        currentElement = newElementOptional.get();
      } else {
        System.err.println(
            "Requested "
                + requiredAmountOfMaxima
                + " local maxima, but the mesh only has "
                + listOfMaxima.size());
        break;
      }
    }
    return listOfMaxima;
  }
}
