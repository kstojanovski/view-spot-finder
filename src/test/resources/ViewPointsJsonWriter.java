package de.xibix.vsf.writer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import de.acme.vsf.deserializer.Element;
import de.acme.vsf.deserializer.ViewSpotFeedback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ViewPointsJsonWriter {

    private final List<Element> elements;
    private final int numberOfViewSpots;
    public ViewPointsJsonWriter(List<Element> elements, int numberOfViewSpots) {
        this.elements = elements;
        this.numberOfViewSpots = numberOfViewSpots;
    }

    public void writeElement() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            JsonFactory jsonFactory = new JsonFactory();
            JsonGenerator jsonGenerator = jsonFactory
                    .createGenerator(stream, JsonEncoding.UTF8);
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStartArray();
            writeViewPoints(jsonGenerator);
            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
            jsonGenerator.close();
        } catch (IOException e) {
            throw new ViewPointsWriteException("Something went wrong on writing view points!");
        }
    }

    private void writeViewPoints(JsonGenerator jsonGenerator) throws IOException {
        List<Integer> elementBlackList = new ArrayList<>();
        int currentNumberOfViewSpots = 0;
        for (Element element : elements) {
            ViewSpotFeedback viewSpotFeedback = element.evaluateForViewSpot();
            if (viewSpotFeedback.isVewSpot() && !elementBlackList.contains(element.getId())) {
                if (currentNumberOfViewSpots > numberOfViewSpots) {
                    return;
                }
                checkAndSetElementToBlackList(elementBlackList, element, viewSpotFeedback.getAllViewSpots());
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("element_id", element.getId());
                jsonGenerator.writeNumberField("value", element.getValue());
                jsonGenerator.writeEndObject();
                ++currentNumberOfViewSpots;
            }
        }
    }

    private void checkAndSetElementToBlackList(
            List<Integer> elementBlackList,
            Element element,
            Optional<Map<Integer, Element>> allViewSpots) {
        if (allViewSpots.isPresent()) {
            for (Map.Entry<Integer, Element> entry: allViewSpots.get().entrySet()) {
                if (!entry.getKey().equals(element.getId())) {
                    elementBlackList.add(entry.getValue().getId());
                }
            }
        }
    }

}
