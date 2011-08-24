package org.rio.parse;

import org.rio.RioException;

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
