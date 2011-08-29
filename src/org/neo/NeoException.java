package org.neo;

/**
 *
 * @author Troy Heninger
 */
public class NeoException extends RuntimeException {

    public NeoException(String msg) {
        super(msg);
    }

    public NeoException(Throwable th) {
        super(th);
    }

}
