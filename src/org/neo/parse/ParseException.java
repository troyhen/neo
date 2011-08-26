package org.neo.parse;

import org.neo.RioException;

/**
 *
 * @author theninger
 */
public class ParseException extends RioException {

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(Throwable ex) {
        super(ex);
    }

}
