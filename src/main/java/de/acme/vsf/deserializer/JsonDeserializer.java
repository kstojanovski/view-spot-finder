package de.acme.vsf.deserializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Parses the inbound mesh file and created elements related to the nodes.
 */
public class JsonDeserializer {

    private static final Logger LOGGER = LogManager.getLogger(JsonDeserializer.class);

    private final Path path;

    public JsonDeserializer(Path path) {
        this.path = path;
    }


    /**
     * Parses the inbound mesh file and created elements related to the nodes.
     *
     * @return element list related to the nodes.
     */
    public Collection<Element> deserializeJson() {
        Map<Integer, Element> elements = new HashMap<>();
        Map<Integer, Node> nodes = new HashMap<>();

        try (JsonParser jsonParser = new JsonFactory().createParser(path.toFile())) {
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                parseNodes(nodes, jsonParser);
                parseElements(elements, nodes, jsonParser);
                parseValues(elements, jsonParser);
            }
        } catch (IOException e) {
            String errorMessage = "IOException on parsing the file!";
            LOGGER.error(errorMessage);
            throw new MeshParseException(errorMessage);
        }

        LOGGER.info("nodes found {}", nodes.size());
        LOGGER.info("elements found {}", elements.size());
        return elements.values();
    }

    /**
     * Parses values from the mesh file.
     *
     * @param elements   inbounds elements.
     * @param jsonParser JsonParser with the mesh content.
     * @throws IOException if something goes wrong on parsing.
     */
    private void parseValues(Map<Integer, Element> elements, JsonParser jsonParser) throws IOException {
        jsonParser.nextToken();
        if ("values".equals(jsonParser.getCurrentName())) {
            jsonParser.nextToken();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                jsonParser.nextToken();
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    int elementId = -1;
                    if ("element_id".equals(jsonParser.getCurrentName())) {
                        elementId = jsonParser.getIntValue();
                    }
                    jsonParser.nextToken();
                    double value = Double.NaN;
                    if ("value".equals(jsonParser.getCurrentName())) {
                        jsonParser.nextToken();
                        value = jsonParser.getDoubleValue();
                    }
                    if (elementId == -1 || Double.isNaN(value)) {
                        String errorMessage = "Element id not found!";
                        LOGGER.error(errorMessage);
                        throw new MeshParseException(errorMessage);
                    }
                    elements.get(elementId).setValue(value);
                }
            }
        }
    }

    /**
     * Parses elements from the mesh file.
     *
     * @param elements   inbound empty element map.
     * @param nodes      inbound list of nodes.
     * @param jsonParser JsonParser with the mesh content.
     * @throws IOException if something goes wrong on parsing.
     */
    private void parseElements(Map<Integer, Element> elements, Map<Integer, Node> nodes, JsonParser jsonParser)
            throws IOException {
        jsonParser.nextToken();
        if ("elements".equals(jsonParser.getCurrentName())) {
            jsonParser.nextToken();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                jsonParser.nextToken();
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    int elementId = -1;
                    if ("id".equals(jsonParser.getCurrentName())) {
                        elementId = jsonParser.getIntValue();
                    }
                    jsonParser.nextToken();
                    List<Node> elementNodes = new ArrayList<>();
                    if ("nodes".equals(jsonParser.getCurrentName())) {
                        jsonParser.nextToken();
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            elementNodes.add(nodes.get(jsonParser.getIntValue()));
                        }
                    }
                    if (elementId == -1) {
                        String errorMessage = "Element id not found!";
                        LOGGER.error(errorMessage);
                        throw new MeshParseException(errorMessage);
                    }
                    elements.put(elementId, new Element(elementId, elementNodes));
                }
            }
        }
    }

    /**
     * Parses nodes from the mesh file.
     *
     * @param nodes      inbound empty node map.
     * @param jsonParser JsonParser with the mesh content.
     * @throws IOException if something goes wrong on parsing.
     */
    private void parseNodes(Map<Integer, Node> nodes, JsonParser jsonParser) throws IOException {
        jsonParser.nextToken();
        if ("nodes".equals(jsonParser.getCurrentName())) {
            jsonParser.nextToken();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                jsonParser.nextToken();
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    if ("id".equals(jsonParser.getCurrentName())) {
                        int nodeId = jsonParser.getIntValue();
                        nodes.put(nodeId, new Node(nodeId));
                    }
                }
            }
        }
    }
}
