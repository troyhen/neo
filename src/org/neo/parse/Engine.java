package org.neo.parse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

import org.neo.Compiler;
import org.neo.Plugin;
import org.neo.lex.LexException;
import org.neo.lex.Token;
import org.neo.util.ClassDef;
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
    private Map<Progress, Progress> steps = new HashMap<Progress, Progress>();
    private List<State> states = new ArrayList<State>();

    public Engine(Compiler compiler) {
        this.compiler = compiler;
    }

    public int add(State state) {
        states.add(state);
        return states.size();
    }

    public void addKeyword(String word) {
        keywords.add(word);
    }

    public static CharSequence buffer() { return engine().buffer; }

    public static CharSequence chars(int chars) {
        return engine().getChars(chars);
    }

    public void close() {
        close(null);
    }

    public void close(Engine previous) {
        buffer = null;
        engine.set(previous);
    }

    public static Compiler compiler() {
        return engine().compiler;
    }

    public Token consume(Plugin plugin, String name, int chars, Object value, String type) {
        CharSequence text = getChars(chars);
        Token token = new Token(plugin, name, text, line, value == null ? text : value, type);
        buffer.position(buffer.position() + chars);
        return token;
    }

    public static ClassDef currentClass() {
        return engine().getCurrentClass();
    }

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

    public ClassDef getCurrentClass() {
        return currentClass;
    }

    public Token getLastToken() {
        return lastToken;
    }

    public int getLine() { return line; }

    public Progress getProgress(Production production, int index) {
        return getProgress(production, index, null);
    }

    public Progress getProgress(Production production, int index, State state) {
        Progress temp = new Progress(production, index, null);
        Progress progress = steps.get(temp);
        if (progress == null) {
            steps.put(progress = temp, temp);
            progress.setState(state);
        }
        return progress;
    }

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

    public boolean hasProgress(Production production, int index) {
        Progress temp = new Progress(production, index, null);
        Progress progress = steps.get(temp);
        return progress != null;
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

    public void index(Production production) {
        List<String> list = production.findStarts();
        for (String name : list) {
            List<Production> plist = index.get(name);
            if (plist == null) {
                plist = new ArrayList<Production>();
                index.put(name, plist);
            }
            plist.add(production);
        }
    }

    public void index(Progress progress) {
        /*State state = progress.getState();
        if (state == null) steps.remove(progress);
        else*/ steps.put(progress, progress);
    }

    public boolean isKeyword(String text) {
        return keywords.contains(text);
    }

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

    public void load(String text) {
        buffer = CharBuffer.wrap(text);
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        /*if (name.startsWith("java."))*/ return Class.forName(name);
//        return loader.loadClass(name);
    }

    private Match matchProductions(String name, Node node, List<Node.Match> matched, Match bestMatch) {
        List<Production> list = index.get(name);
        if (list == null) return bestMatch;
        for (Production production : list) {
            Node next = production.match(node, matched);
            if (next == null) {
                continue;
            }
            if (bestMatch == null || bestMatch.isWorse(matched)) {
                bestMatch = new Match(production, node, matched);
            }
        }
        return bestMatch;
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

    public void merge(State state, State other) {
        if (state == other) return;
        for (Progress progress : steps.keySet()) {
            progress.replace(other, state);
        }
        for (State state1 : states) {
            state1.replace(other, state);
        }
        states.remove(other);
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

        void reduce() {
            production.reduce(node, matched);
        }

        @Override
        public String toString() {
            return "matched " + production.getName();
        }

    }

    public static int offset() { return engine().offset; }
    public static void offset(int offset) { engine().offset = offset; }

    public void open() {
        engine.set(this);
        line = 1;
        offset = 0;
        symbolsPush();
    }

    public void parseAll(Node root) {
        List<Node.Match> matched = new ArrayList<Node.Match>();
        Node node = root.getFirst();
        for (;;) {
            Match bestMatch = null;
            while (node != null) {
                bestMatch = matchProductions("*", node, matched, bestMatch);
                String name = node.getName();
                bestMatch = matchProductions(name, node, matched, bestMatch);
                int ix = name.lastIndexOf('_');
                if (ix > 0) {
                    name = name.substring(0, ix + 1);
                    bestMatch = matchProductions(name, node, matched, bestMatch);
                }
                node = node.getNext();
            }
            if (bestMatch == null) break;
            bestMatch.reduce();
//            Log.info("current state: " + root.childNames(10));
            node = root.getFirst();
        }
    }

    public void parseFirst(Node root) {
        List<Node.Match> matched = new ArrayList<Node.Match>();
        Node node = root.getFirst();
        while (node != null) {
            Match bestMatch = null;
            bestMatch = matchProductions("*", node, matched, bestMatch);
            String name = node.getName();
            bestMatch = matchProductions(name, node, matched, bestMatch);
            int ix = name.lastIndexOf('_');
            if (ix > 0) {
                name = name.substring(0, ix + 1);
                bestMatch = matchProductions(name, node, matched, bestMatch);
            }
            if (bestMatch == null) {
                node = node.getNext();
                continue;
            }
            bestMatch.reduce();
            node = root.getFirst();
        }
    }

    public void parseOld(Node root) {
        List<Node.Match> matched = new ArrayList<Node.Match>();
        Node node = root.getFirst();
        for (;;) {
            Match bestMatch = null;
            while (node != null) {
                String name = node.getName();
                bestMatch = matchProductions(name, node, matched, bestMatch);
                if (bestMatch != null) break;
                int ix = name.lastIndexOf('_');
                if (ix > 0) {
                    name = name.substring(0, ix + 1);
                    bestMatch = matchProductions(name, node, matched, bestMatch);
                    if (bestMatch != null) break;
                }
                bestMatch = matchProductions("*", node, matched, bestMatch);
                if (bestMatch != null) break;
                node = node.getNext();
            }
            if (bestMatch == null) break;
            bestMatch.reduce();
//            Log.info("current state: " + root.childNames(10));
            node = root.getFirst();
        }
    }

    public void setNextToken(Token nextToken) {
        this.nextToken = nextToken;
    }

    public void setStart(String start) {
        engine.set(this);
        List<Production> list = findProductions(start);
        for (Production production : list) {
//            if (initial == null) initial = new State(production);
            Progress progress = new Progress(production, 0, null);
            if (initial == null) initial = progress.getState();
            progress.explore();
        }
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

}
