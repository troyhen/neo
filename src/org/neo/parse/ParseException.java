package org.neo.parse;

import org.neo.NeoException;

/**
 *
 * @author theninger
 */
public class ParseException extends NeoException {

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(Throwable ex) {
        super(ex);
    }

}
