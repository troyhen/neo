package neo.lang;

import java.util.Iterator;

/**
 * @author Troy Heninger
 */
public class Range<T extends Comparable> implements Iterable<T> {

    private T start, end;
    private boolean inclusive, forward;
    private Number step;

    public Range(T start, T end) {
        this(start, end, false, null);
    }

    public Range(T start, T end, Number step) {
        this(start, end, false, step);
    }

    public Range(T start, T end, boolean inclusive) {
        this(start, end, inclusive, null);
    }

    public Range(T start, T end, boolean inclusive, Number step) {
        if (start == null) throw new NullPointerException("start");
        if (end == null) throw new NullPointerException("end");
        Class type1 = start.getClass();
        Class type2 = end.getClass();
        if (type1 != type2) throw new IllegalArgumentException("Range limit types do not match: " + type1 + " and " + type2);
        this.start = start;
        this.end = end;
        forward = end.compareTo(start) >= 0;
        this.inclusive = inclusive;
        if (step == null) {
            if (forward) step = 1;
            else step = -1;
        }
        this.step = step;
    }

    public T getEnd() {
        return end;
    }

    public T getStart() {
        return start;
    }

    public Number getStep() {
        return step;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            
            T value = start;
            T next = null;

            @Override
            public boolean hasNext() {
                int withEnd = value.compareTo(getNext());
                if (forward) {
                    return inclusive ? withEnd <= 0 : withEnd < 0;
                }
                return inclusive ? withEnd >= 0 : withEnd > 0;
            }

            private T getNext() {
                if (next != null) return next;
                if (step == null) {
                    if (forward) return next = (T) N.next(value);
                    return next = (T) N.prev(value);
                }
                return next = (T) N.add(value, step);
            }

            @Override
            public T next() {
                value = getNext();
                next = null;
                return value;
            }

            @Override
            public void remove() { }
        };
    }

    public boolean match(Comparable value) {
        if (value == null) return false;
        int withStart = value.compareTo(start);
        int withEnd = value.compareTo(end);
        if (forward) {
            if (inclusive) return withStart >= 0 && withEnd <= 0;
            return withStart >= 0 && withEnd < 0;
        }
        if (inclusive) return withStart <= 0 && withEnd >= 0;
        return withStart <= 0 && withEnd > 0;
    }
}
