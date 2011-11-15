package org.neo.parse;

import org.neo.NeoException;

/**
 * Created by IntelliJ IDEA.
 * User: theninger
 * Date: 11/3/11
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mismatch extends ParseException {

//    private String name;

    public Mismatch(Node node, String name) {
        super("Expected " + name, node);
//        this.name = name;
    }
}
