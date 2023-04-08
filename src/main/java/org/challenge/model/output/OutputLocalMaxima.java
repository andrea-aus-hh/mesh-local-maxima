package org.challenge.model.output;

import lombok.Builder;
import org.challenge.model.Element;

@Builder
public class OutputLocalMaxima implements Comparable<OutputLocalMaxima> {
  Integer element_id;
  double value;

  public static OutputLocalMaxima from(Element element) {
    return OutputLocalMaxima.builder().element_id(element.id).value(element.height).build();
  }

  @Override
  public int compareTo(OutputLocalMaxima secondOutput) {
    return Double.compare(value, secondOutput.value);
  }
}
