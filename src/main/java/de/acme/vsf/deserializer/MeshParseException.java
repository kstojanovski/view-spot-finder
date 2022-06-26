package de.acme.vsf.deserializer;

/**
 * This exception is used when something goes wrong on mesh parsing.
 */
public class MeshParseException extends RuntimeException {
    MeshParseException(String message) {
        super(message);
    }
}
