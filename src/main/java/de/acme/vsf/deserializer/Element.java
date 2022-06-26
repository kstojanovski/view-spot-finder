package de.acme.vsf.deserializer;

import java.util.List;
import java.util.Objects;

public class Element implements Comparable<Element> {
    private final int id;
    private final List<Node> nodes;
    private double value;

    Element(int id, List<Node> nodes) {
        this.id = id;
        this.nodes = nodes;
    }

    /**
     * Sets  the value to this element and also adds this element to the nodes for possible duplicate.
     *
     * @param value
     */
    void setValue(double value) {
        this.value = value;
        for (Node node : nodes) {
            node.setElementValue(this);
        }
    }

    /**
     * Evaluates the elements for view spots.
     *
     * @return {@link ViewSpotFeedback} object with information if this element is a view spot.
     */
    public ViewSpotFeedback evaluateForViewSpot() {
        for (Node node : nodes) {
            if (node.getMaxValue() > value) {
                return new ViewSpotFeedback(false, null);
            }
            int elementsWithMaxValuesSize = node.getElementsWithMaxValuesSize();
            if (node.getMaxValue() == value) {
                if (elementsWithMaxValuesSize == 1) {
                    return new ViewSpotFeedback(true, null);
                } else if (elementsWithMaxValuesSize > 1) {
                    return new ViewSpotFeedback(true, node.getElementsWithMaxValues());
                }
            }
        }
        return new ViewSpotFeedback(true, null);
    }

    public int getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(Element o) {
        return Double.compare(o.value, this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element)) return false;
        Element element = (Element) o;
        return getId() == element.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
