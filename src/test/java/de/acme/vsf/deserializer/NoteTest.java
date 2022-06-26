package de.acme.vsf.deserializer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class NoteTest {


    public static final double DELTA = 1e-15;

    @Test
    public void testSetValue() {
        //Arrange
        Node node = new Node(0);
        Element element0 = new Element(0, new ArrayList<>(Collections.singletonList(node)));
        Element element1 = new Element(1, new ArrayList<>(Collections.singletonList(node)));
        Element element2 = new Element(2, new ArrayList<>(Collections.singletonList(node)));

        //Act
        element0.setValue(1);
        element1.setValue(2);
        element2.setValue(3);

        //Assert
        assertEquals(3, node.getMaxValue(), DELTA);
        assertEquals(1, node.getElementsWithMaxValuesSize());
    }

    @Test
    public void testSetValueSame() {
        //Arrange
        Node node = new Node(0);
        Element element0 = new Element(0, new ArrayList<>(Collections.singletonList(node)));
        Element element1 = new Element(1, new ArrayList<>(Collections.singletonList(node)));
        Element element2 = new Element(2, new ArrayList<>(Collections.singletonList(node)));

        //Act
        element0.setValue(1);
        element1.setValue(1);
        element2.setValue(1);

        //Assert
        assertEquals(1, node.getMaxValue(), DELTA);
        assertEquals(3, node.getElementsWithMaxValuesSize());
    }

    @Test
    public void testSetValueMax() {
        //Arrange
        Node node = new Node(0);
        Element element0 = new Element(0, new ArrayList<>(Collections.singletonList(node)));
        Element element1 = new Element(1, new ArrayList<>(Collections.singletonList(node)));
        Element element2 = new Element(2, new ArrayList<>(Collections.singletonList(node)));

        //Act
        element0.setValue(1);
        element1.setValue(2);
        element2.setValue(1);

        //Assert
        assertEquals(2, node.getMaxValue(), DELTA);
        assertEquals(1, node.getElementsWithMaxValuesSize());
    }

    @Test
    public void testSetValueTwo() {
        //Arrange
        Node node = new Node(0);
        Element element0 = new Element(0, new ArrayList<>(Collections.singletonList(node)));
        Element element1 = new Element(1, new ArrayList<>(Collections.singletonList(node)));
        Element element2 = new Element(2, new ArrayList<>(Collections.singletonList(node)));

        //Act
        element0.setValue(2);
        element1.setValue(1);
        element2.setValue(2);

        //Assert
        assertEquals(2, node.getMaxValue(), DELTA);
        assertEquals(2, node.getElementsWithMaxValuesSize());
    }
}
