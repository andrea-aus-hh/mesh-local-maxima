# Compiling

To compile the code yourself, run
```
./gradlew shadowJar
```

Or on Windows:

```
gradlew.bat shadowJar
```

This should produce a JAR file named `meshWalk.jar` in the project home directory.

Alternatively, it is possible to download and run a JAR file from the GitHub releases: to each push to master should correspond a release.

# Running

After compiling or downloading the jar, it can be run like this:

```
java -jar meshWalk.jar <json-file-path> <number-of-maxima>
```

It will output the list of maxima in the requested JSON format, in descending order of absolute height.

In case there are less maxima than requested, a message will be displayed in the stderr, and all the available maxima will be displayed as output.

# Unit test

A single automatic test is included, which relies on `mesh_simple.json`. It does more or less what the Main function does, and checks that the output strings looks exactly like it should.