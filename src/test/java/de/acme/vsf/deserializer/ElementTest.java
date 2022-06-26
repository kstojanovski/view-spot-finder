package de.acme.vsf.deserializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ElementTest {

    private static final Logger LOGGER = LogManager.getLogger(ElementTest.class);

    public static final double DELTA = 1e-15;

    @Test
    public void testElementInitialization() {
        //Arrange
        //Nodes
        Node node0 = new Node(0);
        Node node1 = new Node(1);
        Node node12 = new Node(12);
        List<Node> nodes = new ArrayList<>(Arrays.asList(node0, node1, node12));

        //Act
        //Element
        Element element = new Element(0, nodes);
        double value = 0.15154957113761364;
        element.setValue(value);

        //Assert
        assertEquals(value, element.getValue(), DELTA);
        for (Node node : nodes) {
            assertEquals(1, node.getElementsWithMaxValuesSize());
            assertEquals(value, node.getElementsWithMaxValues().entrySet().iterator()
                    .next().getValue().getValue(), DELTA);
        }
        ViewSpotFeedback viewSpotFeedback = element.evaluateForViewSpot();
        assertTrue(viewSpotFeedback.isVewSpot());
        assertFalse(viewSpotFeedback.getAllViewSpots().isPresent());
    }


    @Test
    public void testSeveralElements() {
        Map<Integer, Double> values = getValues();
        Map<Integer, Element> elements = getElements();
        for (Map.Entry<Integer, Element> integerElementEntry : elements.entrySet()) {
            integerElementEntry.getValue().setValue(values.get(integerElementEntry.getKey()));
        }
        int i = 0;
        for (Map.Entry<Integer, Element> integerElementEntry : elements.entrySet()) {
            ViewSpotFeedback viewSpotFeedback = integerElementEntry.getValue().evaluateForViewSpot();
            boolean vewSpot = viewSpotFeedback.isVewSpot();
            assertFalse(viewSpotFeedback.getAllViewSpots().isPresent());
            if (i == 1) {
                assertTrue(vewSpot);
            } else {
                assertFalse(vewSpot);
            }
            i++;
        }
    }

    @Test
    public void testSeveralElementsSameValue() {
        Map<Integer, Double> values = getSameValues();
        Map<Integer, Element> elements = getElements();
        for (Map.Entry<Integer, Element> integerElementEntry : elements.entrySet()) {
            integerElementEntry.getValue().setValue(values.get(integerElementEntry.getKey()));
        }
        for (Map.Entry<Integer, Element> integerElementEntry : elements.entrySet()) {
            ViewSpotFeedback viewSpotFeedback = integerElementEntry.getValue().evaluateForViewSpot();
            boolean vewSpot = viewSpotFeedback.isVewSpot();
            assertTrue(viewSpotFeedback.getAllViewSpots().isPresent());
            assertTrue(vewSpot);
            LOGGER.info("size of the same max values: " + viewSpotFeedback.getAllViewSpots().get().size());
        }
    }

    private Map<Integer, Element> getElements() {
        Map<Integer, Element> elements = new HashMap<>();
        Map<Integer, Node> nodes = getNodes();
        elements.put(
                0,
                new Element(0, new ArrayList<>(Arrays.asList(nodes.get(0), nodes.get(1), nodes.get(12))))
        );
        elements.put(
                1,
                new Element(1, new ArrayList<>(Arrays.asList(nodes.get(0), nodes.get(11), nodes.get(12))))
        );
        elements.put(
                2,
                new Element(2, new ArrayList<>(Arrays.asList(nodes.get(1), nodes.get(2), nodes.get(13))))
        );
        elements.put(
                3,
                new Element(3, new ArrayList<>(Arrays.asList(nodes.get(1), nodes.get(12), nodes.get(13))))
        );
        elements.put(
                4,
                new Element(4, new ArrayList<>(Arrays.asList(nodes.get(2), nodes.get(3), nodes.get(14))))
        );
        elements.put(
                5,
                new Element(5, new ArrayList<>(Arrays.asList(nodes.get(2), nodes.get(13), nodes.get(14))))
        );
        elements.put(
                6,
                new Element(6, new ArrayList<>(Arrays.asList(nodes.get(2), nodes.get(13), nodes.get(14))))
        );
        return elements;
    }

    private Map<Integer, Node> getNodes() {
        Map<Integer, Node> nodes = new HashMap<>();
        for (int i = 0; i < 16; i++) {
            nodes.put(i, new Node(i));
        }
        return nodes;
    }

    private Map<Integer, Double> getValues() {
        List<Double> pureValues = new ArrayList<>(
                Arrays.asList(
                        0.15154957113761364,
                        0.4320398994069125,
                        -0.11672516279133821,
                        0.034824408346275426,
                        -0.27768332035560167,
                        -0.3944084831469399,
                        -0.18334071378716763)
        );
        Map<Integer, Double> values = new HashMap<>();
        for (Double pureValue : pureValues) {
            values.put(pureValues.indexOf(pureValue), pureValue);
        }
        return values;
    }

    private Map<Integer, Double> getSameValues() {
        Map<Integer, Double> values = new HashMap<>();
        for (int i = 0; i < 10 ; i++) {
            values.put(i, 0.4320398994069125);
        }
        return values;
    }
}
