package org.neo.parse;

import org.neo.NeoException;

/**
 * Thrown when there are problems in the language grammar.
 * @author Troy Heninger
 */
public class GrammarException extends NeoException {
    public GrammarException(String msg) {
        super(msg);
    }
}
