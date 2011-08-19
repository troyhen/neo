package org.dia;

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
import java.util.Stack;
import java.util.logging.Level;

import org.dia.core.Compilation;
import org.dia.lex.LexException;
import org.dia.lex.Token;
import org.dia.parse.Missing;
import org.dia.parse.ParseException;
import org.dia.parse.Production;

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
    private Map<String, List<Production>> parsers = new HashMap<String, List<Production>>();
//    private List<String> stack;
    private List<Token> tokens;
    
    public void add(String name, Production parser) {
        List<Production> list = parsers.get(name);
        if (list == null) {
            list = new ArrayList<Production>();
            parsers.put(name, list);
        }
        list.add(parser);
    }

    public static CharSequence buffer() { return compiler().buffer; }

    public CharSequence getBuffer() { return buffer; }
    
    public void close() {
        compiler.set(null);
        closed = true;
    }

    public static Compiler compiler() { return (Compiler) compiler.get(); }

    public Token consume(Plugin plugin, String name, int chars) {
        Token token = new Token(plugin, name, buffer.subSequence(0, chars), line);
        buffer.position(buffer.position() + chars);
        return token;
    }

    public Token consume(Plugin plugin, String name, int chars, Object value) {
        Token token = new Token(plugin, name, buffer.subSequence(0, chars), line, value);
        buffer.position(buffer.position() + chars);
        return token;
    }

    public CharSequence limit(CharSequence text, int limit) {
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
//        stack = new ArrayList<String>();
        tokens = new ArrayList<Token>();
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

    public Node parse(String goal) throws Missing, ParseException {
        Stack<Node> stack = new Stack<Node>();
        for (Token token : tokens) {
            stack.push(token);
            reduce(stack);
        }
        if (stack.size() != 1) throw new Missing(goal);
        return stack.pop();
    }
//        return parse("root", true);
//    }

//    public Node parse(String name, boolean first) throws Missing, ParseException {
//        boolean root = name.startsWith("^"),
//                ignore = name.startsWith("!");
//        if (root || ignore) name = name.substring(1);
//        Log.logger.log(Level.INFO, "parser: matching {0}", name);
//        Token token = tokens.get(offset);
//        if (token.getName().equals(name)) {// || node.getName().startsWith(name + '.')) {
//            Log.logger.log(Level.INFO, "parser: matched {0}", name);
//            offset++;
//            return token;
//        }
//        List<Parser> list = parsers.get(name);
//        if (list == null) {
//            if (literals.contains(name)) throw new Missing(name);
//            throw new ParseException("Invalid plugin grammar: can't find " + name);
//        }
//        if (first && stack.contains(name)) throw new Missing(name);  // don't allow left recursion
//        stack.add(name);
//        try {
//            int offset = this.offset;
//            for (Parser parser : list) {
//                try {
//                    Node child = parser.match(first);
//                    if (ignore) return null;
//                    Node parent = new Node(parser.plugin, parser.name, null, line);
//                    parent.addAll(child);
//                    return parent;
//                } catch (Missing e) {
//                    this.offset = offset;
//                }
//            }
//            throw new Missing(name);
//        } finally {
//            stack.remove(stack.size() - 1);
//        }
//    }

    public void reduce(Stack<Node> stack) {
    loop:
        for (;;) {
            for (Map.Entry<String, List<Production>> entry : parsers.entrySet()) {
                List<Production> list = entry.getValue();
                for (Production parser : list) {
                    for (int index = 0, end = stack.size(); index < end; index++) {
                        int last = parser.match(stack, index);
                        if (last == end) {
                            Node node = new Node(parser.plugin, parser.name, null, 0);
                            while (stack.size() > index) {
                                node.addFirst(stack.pop());
                            }
                            stack.push(node);
                            Log.logger.log(Level.INFO, "parser: matched {0} with {1}", new Object[] {parser.name, node.childNames()});
                            continue loop;
                        }
                    }
                }
            }
            return;
        }
    }

    public List<Token> tokenize() throws DiaException {
        if (closed) open();
        for(;;) {
            Token token = nextToken();
            tokens.add(token);
            if (token.getName() == Compilation.EOF) break;
        }
        return tokens;
    }

}
