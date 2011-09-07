package org.neo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author theninger
 */
public class Log {

    public static final Logger logger = Logger.getLogger("neo");
    static {
        logger.setLevel(Level.WARNING);
    }

}
