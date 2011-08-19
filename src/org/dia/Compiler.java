package org.dia;

import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.dia.lex.LexException;
import org.dia.lex.Lexer;
import org.dia.lex.LexerEof;
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
    private List<Production> grammar = new LinkedList<Production>();
    private List<Token> tokens;
    
    public void add(String name, Production parser) {
        int complexity = parser.complexity;
        int index = 0;
        for (Production prod : grammar) {
            if (prod.complexity < complexity) {
                grammar.add(index, parser);
                return;
            }
            index++;
        }
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

    public Node parse() throws Missing, ParseException {
        Stack<Node> stack = new Stack<Node>();
        for (Token token : tokens) {
            stack.push(token);
            reduce(stack);
        }
        if (stack.size() != 1) throw new ParseException("Syntax error");
        return stack.pop();
    }

    public void reduce(Stack<Node> stack) {
    loop:
        for (;;) {
                for (Production parser : grammar) {
                    for (int index = 0, end = stack.size(); index < end; index++) {
                        int last = parser.match(stack, index);
                        if (last == end) {
                            Node node = new Node(parser, null, 0);
                            while (stack.size() > index) {
                                node.addFirst(stack.pop());
                            }
                            stack.push(node);
                            Log.logger.info("parser: matched " + parser.name
                                    + " with " + node.childNames());
                            continue loop;
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
            if (token.getName().equals(LexerEof.EOF)) break;
        }
        return tokens;
    }

}
