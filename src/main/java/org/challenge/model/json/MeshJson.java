package org.challenge.model.json;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.challenge.model.Element;
import org.challenge.model.ExplorableMesh;

public class MeshJson {
  public List<NodeJson> nodes;
  public List<ValueJson> values;
  public List<ElementJson> elements;

  public ExplorableMesh toExplorableMesh() {

    var nodesWithElements =
        nodes.stream().collect(Collectors.toMap(it -> it.id, it -> new HashSet<Element>()));

    var elementsWithValues =
        elements.stream()
            .map(
                currentElement ->
                    Element.builder()
                        .id(currentElement.id)
                        .height(
                            values.stream()
                                .filter(v -> v.element_id == currentElement.id)
                                .findFirst()
                                .get()
                                .value)
                        .nodes(new HashSet<>(currentElement.nodes))
                        .build())
            .peek(
                currentElement ->
                    currentElement.nodes.forEach(
                        node -> nodesWithElements.get(node).add(currentElement)))
            .sorted((e1, e2) -> Double.compare(e2.height, e1.height))
            .collect(Collectors.toList());

    return ExplorableMesh.builder()
        .elementsWithValueSorted(elementsWithValues)
        .nodesToElementsMap(nodesWithElements)
        .build();
  }
}
