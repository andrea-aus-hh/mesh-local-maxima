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
            .collect(Collectors.toList());

    var nodesWithElements =
        nodes.stream().collect(Collectors.toMap(it -> it.id, it -> new HashSet<Element>()));

    elementsWithValues.forEach(
        currentElement ->
            currentElement.nodes.forEach(node -> nodesWithElements.get(node).add(currentElement)));

    return ExplorableMesh.builder()
        .elementsWithValue(elementsWithValues)
        .nodesToElementsMap(nodesWithElements)
        .build();
  }
}
