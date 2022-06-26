package de.acme.vsf.writer;

import de.acme.vsf.deserializer.Element;
import de.acme.vsf.deserializer.ViewSpotFeedback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ViewPointsStringWriter {

    private static final Logger LOGGER = LogManager.getLogger(ViewPointsStringWriter.class);
    private static final Logger FINAL_LOGGER = LogManager.getLogger("FinalOutput");

    private final List<Element> elements;
    private final int numberOfViewSpots;

    private final List<Integer> elementBlackList = new ArrayList<>();

    public ViewPointsStringWriter(List<Element> elements, int numberOfViewSpots) {
        this.elements = elements;
        this.numberOfViewSpots = numberOfViewSpots;
    }

    /**
     * Creates the output of the view points.
     */
    public void writeElements() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append('\n');
        writeViewPoints(stringBuilder);
        stringBuilder.append("]");
        FINAL_LOGGER.info(stringBuilder);
    }

    /**
     * Creates the elements output of the view points.
     *
     * @param stringBuilder gathers the element for output of the view points.
     */
    private void writeViewPoints(StringBuilder stringBuilder) {
        int currentNumberOfViewSpots = 1;
        for (Element element : elements) {
            ViewSpotFeedback viewSpotFeedback = element.evaluateForViewSpot();
            if (viewSpotFeedback.isVewSpot() && !elementBlackList.contains(element.getId())) {
                if (currentNumberOfViewSpots > numberOfViewSpots && numberOfViewSpots > 0) {
                    LOGGER.info("view points found {}", numberOfViewSpots);
                    return;
                }
                Optional<Map<Integer, Element>> allViewSpots = viewSpotFeedback.getAllViewSpots();
                allViewSpots.ifPresent(integerElementMap -> checkAndSetElementToBlackList(element, integerElementMap));
                stringBuilder.append("   {").append("element_id: ").append(element.getId()).append(", ").append("value: ").append(element.getValue()).append("}").append(",").append('\n');
                ++currentNumberOfViewSpots;
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        }
        LOGGER.info("view points found {}", currentNumberOfViewSpots);
    }

    /**
     * Adds near identical view spot values to the blacklist.
     *
     * @param element      inbound elements.
     * @param allViewSpots inbound view spot and potentially its duplicates for the current element.
     */
    private void checkAndSetElementToBlackList(
            Element element,
            Map<Integer, Element> allViewSpots) {
        for (Map.Entry<Integer, Element> entry : allViewSpots.entrySet()) {
            if (!entry.getKey().equals(element.getId())) {
                elementBlackList.add(entry.getValue().getId());
            }
        }
    }

}
