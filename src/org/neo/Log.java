package org.neo;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 *
 * @author theninger
 */
public class Log {

    public static final Logger logger = Logger.getLogger("neo");
    static {
        logger.setLevel(Level.WARNING);
    }
    private static Handler testHandler;

    public static void config(String msg) {
        logger.config(msg);
    }

    public static void error(String msg) {
        logger.severe(msg);
    }

    public static void error(Throwable th) {
        logger.log(Level.SEVERE, th.toString());
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warning(String msg) {
        logger.warning(msg);
    }

    public static void testStart() {
        logger.setUseParentHandlers(false);
        logger.addHandler(testHandler = new Handler() {

            @Override
            public void publish(LogRecord record) {
                System.out.println(record.getMessage());
            }

            @Override
            public void flush() {
                System.out.flush();
            }

            @Override
            public void close() throws SecurityException {
            }
        });
        logger.setLevel(Level.INFO);
    }

    public static void testStop() {
        if (testHandler != null) {
            System.out.flush();
            logger.setLevel(Level.WARNING);
            logger.setUseParentHandlers(true);
            logger.removeHandler(testHandler);
            testHandler = null;
        }
    }

}
