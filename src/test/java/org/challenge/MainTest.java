package org.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.challenge.model.Element;
import org.challenge.model.ExplorableMesh;
import org.challenge.model.json.MeshJson;
import org.challenge.model.output.OutputLocalMaxima;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

  private Gson gson;

  @BeforeEach
  void setup() {
    gson = new GsonBuilder().setPrettyPrinting().create();
  }

  @Test
  void givenTheSimpleMesh_whenFindingMaxima_thenMaximaAreFound() throws IOException {
    // Given
    ExplorableMesh mesh;
    Reader reader = new InputStreamReader(MainTest.class.getResourceAsStream("/mesh_simple.json"));
    var meshJsonData = gson.fromJson(reader, MeshJson.class);
    mesh = meshJsonData.toExplorableMesh();

    // When
    ArrayList<Element> allLocalMaxima = mesh.findAllLocalMaxima(15);

    // Then
    var finalString =
        gson.toJson(
            allLocalMaxima.stream()
                .map(OutputLocalMaxima::from)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList()));
    assertEquals(
        """
                [
                  {
                    "element_id": 153,
                    "value": 5.99412916855438
                  },
                  {
                    "element_id": 141,
                    "value": 5.59673083328692
                  },
                  {
                    "element_id": 99,
                    "value": 3.7165791791790643
                  },
                  {
                    "element_id": 87,
                    "value": 3.6258426752667208
                  },
                  {
                    "element_id": 199,
                    "value": 2.047341538506613
                  },
                  {
                    "element_id": 185,
                    "value": 2.0353928618660895
                  },
                  {
                    "element_id": 33,
                    "value": 1.3083863128423896
                  },
                  {
                    "element_id": 21,
                    "value": 1.2142189437756745
                  },
                  {
                    "element_id": 18,
                    "value": -0.23535144863207666
                  }
                ]""",
        finalString);
  }
}
