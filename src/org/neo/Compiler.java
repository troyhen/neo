package org.neo;

import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import neo.lang.N;

import org.neo.lex.LexException;
import org.neo.lex.LexerEof;
import org.neo.lex.Token;
//import org.neo.tool.NeoClassLoader;

/**
 *
 * @author Troy Heninger
 */
public class Compiler {
    
    public static final String DEFAULT_BACKEND = "java";

    enum State { closed, opened, loaded, tokenized, parsed, pruned, transformed, validated, rendered, error };

    private static ThreadLocal<Compiler> compiler = new ThreadLocal<Compiler>();

    public final List<Plugin> plugins = new ArrayList<Plugin>();
    public final Set<String> literals = new HashSet<String>();

    private final LinkedList<Map<String, ClassDef>> symbols = new LinkedList<Map<String, ClassDef>>();
    private final LinkedList<Map<String, List<MethodDef>>> methods = new LinkedList<Map<String, List<MethodDef>>>();

    private ClassLoader loader;
    private List<String> imports = new ArrayList<String>();
    private State state = State.closed;
    private Map<String, String> config = new HashMap<String, String>();
    private int line, offset;
    private CharBuffer buffer;
    private Node root;
    public Token lastToken; // needed by LexerIndent
    private Set<String> keywords = new HashSet<String>();
    private ClassDef currentClass;

    public void addKeyword(String word) {
        keywords.add(word);
    }
    
    public static CharSequence buffer() { return compiler().buffer; }

    public CharSequence getBuffer() { return buffer; }
    
    public static Object chars(int chars) {
        return compiler.get().getChars(chars);
    }

    public void close() {
        close(null);
    }

    public void close(Compiler previous) {
        buffer = null;
        root = null;
        compiler.set(previous);
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

//    public Token consume(Plugin plugin, String name, int chars) {
//        Token token = new Token(plugin, name, buffer.subSequence(0, chars), line);
//        buffer.position(buffer.position() + chars);
//        return token;
//    }
//
//    public Token consume(Plugin plugin, String name, int chars, Object value) {
//        Token token = new Token(plugin, name, buffer.subSequence(0, chars), line, value);
//        buffer.position(buffer.position() + chars);
//        return token;
//    }

    public Token consume(Plugin plugin, String name, int chars, Object value, String type) {
        CharSequence text = getChars(chars);
        Token token = new Token(plugin, name, text, line, value == null ? text : value, type);
        buffer.position(buffer.position() + chars);
        return token;
    }

    public static ClassDef currentClass() {
        return compiler().getCurrentClass();
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

    public CharSequence getChars(int chars) {
        chars = Math.min(chars, buffer.length());
        return buffer.subSequence(0, chars);
    }

    public ClassDef getCurrentClass() {
        return currentClass;
    }

    public void importPackage(String path) {
        if (!imports.contains(path)) imports.add(path);
//        ClassDef[] classes;
//        try {
//            classes = PackageTool.getClasses(path);
//        } catch (NeoException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            Log.error(ex);
//            throw new NeoException(ex);
//        }
//        for (Class type : classes) {
//            symbolAdd(type.getSimpleName(), ClassDef.get(type));
//        }
//        for (ClassDef type : classes) {
        ClassDef type = ClassDef.get(path + ".N");
        if (type == null) return;
            String name = type.getSimpleName();
            symbolAdd(name, type);
            if ("N".equals(name)) {
                MethodDef[] meths = type.getMethods();
                for (MethodDef method : meths) {
                    if (method.isStatic() && method.isPublic()) {
                        String mname = method.getName();
                        methodAdd(mname, method);
                    }
                }
            }
//        }
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

    public boolean isKeyword(String text) {
        return keywords.contains(text);
    }

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
        switch (state) {
            case closed: open();
        }
        buffer = CharBuffer.wrap(text);
        set("text", text);
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        /*if (name.startsWith("java."))*/ return Class.forName(name);
//        return loader.loadClass(name);
    }

    public static int offset() { return compiler().offset; }
    public static void offset(int offset) { compiler().offset = offset; }

    public void open() {
//        loader = NeoClassLoader.getInstance();
//        Thread.currentThread().setContextClassLoader(loader);
        compiler.set(this);
        for (Plugin plugin : plugins) {
            plugin.open();
        }
        line = 1;
        offset = 0;
        symbolsPush();
        state = State.opened;
    }

    public Token nextToken() throws LexException {
    loop:
        for (;;) {
            for (Plugin plugin : plugins) {
                Token token = plugin.nextToken();
                if (token != null) {
                    lastToken = token;
                    line += token.countLines();
                    if (token.isIgnored()) continue loop;
                    return token;
                }
            }
            throw new LexException(String.format("Unrecognized input at line "
                    + line + " (" + limit(buffer, 80) + ')'));
        }
    }

    public void methodAdd(String name, MethodDef type) {
        Map<String, List<MethodDef>> map = methods.peek();
        List<MethodDef> meths = map.get(name);
        if (meths == null) {
            meths = new ArrayList<MethodDef>();
            map.put(name, meths);
        }
        meths.add(type);
    }

    public MethodDef methodFind(ClassDef type, String name, String...argTypes) {
        String[] argTypes2 = new String[argTypes.length + 1];
        argTypes2[0] = type.getName();
        System.arraycopy(argTypes, 0, argTypes2, 1, argTypes.length);
        MethodDef method = methodFind(name, argTypes2);
        if (method == null) {
            method = type.findMethod(name, argTypes);
        }
        return method;
    }

    public MethodDef methodFind(String name, String...argTypes) {
        ListIterator<Map<String, List<MethodDef>>> it = methods.listIterator(methods.size());
        while (it.hasPrevious()) {
            Map<String, List<MethodDef>> map = it.previous();
            List<MethodDef> meths = map.get(name);
            if (meths != null) {
                for (MethodDef method : meths) {
                    if (method.isCallableWith(argTypes)) {
                        return method;
                    }
                }
            }
        }
        return null;
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
        root.render(backend);
        state = State.rendered;
    }

    public Node parse() throws NeoException {
        switch (state) {
            case closed: open();
            case opened: load();
            case loaded: tokenize();
        }
        List<Node.Match> matched = new ArrayList<Node.Match>();
    loop:
        for (;;) {
            Node node = root.getFirst();
            Log.info("current state: " + root.childNames());
            for (Plugin plugin : plugins) {
                Node next = plugin.match(node, matched);
                if (next != null) continue loop;
            }
            break;
        }
        state = State.parsed;
        Log.info("parse complete with " + root.childNames());
        return root;
    }

    public String toTree() {
        return root.toTree();
    }

    public MemberDef memberFind(ClassDef type, String symbol) {
        MemberDef reference = null;
        reference = methodFind(type, symbol);
        if (reference == null) {
            reference = methodFind(type, "get" + N.capitalize(symbol));
        }
        if (reference == null) {
            reference = type.findField(symbol);
        }
        return reference;
    }

    public MemberDef referenceFind(ClassDef type, String symbol, String assignmentType) {
        MemberDef reference = null;
        reference = methodFind(type, symbol, assignmentType);
        if (reference == null) {
            reference = methodFind(type, "set" + N.capitalize(symbol), assignmentType);
        }
        if (reference == null) {
            reference = type.findField(symbol);
        }
        return reference;
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
        state = State.validated;
        return root;
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

    public Node getRoot() { return root; }
    public void setRoot(Node root) { this.root = root; }

    public Compiler subcompile(String string) {
        return new Compiler();
    }

    public void symbolAdd(String name, ClassDef type) {
        Map<String, ClassDef> map = symbols.peek();
        map.put(name, type);
    }

    public ClassDef symbolFind(String name) {
        ListIterator<Map<String, ClassDef>> it = symbols.listIterator(symbols.size());
        while (it.hasPrevious()) {
            Map<String, ClassDef> map = it.previous();
            ClassDef type = map.get(name);
            if (type != null)
                return type;
        }
        for (String importPath : imports) {
            ClassDef type = ClassDef.get(importPath + '.' + name);
            if (type != null)
                return type;
        }
        return null;
    }

    public void symbolsPop() {
        symbols.pop();
        methods.pop();
    }

    public void symbolsPush() {
        symbols.push(new HashMap<String, ClassDef>());
        methods.push(new HashMap<String, List<MethodDef>>());
    }

    public Node tokenize() throws NeoException {
        switch (state) {
            case closed: open();
            case opened: load();
        }
        for(;;) {
            Token token = nextToken();
            root.add(token);
            if (token.isNamed(LexerEof.EOF)) break;
        }
        state = State.tokenized;
        return root;
    }

    public Node transform() throws NeoException {
        switch (state) {
            case closed: open();
            case opened: load();
            case loaded: tokenize();
            case tokenized: parse();
            case parsed: prune();
        }
        importPackage("java.lang");
        importPackage("neo.lang");
        transform(root);
        state = State.transformed;
        return root;
    }

    private void transform(Node node) throws NeoException {
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
