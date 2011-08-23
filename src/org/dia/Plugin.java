package org.dia;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dia.lex.Lexer;
import org.dia.lex.Token;
import org.dia.parse.Production;

/**
 *
 * @author Troy Heninger
 */
public class Plugin implements Lexer {

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
        Compiler.compiler().add(name, new Production(this, name, ""));
    }

    protected void addParser(String name, String structure) {
        Compiler.compiler().add(name, new Production(this, name, structure));
    }

    public void close() {
    }

    public Token consume(Lexer lexer, int chars) {
        return Compiler.compiler().consume(lexer, chars);
    }

    public Token consume(Lexer lexer, int chars, Object value) {
        return Compiler.compiler().consume(lexer, chars, value);
    }

//    @Override
//    public boolean isIgnored() { return false; }


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

    public Node matched(String name, Node node) throws Exception {
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

//    protected Node or(Node...nodes) {
//        for (Node node : nodes) {
//            if (node != null) return node;
//        }
//        throw new Missing();
//    }

//    public void parse(Compiler parser) {
//    }

//    public abstract void compile(Node node, Backend back);

    @Override
    public String toString() {
        return getName();
    }
}
