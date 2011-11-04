package org.neo.parse;

import org.neo.NeoException;

/**
 * Created by IntelliJ IDEA.
 * User: theninger
 * Date: 11/3/11
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mismatch extends NeoException {
    private Node node;
    private String name;

    public Mismatch(Node node, String name) {
        super("Expected " + name + " at " + node);
        this.node = node;
        this.name = name;
    }
}
