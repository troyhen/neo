package org.dia.parse;

import org.dia.DiaException;

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
