package org.rio.parse;

import org.rio.DiaException;

/**
 *
 * @author theninger
 */
public class ParseException extends DiaException {

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(Throwable ex) {
        super(ex);
    }

}
