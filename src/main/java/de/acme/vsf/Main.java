package de.acme.vsf;

import de.acme.vsf.deserializer.JsonDeserializer;
import de.acme.vsf.writer.ViewPointsStringWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Main class that executes the view point finder.
 */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    /**
     * Starter methods which expects 2 arguments. One path to the mesh file and the number of the view points.
     *
     * @param args inbound arguments.
     */
    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        String meshFilePath;
        int numberOfViewPoints;
        if (args.length != 2) {
            String errorMessage = "Incoming arguments like mesh file and/or number of view points are missing!";
            LOGGER.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        } else {
            meshFilePath = args[0];
            numberOfViewPoints = Integer.parseInt(args[1]);
        }
        JsonDeserializer jsonDeserializer = new JsonDeserializer(Paths.get(meshFilePath));
        ViewPointsStringWriter viewPointsStringWriter = new ViewPointsStringWriter(
                jsonDeserializer.deserializeJson().stream().sorted().collect(Collectors.toList()),
                numberOfViewPoints
        );
        viewPointsStringWriter.writeElements();
        LOGGER.info("calculation time: {} milliseconds", (System.currentTimeMillis() - begin));
    }
}
