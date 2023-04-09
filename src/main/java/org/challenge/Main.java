package org.challenge;

import static java.lang.Integer.parseInt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.challenge.model.Element;
import org.challenge.model.json.MeshJson;
import org.challenge.model.output.OutputLocalMaxima;

public class Main {
  public static void main(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException("This program expects exactly two arguments");
    }
    var filePath = Paths.get(args[0]);
    var requestedNumberOfMaxima = parseInt(args[1]);

    try (Reader reader = new InputStreamReader(new FileInputStream(filePath.toFile()))) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      var meshJsonData = gson.fromJson(reader, MeshJson.class);
      var mesh = meshJsonData.toExplorableMesh();
      ArrayList<Element> allLocalMaxima = mesh.findLocalMaxima(requestedNumberOfMaxima);
      System.out.println(
          gson.toJson(
              allLocalMaxima.stream()
                  .map(OutputLocalMaxima::from)
                  .sorted(Comparator.reverseOrder())
                  .collect(Collectors.toList())));
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
    }
  }
}
