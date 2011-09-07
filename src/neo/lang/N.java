package neo.lang;

/**
 *
 * @author Troy Heninger
 */
public class N {

    public static <T> T to(Object obj, T type) {
        throw new IllegalArgumentException("Unknown type cast");
    }

    public static boolean toboolean(Object val) {
        if (val == null) return false;
        throw new IllegalArgumentException("Unknown type cast");
    }

    public static boolean toboolean(Number val) {
        if (val == null) return false;
        return toboolean(toint(val));
    }

    public static boolean toboolean(String val) {
        if (val == null) return false;
        return val.length() > 0;
    }

    public static boolean toboolean(boolean val) {
        return val;
    }

    public static boolean toboolean(double val) {
        return val != 0.0;
    }

    public static boolean toboolean(float val) {
        return val != 0.0f;
    }

    public static boolean toboolean(int val) {
        return val != 0;
    }

    public static boolean toboolean(long val) {
        return val != 0l;
    }

    public static byte tobyte(Object val) {
        if (val == null) return 0;
        throw new IllegalArgumentException("Unknown type cast");
    }

    public static byte tobyte(Number val) {
        if (val == null) return 0;
        return val.byteValue();
    }

    public static byte tobyte(String val) {
        if (val == null) return 0;
        return Byte.parseByte(val);
    }

    public static byte tobyte(boolean val) {
        return (byte) (val ? 1 : 0);
    }

    public static byte tobyte(byte val) {
        return val;
    }

    public static byte tobyte(double val) {
        return (byte) val;
    }

    public static byte tobyte(float val) {
        return (byte) val;
    }

    public static byte tobyte(int val) {
        return (byte) val;
    }

    public static byte tobyte(long val) {
        return (byte) val;
    }

    public static char tochar(Object val) {
        if (val == null) return 0;
        throw new IllegalArgumentException("Unknown type cast");
    }

    public static char tochar(Number val) {
        if (val == null) return 0;
        return tochar(toint(val));
    }

    public static char tochar(String val) {
        if (val == null || val.length() == 0) return 0;
        return val.charAt(0);
    }

    public static char tochar(boolean val) {
        return val ? 't' : 0;
    }

    public static char tochar(char val) {
        return val;
    }

    public static char tochar(double val) {
        return (char) val;
    }

    public static char tochar(float val) {
        return (char) val;
    }

    public static char tochar(int val) {
        return (char) val;
    }

    public static char tochar(long val) {
        return (char) val;
    }

    public static double todouble(Object val) {
        if (val == null) return 0f;
        throw new IllegalArgumentException("Unknown type cast");
    }

    public static double todouble(Number val) {
        if (val == null) return 0f;
        return val.doubleValue();
    }

    public static double todouble(String val) {
        if (val == null) return 0f;
        return Double.parseDouble(val);
    }

    public static double todouble(boolean val) {
        return val ? 1f : 0f;
    }

    public static double todouble(double val) {
        return val;
    }

    public static double todouble(float val) {
        return val;
    }

    public static double todouble(int val) {
        return val;
    }

    public static double todouble(long val) {
        return val;
    }

    public static float tofloat(Object val) {
        if (val == null) return 0f;
        throw new IllegalArgumentException("Unknown type cast");
    }

    public static float tofloat(Number val) {
        if (val == null) return 0f;
        return val.floatValue();
    }

    public static float tofloat(String val) {
        if (val == null) return 0f;
        return Float.parseFloat(val);
    }

    public static float tofloat(boolean val) {
        return val ? 1f : 0f;
    }

    public static float tofloat(double val) {
        return (float) val;
    }

    public static float tofloat(float val) {
        return val;
    }

    public static float tofloat(int val) {
        return val;
    }

    public static float tofloat(long val) {
        return val;
    }

    public static int toint(Object val) {
        if (val == null) return 0;
        throw new IllegalArgumentException("Unknown type cast");
    }

    public static int toint(Number val) {
        if (val == null) return 0;
        return val.intValue();
    }

    public static int toint(String val) {
        if (val == null) return 0;
        return Integer.parseInt(val);
    }

    public static int toint(boolean val) {
        return val ? 1 : 0;
    }

    public static int toint(double val) {
        return (int) val;
    }

    public static int toint(float val) {
        return (int) val;
    }

    public static int toint(int val) {
        return val;
    }

    public static int toint(long val) {
        return (int) val;
    }

    public static long tolong(java.util.Calendar val) {
        if (val == null) return 0l;
        return val.getTimeInMillis();
    }

    public static long tolong(java.util.Date val) {
        if (val == null) return 0l;
        return val.getTime();
    }

    public static long tolong(Object val) {
        if (val == null) return 0l;
        throw new IllegalArgumentException("Unknown type cast");
    }

    public static long tolong(Number val) {
        if (val == null) return 0l;
        return val.longValue();
    }

    public static long tolong(String val) {
        if (val == null) return 0l;
        return Long.parseLong(val);
    }

    public static long tolong(boolean val) {
        return val ? 1l : 0l;
    }

    public static long tolong(double val) {
        return (long) val;
    }

    public static long tolong(float val) {
        return (long) val;
    }

    public static long tolong(int val) {
        return val;
    }

    public static long tolong(long val) {
        return val;
    }

    public static short toshort(Object val) {
        if (val == null) return 0;
        throw new IllegalArgumentException("Unknown type cast");
    }

    public static short toshort(Number val) {
        if (val == null) return 0;
        return val.shortValue();
    }

    public static short toshort(String val) {
        if (val == null) return 0;
        return Short.parseShort(val);
    }

    public static short toshort(boolean val) {
        return (short) (val ? 1 : 0);
    }

    public static short toshort(double val) {
        return (short) val;
    }

    public static short toshort(float val) {
        return (short) val;
    }

    public static short toshort(int val) {
        return (short) val;
    }

    public static short toshort(long val) {
        return (short) val;
    }

    public static short toshort(short val) {
        return val;
    }

    public static String toString(Object val) {
        if (val == null) return null;
        throw new IllegalArgumentException("Unknown type cast");
    }

    public static String toString(Number val) {
        if (val == null) return null;
        return String.valueOf(val);
    }

    public static String toString(String val) {
        return val;
    }

    public static String toString(boolean val) {
        return val ? "true" : "";
    }

    public static String toString(double val) {
        return String.valueOf(val);
    }

    public static String toString(float val) {
        return String.valueOf(val);
    }

    public static String toString(int val) {
        return String.valueOf(val);
    }

    public static String toString(long val) {
        return String.valueOf(val);
    }

}
