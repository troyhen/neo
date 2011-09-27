package neo.lang;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Troy Heninger
 */
public class Context {

    private Map<String, Object> vars = new HashMap<String, Object>();

    public void add(String name, Object value) {
        vars.put(name, value);
    }

    public void addAll(Object...pairs) {
        for (int ix = 0, iz = pairs.length; ix < iz; ) {
            vars.put((String) pairs[ix++],pairs[ix++]);
        }
    }

    public Object get(String name) {
        return vars.get(name);
    }
    
}
