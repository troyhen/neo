package org.rio;

/**
 *
 * @author Troy Heninger
 */
public class DiaException extends RuntimeException {

    public DiaException(String msg) {
        super(msg);
    }

    public DiaException(Throwable th) {
        super(th);
    }

}
