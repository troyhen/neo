package org.neo.parse;

import org.neo.NeoException;

/**
 *
 * @author theninger
 */
public class ParseException extends NeoException {

//    protected Node node;

    public ParseException(String msg, Node node) {
        super(msg + " at " + node.getName() + " (line " + node.getLine() + ')');
//        this.node = node;
    }

    public ParseException(Throwable ex) {
        super(ex);
    }

}
