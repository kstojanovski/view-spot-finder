package de.acme.vsf.deserializer;

import java.util.HashMap;
import java.util.Map;

public class Node {

    private final int id;
    private double maxValue = Long.MIN_VALUE;
    private final Map<Integer, Element> elementsWithMaxValues;

    Node(int id) {
        this.id = id;
        this.elementsWithMaxValues = new HashMap<>();
    }

    /**
     * Sets the inbound element in the list if its value is max. Also gathers elements if they are equal values.
     *
     * @param element
     */
    void setElementValue(Element element) {
        double value = element.getValue();
        double tempMaxValue = maxValue;
        maxValue = Math.max(maxValue, value);
        if (tempMaxValue != maxValue) {
            elementsWithMaxValues.clear();
        }
        if (value == maxValue) {
            elementsWithMaxValues.put(element.getId(), element);
        }
    }

    public double getMaxValue() {
        return maxValue;
    }

    public int getId() {
        return id;
    }

    public int getElementsWithMaxValuesSize() {
        return elementsWithMaxValues.size();
    }

    public Map<Integer, Element> getElementsWithMaxValues() {
        return elementsWithMaxValues;
    }
}
