package org.rio.parse;

/**
 *
 * @author Trpy Heninger
 */
public class Missing extends RuntimeException {

    public Missing() {
    }

    public Missing(String name) {
        super(name);
    }
}
