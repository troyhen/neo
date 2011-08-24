package org.rio;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rio.lex.Lexer;
import org.rio.lex.Token;
import org.rio.parse.Production;

/**
 *
 * @author Troy Heninger
 */
public class PluginBase implements Plugin {

    private List<Lexer> lexers = new ArrayList<Lexer>();
    protected List<String> names = new ArrayList<String>();
    private Map<String, Method> cache = new HashMap<String, Method>();

    @Override
    public String getName() {
        if (names.isEmpty()) return getClass().getSimpleName();
        return names.get(0);
    }

    @Override
    public Plugin getPlugin() { return this; }

    public void add(Lexer lexer) {
        lexers.add(lexer);
        Compiler.compiler().literals.add(lexer.getName());
    }

    protected void addParser(String name) {
        Compiler.compiler().add(new Production(this, name, ""));
    }

    protected void addParser(String name, String structure) {
        Compiler.compiler().add(new Production(this, name, structure));
    }

    public void close() {
    }

    public Token consume(Lexer lexer, int chars) {
        return Compiler.compiler().consume(lexer, chars);
    }

    public Token consume(Lexer lexer, int chars, Object value) {
        return Compiler.compiler().consume(lexer, chars, value);
    }

    private static final Class<?>[] matchedSig = new Class<?>[] {
        Node.class
    };

    private Method lookup(String name) {
        Method method = cache.get(name);
        if (method == null) {
            if (cache.containsKey(name)) return null;
            try {
                method = getClass().getMethod(name, matchedSig);
            } catch(Exception e) {
            }
            cache.put(name, method);
        }
        return method;
    }

    public Node prune(String name, Node node) throws Exception {
        Method method = lookup(name);
        if (method == null) return node;
        return (Node) method.invoke(this, node);
    }

    public void open() {
    }
    
    @Override
    public Token nextToken() {
        for (Lexer lexer : lexers) {
            Token token = lexer.nextToken();
            if (token != null) return token;
        }
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }
}
