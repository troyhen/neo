package org.neo;

import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo.back.Backend;
import org.neo.lex.LexException;
import org.neo.lex.LexerEof;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Compiler {
    
    public static final String DEFAULT_BACKEND = "java";

    enum State { closed, opened, loaded, tokenized, parsed, pruned, rendered, error };

    private static ThreadLocal<Compiler> compiler = new ThreadLocal<Compiler>();

    public final List<Plugin> plugins = new ArrayList<Plugin>();
    public final Set<String> literals = new HashSet<String>();
    public final Map<String, Backend> backends = new HashMap<String, Backend>();

    private State state = State.closed;
    private Map<String, String> config = new HashMap<String, String>();
    private int line, offset;
    private CharBuffer buffer;
    private Node root;
    
    public static CharSequence buffer() { return compiler().buffer; }

    public CharSequence getBuffer() { return buffer; }
    
    public void close() {
        buffer = null;
        root = null;
        compiler.set(null);
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

    public static Compiler compiler() { return (Compiler) compiler.get(); }

    public Token consume(Named named, int chars) {
        Token token = new Token(named, buffer.subSequence(0, chars), line);
        buffer.position(buffer.position() + chars);
        return token;
    }

    public Token consume(Named named, int chars, Object value) {
        Token token = new Token(named, buffer.subSequence(0, chars), line, value);
        buffer.position(buffer.position() + chars);
        return token;
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

    public String set(String key, String value) { return config.put(key, value); }

    public CharSequence limit(CharSequence text, int limit) {
        limit = Math.min(text.length(), limit);
        int index = 0;
        for (; index < limit; index++) {
            char ch = text.charAt(index);
            if (ch == '\r' || ch == '\n') break;
        }
        return text.subSequence(0, index);
    }

    public int getLine() { return line; }

    public static int line() { return compiler().line; }

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
                Log.logger.severe(ex.toString());
                throw new NeoException(ex);
            }
        }
        state = State.loaded;
    }

    public void load(File file) throws IOException {
        if (state == State.closed) open();
        buffer = CharBuffer.allocate((int) file.length());
        FileReader inp = new FileReader(file);
        try {
            inp.read(buffer);
            buffer.flip();
        } finally {
            inp.close();
        }
        set("file", file.getPath());
    }

    public void load(String text) {
        if (state == State.closed) open();
        buffer = CharBuffer.wrap(text);
        set("text", text);
    }

    public static int offset() { return compiler().offset; }
    public static void offset(int offset) { compiler().offset = offset; }

    public void open() {
        compiler.set(this);
        for (Plugin plugin : plugins) {
            plugin.open();
        }
        line = 1;
        offset = 0;
        root = new Node(new Named() {

            Plugin plugin = new PluginBase();

            @Override
            public String getName() {
                return "root";
            }

            @Override
            public Plugin getPlugin() {
                return plugin;
            }

        });
        state = State.opened;
    }

    public Token nextToken() throws LexException {
    loop:
        for (;;) {
            for (Plugin plugin : plugins) {
                Token token = plugin.nextToken();
                if (token != null) {
                    line += token.countLines();
                    if (token.isIgnored()) continue loop;
                    return token;
                }
            }
            throw new LexException(String.format("Unrecognized input at line "
                    + line + " (" + limit(buffer, 80) + ')'));
        }
    }

    public Node prune() throws NeoException {
        if (state == State.closed) open();
        if (state == State.opened) load();
        if (state == State.loaded) tokenize();
        if (state == State.tokenized) parse();
        Node node = root.getFirst();
        prune(node);
        state = State.pruned;
        return root;
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
        if (state == State.closed) open();
        if (state == State.opened) load();
        if (state == State.loaded) tokenize();
        List<Node> matched = new ArrayList<Node>();
    loop:
        for (;;) {
            Node node = root.getFirst();
            Log.logger.info("current state: " + root.childNames());
            for (Plugin plugin : plugins) {
                Node next = plugin.match(node, matched);
                if (next != null) continue loop;
            }
            break;
        }
        state = State.parsed;
        Log.logger.info("parse complete with " + root.childNames());
        return root;
    }

    private void render() throws NeoException {
        if (state == State.closed) open();
        if (state == State.opened) load();
        if (state == State.loaded) tokenize();
        if (state == State.tokenized) parse();
        if (state == State.parsed) prune();
        String backend = get("backend", DEFAULT_BACKEND);
        Backend back = backends.get(backend);
        back.render(root.getFirst());
        state = State.rendered;
    }

    public Node tokenize() throws NeoException {
        if (state == State.closed) open();
        if (state == State.opened) load();
        for(;;) {
            Token token = nextToken();
            root.add(token);
            if (token.getName().equals(LexerEof.EOF)) break;
        }
        state = State.tokenized;
        return root;
    }

}
