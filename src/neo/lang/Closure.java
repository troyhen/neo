package neo.lang;

/**
 *
 * @author Troy Heninger
 */
public abstract class Closure {

    public static class Notice extends Exception {

        public Notice() {
        }
    }

    public static class BreakNotice extends Notice {

        public BreakNotice() {
        }
    }

    public static class ReturnNotice extends Notice {
        private final Object value;

        public ReturnNotice(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }

    protected final Context context;

    public Closure(Context locals) {
        this.context = locals;
    }

    public abstract Object invoke(Object...args) throws BreakNotice, ReturnNotice;
}
