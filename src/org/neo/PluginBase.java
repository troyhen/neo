package org.neo;

import org.neo.parse.*;
import org.neo.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo.lex.Lexer;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class PluginBase implements Plugin {

    private List<Lexer> lexers = new ArrayList<Lexer>();
    private List<Production> productions = new ArrayList<Production>();
    protected List<String> names = new ArrayList<String>();
    private Map<String, Method> cache = new HashMap<String, Method>();

    public void add(Lexer lexer) {
        lexers.add(lexer);
        Engine.engine().literals.add(lexer.getName());
    }

    protected void addKeyword(String word) {
        Engine.engine().addKeyword(word);
    }
    
//    protected void addParser(String name) {
//        addParser(name, "");
//    }

    protected void addParser(String name, String structure) {
        final Production production = new Production(this, name, structure);
        productions.add(production);
//        Engine.engine().index(production);
    }

    protected void addInvalidParser(String name, String structure, String message) {
        productions.add(new InvalidProduction(this, name, structure, message));
    }

    public void close() {
    }

//    @Override
//    public Token consume(String name, int chars) {
//        return Compiler.compiler().consume(this, name, chars);
//    }
//
//    @Override
//    public Token consume(String name, int chars, Object value) {
//        return Compiler.compiler().consume(this, name, chars, value);
//    }

    @Override
    public Token consume(String name, int chars, Object value, String type) {
        return Engine.engine().consume(this, name, chars, value, type);
    }

    @Override
    public void collect(String name, List<Production> list) {
        for (Production production : productions) {
            if (production.isNamed(name)) list.add(production);
        }
    }

    @Override
    public String getName() {
        if (names.isEmpty()) return getClass().getSimpleName();
        return names.get(0);
    }

    @Override
    public Plugin getPlugin() { return this; }

    private static final Class<?>[] matchedSig = new Class<?>[] {
        Node.class
    };

    @Override
    public void indexProductions() {
        Engine engine = Engine.engine();
        for (Production production : productions) {
            engine.index(production);
        }
    }

    @Override
    public Object invoke(String prefix, Node node) {
        Method method = lookup(prefix + node.getName());
        if (method == null) {
            method = lookup(prefix + node.getShortName());
        }
        if (method == null) return node;
        try {
            return method.invoke(this, node);
        } catch (NeoException ex) {
            throw ex;
        } catch (Exception ex) {
            while (ex.getCause() != null) ex = (Exception) ex.getCause();
            Log.error(ex);
            throw new NeoException(ex);
        }
    }

    private Method lookup(String name) {
        Method method = cache.get(name);
        if (method == null) {
            if (cache.containsKey(name)) return null;
            try {
                method = getClass().getMethod(name, matchedSig);
            } catch (NoSuchMethodException e) {
                try {
                    method = getClass().getDeclaredMethod(name, matchedSig);
                    if (method != null) {
                        method.setAccessible(true);
                    }
                } catch(NoSuchMethodException e2) {
                }
            }
            cache.put(name, method);
        }
        return method;
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
    public void prepare(Node node) {
        invoke("prepare_", node);
    }

    @Override
    public void open() {
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void refine(Node node) {
        invoke("refine_", node);
    }

    @Override
    public void render(Node node, String backend) {
        // Do nothing. Certain subclasses will need render their nodes.
    }

    @Override
    public Node transform(Node start) {
        Node node = (Node) invoke("transform_", start);
        if (node == null) throw new NullPointerException("transform for " + start + " must return a node");
        return node;
    }

}
