package org.neo.parse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.*;

import neo.lang.N;

import org.neo.Compiler;
import org.neo.Plugin;
import org.neo.lex.LexException;
import org.neo.lex.Token;
import org.neo.util.ClassDef;
import org.neo.util.Log;
import org.neo.util.MemberDef;
import org.neo.util.MethodDef;

/**
 *
 * @author Troy Heninger
 */
public class Engine {
    
    private static ThreadLocal<Engine> engine = new ThreadLocal<Engine>();

    public final Set<String> literals = new HashSet<String>();

    private CharBuffer buffer;
    private final Compiler compiler;
    private ClassDef currentClass;
    private List<String> imports = new ArrayList<String>();
    private Map<String, List<Production>> index = new HashMap<String, List<Production>>();
    private Token lastToken, nextToken;
    private int line, offset;
    private Set<String> keywords = new HashSet<String>();
    private LinkedList<Map<String, ClassDef>> symbols = new LinkedList<Map<String, ClassDef>>();
    private LinkedList<Map<String, List<MethodDef>>> methods = new LinkedList<Map<String, List<MethodDef>>>();
    private State initial;
    private String start;
    private Deque<Position> productionStack;
//    private int tokenIndex = 0;
    private Map<String, Set<String>> starts = new HashMap<String, Set<String>>();

    public Engine(Compiler compiler) { this.compiler = compiler; }

    public void addKeyword(String word) { keywords.add(word); }

    public static CharSequence buffer() { return engine().buffer; }

    public static CharSequence chars(int chars) {
        return engine().getChars(chars);
    }

    public void close() { close(null); }

    public void close(Engine previous) {
        buffer = null;
        engine.set(previous);
    }

    public static Compiler compiler() { return engine().compiler; }

    public Token consume(Plugin plugin, String name, int chars, Object value, String type) {
        CharSequence text = getChars(chars);
        Token token = new Token(plugin, name/*, tokenIndex++*/, text, line, value == null ? text : value, type);
        buffer.position(buffer.position() + chars);
        return token;
    }

    public static ClassDef currentClass() { return engine().getCurrentClass(); }

    public static Engine engine() { return (Engine) engine.get(); }

    public List<Production> findProductions(String name) {
        List<Production> list = new ArrayList<Production>();
        for (Plugin plugin : Engine.compiler().plugins) {
            plugin.collect(name, list);
        }
        return list;
    }

    public CharSequence getBuffer() { return buffer; }

    public CharSequence getChars(int chars) {
        chars = Math.min(chars, buffer.length());
        return buffer.subSequence(0, chars);
    }

    public ClassDef getCurrentClass() { return currentClass; }

    public Token getLastToken() { return lastToken; }

    public int getLine() { return line; }

//    public Progress getProgress(Production production, int index) {
//        Progress temp = new Progress(production, index);
//        Progress progress = steps.get(temp);
//        return progress;
//    }

//    public State getState(Progress progress, boolean addIfMissing) {
//        State state = progress.getState();
//        if (state == null) {
//            state = steps.get(progress);
//            if (state == null && addIfMissing) {
//                state = new State(progress);
//            }
//        }
//        return state;
//    }

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

    public void index(Production production) {
        Set<String> list = production.findStarts();
        String name = production.getName();
        Set<String> list0 = starts.get(name);
        if (list0 == null) starts.put(name, list);
        else list0.addAll(list);
        String name1 = production.getShortName();
        if (name.equals(name1)) return;
        list0 = starts.get(name1);
        if (list0 == null) starts.put(name1, list);
        else list0.addAll(list);
//        for (String name : list) {
//            List<Production> plist = index.get(name);
//            if (plist == null) {
//                plist = new ArrayList<Production>();
//                index.put(name, plist);
//            }
//            plist.add(production);
//        }
    }

//    public void index(Progress progress) {
//        /*State state = progress.getState();
//        if (state == null) steps.remove(progress);
//        else*/ steps.put(progress, progress);
//    }

    public void indexPlugins() {
        for (Plugin plugin : compiler().plugins) {
            plugin.indexProductions();
        }
        List<String> found = new ArrayList<String>();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Map.Entry<String, Set<String>> set : starts.entrySet()) {
                final Set<String> value = set.getValue();
                for (String ident : value) {
                    final Set<String> more = starts.get(ident);
                    if (more != null) {
                        found.addAll(more);
                    }
                }
                changed |= value.addAll(found);
                found.clear();
            }
        }
    }

    public boolean isKeyword(String text) { return keywords.contains(text); }

    public CharSequence limit(CharSequence text, int limit) {
        limit = Math.min(text.length(), limit);
        int ix = 0;
        for (; ix < limit; ix++) {
            char ch = text.charAt(ix);
            if (ch == '\r' || ch == '\n') break;
        }
        return text.subSequence(0, ix);
    }

    public static int line() { return engine().line; }

    public String load(File file) throws IOException {
        buffer = CharBuffer.allocate((int) file.length());
        FileReader inp = new FileReader(file);
        try {
            inp.read(buffer);
            buffer.flip();
        } finally {
            inp.close();
        }
        return file.getPath();
    }

    public void load(String text) { buffer = CharBuffer.wrap(text); }

    public Class loadClass(String name) throws ClassNotFoundException {
        /*if (name.startsWith("java."))*/ return Class.forName(name);
//        return loader.loadClass(name);
    }

    public MemberDef memberFind(ClassDef type, String symbol, String assignmentType) {
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

//    public boolean memoExists(int index, String name) {
//        if (memo.size() <= index) return false;
//        return memo.get(index).containsKey(name);
//    }
//
//    public boolean memo(int index, String name) {
//        if (memo.size() <= index) return false;
//        return memo.get(index).get(name);
//    }
//
//    public void memo(int index, String name, boolean recursive) {
//        while (memo.size() <= index) {
//            memo.add(new HashMap<String, Boolean>());
//        }
//        Map<String, Boolean> at = memo.get(index);
////        if (node != null) {
////            Iterator<Map.Entry<String, Node>> it = at.entrySet().iterator();
////            while (it.hasNext()) {
////                Map.Entry<String, Node> entry = it.next();
////                if (entry.getValue() != null) {
////                    entry.setValue(null);
////                    break;
////                }
////            }
////        }
//        at.put(name, recursive);
//    }

    public void methodAdd(String name, MethodDef type) {
        Map<String, List<MethodDef>> map = methods.peek();
        List<MethodDef> meths = map.get(name);
        if (meths == null) {
            meths = new ArrayList<MethodDef>();
            map.put(name, meths);
        }
        meths.add(type);
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

    public Token nextToken() throws LexException {
        if (nextToken != null) {
            Token temp = nextToken;
            nextToken = null;
            return temp;
        }
    loop:
        for (;;) {
            for (Plugin plugin : compiler.plugins) {
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

//    public int nextTokenIndex() {
//        return tokenIndex++;
//    }

    public Set<String> getStarts(String name) { return starts.get(name); }

    private static class Match {
        Production production;
        Node node;
        List<Node.Match> matched = new ArrayList<Node.Match>();

        Match(Production production, Node node, List<Node.Match> matched) {
            this.production = production;
            this.node = node;
            this.matched.addAll(matched);
        }

        boolean isWorse(List<Node.Match> matched) {
            return this.matched.size() < matched.size();
        }

        Node reduce() {
            final Node parent = node.getParent();
            if (parent.isNamed(production.getName()))
                return parent;
            return production.reduce(node, matched);
        }

        @Override
        public String toString() { return "matched " + production.getName(); }

        public boolean isReduced() {
            return node.isNamed(production.getName()) && matched.size() == 1;
        }
    }

    public static int offset() { return engine().offset; }
    public static void offset(int offset) { engine().offset = offset; }

    public void open() {
        engine.set(this);
//        tokenIndex = 0;
        line = 1;
        offset = 0;
        symbolsPush();
    }

    /**
     * Setup
     * Create a production stack. Each level of the stack holds multiple production possibilities.
     *
     * Algorithm
     * When a production name in encountered, find all possible productions not already on the stack and push
     * them as a group to the next level of the stack. Try to match productions from the top group of the stack
     * to the current node. Compare the matches and select the longest match. Reduce the match and pop the top
     * production group. If no matches were found, report an error and stop.
     *
     * @param root starting node
     */
    public void parse(Node root) {
        productionStack = new ArrayDeque<Position>();
        Node top = parse(root.getFirst(), start);
Log.info(root.getFirst().toListTree());
        if (top == null) throw new ParseException("Invalid program", root);
    }

    public Node parse(Node from, String name) {
        final Position position = new Position(name, from);
        if (productionStack.contains(position)) {
//            memo(from.getIndex(), name, true);
            throw new Mismatch(from, name);
        }
//        if (memoExists(from.getIndex(), name) && !memo(from.getIndex(), name)) {
        if (from.hasFailed(name)) {
//            if (from.getParent() != null && from.getParent().getParent() != null) {
//                Log.info(from.getParent().getParent() + " -> " + from.getParent() + " -> " + from);
//            }
            throw new Mismatch(from, name);
        }
        final Set<String> startNames = starts.get(name);
        if (startNames != null && !startNames.contains(from.getName()) && !startNames.contains(from.getShortName())) {
//            memo(from.getIndex(), name, false);
            from.failed(name);
            throw new Mismatch(from, name);
        }
        List<Production> list = findProductions(name);
        if (list.isEmpty()) {
//            memo(from.getIndex(), name, false);
            from.failed(name);
            throw new Mismatch(from, name);
        }
        Node newNode = null;
        productionStack.push(position);
        try {
            Node root = from.getParent();
            Mismatch lastError = null;
            List<Node.Match> matched = new ArrayList<Node.Match>();
            for (;;) {
                Match bestMatch = null;
                for (Production production : list) {
                    matched.clear();
                    try {
                        production.parse(from, matched);
                    } catch (Mismatch e) {
                        lastError = e;
                        continue;
                    } finally {
                        while (from.getParent() != root) {
                            from = from.getParent();
                        }
                    }
                    if (bestMatch == null || bestMatch.isWorse(matched)) {
                        bestMatch = new Match(production, from, matched);
                    }
                }
                if (bestMatch == null && newNode == null) {
//                    memo(from.getIndex(), name, false);
                    throw lastError;
                }
                if (newNode != null && (bestMatch == null || bestMatch.isReduced()))
                    break;
                newNode = bestMatch.reduce();
                fixStack();
                from = newNode;
            }
        } finally {
            productionStack.pop();
        }
        return newNode;
    }

    private void fixStack() {
        for (Position position : productionStack) {
            position.raise();
        }
    }

    public void setNextToken(Token nextToken) { this.nextToken = nextToken; }

    public void setStart(String start) {
        engine.set(this);
        this.start = start;
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

    private class Position {
        String name;
//        int index;
        Node node;
        boolean recursive;

        private Position(String name, Node node/*int index*/) {
            this.name = name;
//            this.index = index;
            this.node = node;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Position)) return false;
            final Position obj1 = (Position) obj;
            return this.name.equals(obj1.name) && this.node == obj1.node;
        }

        @Override
        public int hashCode() { return name.hashCode() + node.hashCode(); }

        public void raise() {
            while (node.getParent() != null && node.getParent().getParent() != null) {
                node = node.getParent();
            }
        }

        @Override
        public String toString() { return name + '@' + index; }
    }
}
