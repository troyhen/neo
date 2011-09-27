package org.neo.core;

import org.neo.NeoException;
import org.neo.Node;

/**
 *
 * @author theninger
 */
class SymbolException extends NeoException {

    public SymbolException(String string, Node node) {
        super(string + " at line " + node.getLine());
    }

}
