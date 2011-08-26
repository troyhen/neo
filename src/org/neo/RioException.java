package org.neo;

/**
 *
 * @author Troy Heninger
 */
public class RioException extends RuntimeException {

    public RioException(String msg) {
        super(msg);
    }

    public RioException(Throwable th) {
        super(th);
    }

}
