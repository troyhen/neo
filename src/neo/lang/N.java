package neo.lang;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import neo.lang.Closure.Notice;

/**
 *
 * @author Troy Heninger
 */
public class N {

    public static String capitalize(String word) {
        if (word == null || word.length() == 0) return word;
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    public static int compare(Comparable obj1, Comparable obj2) {
        if (obj1 == null && obj2 == null) return 0;
        return obj1.compareTo(obj2);
    }

    public static int compare(int obj1, int obj2) {
        return obj1 == obj2 ? 0 : obj1 < obj2 ? -1 : 1;
    }

    public static int compare(long obj1, long obj2) {
        return obj1 == obj2 ? 0 : obj1 < obj2 ? -1 : 1;
    }

    public static int compare(double obj1, double obj2) {
        return obj1 == obj2 ? 0 : obj1 < obj2 ? -1 : 1;
    }

    public static int compare(float obj1, float obj2) {
        return obj1 == obj2 ? 0 : obj1 < obj2 ? -1 : 1;
    }

//    private static Method[] eachMulti;
//    static {
//        try {
//            eachMulti = new Method[]{N.class.getDeclaredMethod("each", Iterable.class, Closure.class)};
//        } catch (Exception ex) {
//            Log.error(ex);
//        }
//    }

    public static void each(Iterable array, Closure cl) {
        if (cl == null) throw new NullPointerException("missing closure");
        if (array == null) return;
        synchronized(array) {
            try {
                for (Object elem : array) {
                    cl.invoke(elem);
                }
            } catch (Notice ex) {
            }
        }
    }

    /**
     * def each(array~Any, null) throws NullPointerException
     * def each(null, cl~Any) does nothing
     * def each(array~[Any], cl~Closure) invokes closure with each element
     * def each(array~Iterable, cl~Closure) invokes closure with each element
     *
     * @param array
     * @param cl
     */
    public static void each(Object array, Closure cl) {
        if (cl == null) throw new NullPointerException("missing closure");
        if (array == null) return;
        if (array.getClass().isArray()) {
            synchronized(array) {
                try {
                    for (int ix = 0, iz = Array.getLength(array); ix < iz; ix++) {
                        Object elem = Array.get(array, ix);
                        cl.invoke(elem);
                    }
                } catch (Notice ex) {
                }
            }
        } else if (array instanceof Iterable) {
            each((Iterable) array, cl);
        } else {
            throw new IllegalArgumentException("unsupported type: " + array.getClass());
        }
    }

    public static void eachIndex(List array, Closure cl) {
        if (cl == null) throw new NullPointerException("missing closure");
        if (array == null) return;
        synchronized(array) {
            try {
                for (int ix = 0, iz = array.size(); ix < iz; ix++) {
                    cl.invoke(ix);
                }
            } catch (Notice ex) {
            }
        }
    }

    /**
     * def eachIndex(array~Any, null) throws NullPointerException
     * def eachIndex(null, cl~Any) does nothing
     * def eachIndex(array~[Any], cl~Closure) invokes closure with index of each element
     * def eachIndex(array~List, cl~Closure) invokes closure with index of each element
     *
     * @param array
     * @param cl
     */
    public static void eachIndex(Object array, Closure cl) {
        if (cl == null) throw new NullPointerException("missing closure");
        if (array == null) return;
        if (array.getClass().isArray()) {
            synchronized(array) {
                try {
                    for (int ix = 0, iz = Array.getLength(array); ix < iz; ix++) {
                        cl.invoke(ix);
                    }
                } catch (Notice ex) {
                }
            }
        } else if (array instanceof List) {
            eachIndex((List) array, cl);
        } else {
            throw new IllegalArgumentException("unsupported type: " + array.getClass());
        }
    }

    public static void eachWithIndex(Iterable array, Closure cl) {
        if (cl == null) throw new NullPointerException("missing closure");
        if (array == null) return;
        synchronized(array) {
            try {
                int ix = 0;
                for (Object elem : array) {
                    cl.invoke(elem, ix++);
                }
            } catch (Notice ex) {
            }
        }
    }

    /**
     * def eachWithIndex(array~Any, null) throws NullPointerException
     * def eachWithIndex(null, cl~Any) does nothing
     * def eachWithIndex(array~[Any], cl~Closure) invokes closure with each element and its index
     * def eachWithIndex(array~Iterable, cl~Closure) invokes closure with each element and its index
     *
     * @param array
     * @param cl
     */
    public static void eachWithIndex(Object array, Closure cl) {
        if (cl == null) throw new NullPointerException("missing closure");
        if (array == null) return;
        if (array.getClass().isArray()) {
            synchronized(array) {
                try {
                    for (int ix = 0, iz = Array.getLength(array); ix < iz; ix++) {
                        Object elem = Array.get(array, ix);
                        cl.invoke(elem, ix);
                    }
                } catch (Notice ex) {
                }
            }
        } else if (array instanceof Iterable) {
            eachWithIndex((Iterable) array, cl);
        } else {
            throw new IllegalArgumentException("unsupported type: " + array.getClass());
        }
    }

    public static String join(Object array) {
        return join(array, null);
    }

    public static String join(Object array, String between) {
        if (array == null) return "";
        if (array.getClass().isArray()) {
            if (between == null) between = "";
            StringBuilder buff = new StringBuilder();
            boolean after = false;
            synchronized(array) {
                for (int ix = 0, iz = Array.getLength(array); ix < iz; ix++) {
                    Object elem = Array.get(array, ix);
                    if (elem != null) {
                        if (after) buff.append(between);
                        else after = true;
                        buff.append(elem);
                    }
                }
            }
            return buff.toString();
        } else if (array instanceof Iterable) {
            return join((Iterable) array, between);
        }
        throw new IllegalArgumentException("unsupported type: " + array.getClass());
    }

    public static String join(Iterable array) {
        return join(array, null);
    }

    public static String join(Iterable array, String between) {
        if (array == null) return "";
        if (between == null) between = "";
        StringBuilder buff = new StringBuilder();
        boolean after = false;
        synchronized(array) {
            for (Object elem : array) {
                if (elem != null) {
                    if (after) buff.append(between);
                    else after = true;
                    buff.append(elem);
                }
            }
        }
        return buff.toString();
    }

    public static String plus(String left, Object right) {
        return left + right;
    }

    public static String plus(Object left, String right) {
        return left + right;
    }

    public static <T> T to(Object obj, T example) {
        if (example == null) throw new NullPointerException("missing example");
        if (obj == null) return null;
        return to(obj, (Class<T>) example.getClass());
    }

    public static <T> T to(Object obj, Class<T> type) {
        if (type == null) throw new NullPointerException("missing type");
        if (type == Boolean.class) return (T) Boolean.valueOf(toboolean(obj));
        if (type == Byte.class) return (T) Byte.valueOf(tobyte(obj));
        if (type == Character.class) return (T) Character.valueOf(tochar(obj));
        if (type == Double.class) return (T) Double.valueOf(todouble(obj));
        if (type == Float.class) return (T) Float.valueOf(tofloat(obj));
        if (type == Integer.class) return (T) Integer.valueOf(toint(obj));
        if (type == Long.class) return (T) Long.valueOf(tolong(obj));
        if (type == Short.class) return (T) Short.valueOf(toshort(obj));
        if (type == String.class) return (T) toString(obj);
        if (List.class.isAssignableFrom(type)) return (T) toList(obj);
        if (obj == null) return null;
        throw new IllegalArgumentException("unsupported type: " + type);
    }

//    public static <T> T[] toArray(Object obj, Class<T> type) {
//        if (obj == null) return (T[]) Array.newInstance(type, 0);
//        if (List.class.isAssignableFrom(type)) return toArray(obj, List<T>.class);
//    }
//
//    public static <T> T[] toArray(Object val, T[] array) {
//
//    }

    public static boolean toboolean(Collection val) {
        if (val == null) return false;
        return !val.isEmpty();
    }

    public static boolean toboolean(Boolean val) {
        if (val == null) return false;
        return val.booleanValue();
    }

    public static boolean toboolean(Object val) {
        if (val == null) return false;
        if (val.getClass().isArray()) return Array.getLength(val) > 0;
        if (val instanceof Collection) return toboolean((Collection) val);
        if (val instanceof Boolean) return toboolean((Boolean) val);
        if (val instanceof Number) return toboolean((Number) val);
        if (val instanceof String) return toboolean((String) val);
        return true;
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
        if (val instanceof Boolean) return tobyte((Boolean) val);
        if (val instanceof Number) return tobyte((Number) val);
        if (val instanceof String) return tobyte((String) val);
        throw new IllegalArgumentException("unsupported type: " + val.getClass());
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
        if (val instanceof Character) return tochar((Character) val);
        if (val instanceof Number) return tochar((Number) val);
        if (val instanceof String) return tochar((String) val);
        throw new IllegalArgumentException("unsupported type: " + val.getClass());
    }

    public static char tochar(Character val) {
        if (val == null) return '\0';
        return val.charValue();
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
        if (val == null) return 0.0;
        if (val instanceof Boolean) return todouble((Boolean) val);
        if (val instanceof Number) return todouble((Number) val);
        if (val instanceof String) return todouble((String) val);
        throw new IllegalArgumentException("unsupported type: " + val.getClass());
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
        if (val instanceof Boolean) return tofloat((Boolean) val);
        if (val instanceof Number) return tofloat((Number) val);
        if (val instanceof String) return tofloat((String) val);
        throw new IllegalArgumentException("unsupported type: " + val.getClass());
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
        if (val instanceof Boolean) return toint((Boolean) val);
        if (val instanceof Number) return toint((Number) val);
        if (val instanceof String) return toint((String) val);
        throw new IllegalArgumentException("unsupported type: " + val.getClass());
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

    public static List toList(Object val) {
        if (val == null) return new ArrayList();
        if (val instanceof List) return (List) val;
        if (val instanceof Collection) return toList((Collection) val);
        if (val.getClass().isArray()) {
            int iz = Array.getLength(val);
            List list = new ArrayList(iz);
            for (int ix = 0; ix < iz; ix++) {
                list.add(Array.get(val, ix));
            }
            return list;
        }
        List list = new ArrayList();
        list.add(val);
        return list;
    }

    public static List toList(Collection val) {
        int iz = Array.getLength(val);
        List list = new ArrayList(iz);
        list.addAll(val);
        return list;
    }

    public static List toList(Iterable val) {
        List list = new ArrayList();
        for (Object obj : val) {
            list.add(obj);
        }
        return list;
    }

    public static List toList(List val) {
        return val;
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
        if (val instanceof java.util.Calendar) return tolong((java.util.Calendar) val);
        if (val instanceof java.util.Date) return tolong((java.util.Date) val);
        if (val instanceof Number) return tolong((Number) val);
        if (val instanceof String) return tolong((String) val);
        throw new IllegalArgumentException("unsupported type: " + val.getClass());
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
        if (val instanceof Number) return toshort((Number) val);
        if (val instanceof String) return toshort((String) val);
        throw new IllegalArgumentException("unsupported type: " + val.getClass());
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
        if (val == null) return "";
        if (val instanceof Boolean) return toString((Boolean) val);
        if (val instanceof String) return toString((String) val);
        return String.valueOf(val);
    }

    public static String toString(String val) {
        if (val == null) return "";
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
