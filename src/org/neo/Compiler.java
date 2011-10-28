package org.neo;

import org.neo.util.Log;
import org.neo.parse.Node;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo.lex.LexerEof;
import org.neo.lex.Token;
import org.neo.parse.Engine;
//import org.neo.tool.NeoClassLoader;

/**
 *
 * @author Troy Heninger
 */
public class Compiler {
    
    public static final String DEFAULT_BACKEND = "java";

    enum State { closed, opened, loaded, tokenized, parsed, pruned, transformed, refined, rendered, error };

    public final List<Plugin> plugins = new ArrayList<Plugin>();

    private Map<String, String> config = new HashMap<String, String>();
    private Engine engine;
    private Node root;
    private State state = State.closed;

    public void close(Compiler previous) {
        root = null;
        engine.close(previous != null ? previous.engine : null);
        state = State.closed;
    }

    public void compile() throws NeoException {
        render();
    }

    public void compile(File file) throws NeoException {
        set("file", file.getPath());
        compile();
    }

    public void compile(File file, String backend) throws NeoException {
        set("file", file.getPath());
        set("backend", backend);
        compile();
    }

    public void compile(String text) throws NeoException {
        set("text", text);
        compile();
    }
    
    public void compile(Map<String, String> options) throws NeoException {
        config.putAll(options);
        compile();
    }

    public static Compiler compiler() {
        return Engine.compiler();
    }

    public static File file() {
        String fileName = compiler().get("file");
        if (fileName == null) return null;
        File file = new File(fileName);
        return file;
    }

    public String get(String key) { return config.get(key); }

    private String get(String key, String defValue) {
        if (config.containsKey(key)) return config.get(key);
        return defValue;
    }

    public Node getRoot() { return root; }

    public void load() throws NeoException {
        String text = get("text");
        if (text != null) load(text);
        else {
            String fileName = get("file");
            if (fileName == null) throw new NeoException("Missing source file");
            File file = new File(fileName);
            try {
                load(file);
            } catch (IOException ex) {
                Log.error(ex);
                throw new NeoException(ex);
            }
        }
        state = State.loaded;
    }

    public void load(File file) throws IOException {
        switch (state) {
            case closed: open();
        }
        set("file", engine.load(file));
    }

    public void load(String text) {
        switch (state) {
            case closed: open();
        }
        engine.load(text);
        set("text", text);
    }

    public void open() {
//        loader = NeoClassLoader.getInstance();
//        Thread.currentThread().setContextClassLoader(loader);
        engine = new Engine(this);
        engine.open();
        for (Plugin plugin : plugins) {
            plugin.open();
        }
        state = State.opened;
    }

    public Node prune() throws NeoException {
        switch (state) {
            case closed: open();
            case opened: load();
            case loaded: tokenize();
            case tokenized: parse();
        }
        prune(root);
        state = State.pruned;
        return root.getFirst();
    }

    private void prune(Node node) throws NeoException {
        while (node != null) {
            if (node.getFirst() != null) {
                prune(node.getFirst());
            }
            node = node.prune();
        }
    }

    public Node parse() throws NeoException {
        switch (state) {
            case closed: open();
            case opened: load();
            case loaded: tokenize();
        }
        Log.info("start: " + root.childNames());
        engine.parseLL(root);
        state = State.parsed;
        Log.info("end: " + root.childNames());
        return root.getFirst();
    }

    private void render() throws NeoException {
        switch (state) {
            case closed: open();
            case opened: load();
            case loaded: tokenize();
            case tokenized: parse();
            case parsed: prune();
            case pruned: transform();
            case transformed: refine();
        }
        String backend = get("backend", DEFAULT_BACKEND);
        root.getFirst().render(backend);
        state = State.rendered;
    }

    public Node refine() throws NeoException {
        switch (state) {
            case closed: open();
            case opened: load();
            case loaded: tokenize();
            case tokenized: parse();
            case parsed: prune();
            case pruned: transform();
        }
        refine(root);
        state = State.refined;
        return root.getFirst();
    }

    private void refine(Node node) throws NeoException {
        while (node != null) {
            node.refine();
            if (node.getFirst() != null) {
                refine(node.getFirst());
            }
            node = node.getNext();
        }
    }

    public String set(String key, String value) { return config.put(key, value); }
    protected void setRoot(Node root) { this.root = root; }

    protected void setStart(String start) {
        engine.setStart(start);
    }

    public Compiler subcompile(String string) {
        return new Compiler();
    }

    public Node tokenize() throws NeoException {
        switch (state) {
            case closed: open();
            case opened: load();
        }
        for(;;) {
            Token token = engine.nextToken();
            root.add(token);
            if (token.isNamed(LexerEof.EOF)) break;
        }
        state = State.tokenized;
        return root;
    }

    public String toTree() {
        return root.getFirst().toTree();
    }

    public Node transform() throws NeoException {
        switch (state) {
            case closed: open();
            case opened: load();
            case loaded: tokenize();
            case tokenized: parse();
            case parsed: prune();
        }
        engine.importPackage("java.lang");
        engine.importPackage("neo.lang");
        transform(root);
        state = State.transformed;
        return root.getFirst();
    }

    private void transform(Node start) throws NeoException {
        Node node = start;
        while (node != null) {
            node.prepare();
            if (node.getFirst() != null) {
                transform(node.getFirst());
            }
            node = node.transform();
            node = node.getNext();
        }
    }

}
