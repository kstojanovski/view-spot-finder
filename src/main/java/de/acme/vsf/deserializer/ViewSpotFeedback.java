package de.acme.vsf.deserializer;

import java.util.Map;
import java.util.Optional;

public class ViewSpotFeedback {

    private final boolean isVewSpot;
    private final Map<Integer, Element> allViewSpots;

    public ViewSpotFeedback(boolean isVewSpot, Map<Integer, Element> allViewSpots) {
        this.isVewSpot = isVewSpot;
        this.allViewSpots = allViewSpots;
    }

    public boolean isVewSpot() {
        return isVewSpot;
    }

    public Optional<Map<Integer, Element>> getAllViewSpots() {
        return Optional.ofNullable(allViewSpots);
    }
}
