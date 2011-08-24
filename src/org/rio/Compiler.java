package org.rio;

import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rio.lex.LexException;
import org.rio.lex.Lexer;
import org.rio.lex.LexerEof;
import org.rio.lex.Token;
import org.rio.parse.Production;

/**
 *
 * @author Troy Heninger
 */
public abstract class Compiler {

    private static ThreadLocal<Compiler> compiler = new ThreadLocal<Compiler>();

    public final List<Plugin> plugins = new ArrayList<Plugin>();
    public final Set<String> literals = new HashSet<String>();

    private int line, offset;
    private CharBuffer buffer;
    private boolean closed = true;
    private List<Production> grammar = new ArrayList<Production>();//LinkedList<Production>();
    private Node root;
    
    public void add(Production parser) {
        grammar.add(parser);
    }

    public static CharSequence buffer() { return compiler().buffer; }

    public CharSequence getBuffer() { return buffer; }
    
    public void close() {
        compiler.set(null);
        closed = true;
    }

    public static Compiler compiler() { return (Compiler) compiler.get(); }

    public Token consume(Lexer lexer, int chars) {
        Token token = new Token(lexer, buffer.subSequence(0, chars), line);
        buffer.position(buffer.position() + chars);
        return token;
    }

    public Token consume(Lexer lexer, int chars, Object value) {
        Token token = new Token(lexer, buffer.subSequence(0, chars), line, value);
        buffer.position(buffer.position() + chars);
        return token;
    }

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
    
    public void load(File file) throws IOException {
        buffer = CharBuffer.allocate((int) file.length());
        FileReader inp = new FileReader(file);
        try {
            inp.read(buffer);
        } finally {
            inp.close();
        }
    }

    public void load(String text) {
        buffer = CharBuffer.wrap(text);
    }

    public static int offset() { return compiler().offset; }
    public static void offset(int offset) { compiler().offset = offset; }

    public void open() {
        compiler.set(this);
        for (Plugin plugin : plugins) {
            plugin.open();
        }
        closed = false;
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

    public Node prune() throws RioException {
        if (closed) {
            open();
            tokenize();
            parse();
        }
        Node node = root.getFirst();
        prune(node);
        return root;
    }

    private void prune(Node node) {
        while (node != null) {
            if (node.getFirst() != null) {
                prune(node.getFirst());
            }
            node = node.prune();
        }
    }

    public Node parse() throws RioException {
        if (closed) {
            open();
            tokenize();
        }
        List<Node> matched = new ArrayList<Node>();
        for (;;) {
            boolean changed = false;
            for (Production production : grammar) {
                Node node = root.getFirst();
                while (node != null) {
                    Node next = production.match(node, matched);
                    if (next != null) {
                        Log.logger.info("parser: matched " + production.name
                                + " with " + node.getParent().childNames());
                        changed = true;
                        node = next;
                    } else {
                        node = node.getNext();
                    }
                }
            }
            if (!changed) break;
        }
        return root;
    }

    public Node tokenize() throws RioException {
        if (closed) open();
        for(;;) {
            Token token = nextToken();
            root.add(token);
            if (token.getName().equals(LexerEof.EOF)) break;
        }
        return root;
    }

}
