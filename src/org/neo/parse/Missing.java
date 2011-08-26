package org.neo.parse;

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
