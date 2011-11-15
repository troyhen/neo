package org.neo.parse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.neo.Plugin;
import org.neo.back.CodeBuilder;
import org.neo.core.Strings;

/**
 *
 * @author Troy Heninger
 */
public class Node {
    
    public static final byte IGNORE = 1;
    public static final byte ROOT = 2;
    public static final byte SUBSUME = 4;
    
    private final CharSequence text;
    private final Plugin plugin;
    private String name;
    private Object value;
    private String typeName;
    private Node parent;
    private Node first;
    private Node last;
    private Node next;
    private Node prev;
    private byte flags;
//    private int index;
    private HashSet<String> memo;

    public Node(Node copy) {
        this(copy.plugin, copy.name/*, copy.index*/, copy.text, copy.value, copy.typeName);
    }

    public Node(Plugin plugin, String name/*, int index*/) {
        this(plugin, name/*, index*/, null, null, null);
    }
    
    public Node(Plugin plugin, String name/*, int index*/, CharSequence text) {
        this(plugin, name/*, index*/, text, null, null);
    }
    
    public Node(Plugin plugin, String name/*, int index*/, CharSequence text, Object value) {
        this(plugin, name/*, index*/, text, value, null);
    }

    public Node(Plugin plugin, String name/*, int index*/, CharSequence text, Object value, String typeName) {
        this.plugin = plugin;
        this.name = name;
        flags |= name.startsWith("!") ? IGNORE : 0; // lexers use this for ignored tokens
        this.value = value;
        this.text = text;
        this.typeName = typeName;
//        this.index = index;
    }

    public void add(Node child) {
        if (child == null) throw new NullPointerException();
        child.unlink();
        if (this.last == null) {
            this.first = this.last = child;
        } else {
            child.prev = last;
            this.last.next = child;
            this.last = child;
        }
        child.parent = this;
    }

    public void addFirst(Node child) {
        if (child == null) throw new NullPointerException();
        child.unlink();
        if (this.first == null) {
            this.first = this.last = child;
        } else {
            child.next = this.first;
            this.first.prev = child;
            this.first = child;
        }
        child.parent = this;
    }

    public void addAll(Node child) {
        if (child == null) throw new NullPointerException();
        while (child != null) {
            Node nextNode = child.next;
            add(child);
            child = nextNode;
        }
    }

    public void append(Node node) {
        if (node == null) throw new NullPointerException();
        Node lastNode;
        if (parent != null) {
            lastNode = parent.last;
            parent.last = node;
        } else {
            lastNode = this;
            while (lastNode.next != null) {
                lastNode = lastNode.next;
            }
        }
        lastNode.next = node;
        node.parent = parent;
    }

    public String childNames() {
        return childNames(Integer.MAX_VALUE);
    }
    
    public String childNames(int max) {
        if (first == null) return "(none)";
        StringBuilder buff = new StringBuilder();
        Node node = first;
        String comma = "";
        while (node != null && max-- > 0) {
            buff.append(comma);
            buff.append(node.getLine());
            buff.append(':');
            buff.append(node);
            comma = " ";
            node = node.next;
        }
        return buff.toString();
    }

    public int countChildren() {
        int count = 0;
        Node node = first;
        while (node != null) {
            count++;
            node = node.next;
        }
        return count;
    }

    public void failed(String name) {
        if (memo == null) memo = new HashSet<String>();
        memo.add(name);
//        Engine.engine().memo(index, name, node);
    }

    public Node get(int index) {
        Node node = getFirst();
        while (index-- > 0 && node != null) {
            node = node.next;
        }
        return node;
    }

    public Node getFirst() { return first; }
    public byte getFlags() { return flags; }
    public void setFlags(byte flags) { this.flags = flags; }
//    public int getIndex() { return index; }
    public Node getLast() { return last; }

    public int getLine() {
        if (first != null) return first.getLine();
        return 0;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Node getNext() { return next; }
    public Node getNextOrLast() {
        if (next == null)
            return this;
        return next == null ? this : next; }

//    public Node getNextWrapped() {
//        if (next == null) {
//            return parent.first;
//        }
//        return next;
//    }

    public Node getParent() { return parent; }
    public Plugin getPlugin() { return plugin; }
    public Node getPrev() { return prev; }
    public Node getPrevOrFirst() { return prev == null ? this : prev; }

//    public Node getPrevWrapped() {
//        if (prev == null) {
//            return parent.last;
//        }
//        return prev;
//    }

    public String getShortName() {
        int ix = name.indexOf('_');
        if (ix > 0) {
            return name.substring(0, ix);
        }
        return name;
    }

    public CharSequence getText() { return text; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public Object getValue() { return value; }

    public boolean hasFailed(String name) {
        if (memo == null) return false;
        return memo.contains(name);
//        return Engine.engine().memo(index, name);
    }

//    public boolean hasMemo(String name) {
////        if (memo == null) return false;
////        return memo.containsKey(name);
//        return Engine.engine().memoExists(index, name);
//    }
    
    public boolean isIgnored() { return (flags & IGNORE) != 0; }

    public boolean isNamed(String name) {
        return this.name.equals(name) || this.name.startsWith(name + '_');
    }

    public boolean isRoot() { return (flags & ROOT) != 0; }
    public boolean isSubsumed() { return (flags & SUBSUME) != 0; }

    public void insertAfter(Node node) {
        if (node == null) throw new NullPointerException();
        node.unlink();
        node.next = this.next;
        if (this.next != null) this.next.prev = node;
        node.parent = this.parent;
        node.prev = this;
        this.next = node;
        if (parent != null && parent.last == this) {
            parent.last = node;
        }
    }

    public void insertBefore(Node node) {
        if (node == null) throw new NullPointerException();
        node.unlink();
        node.prev = this.prev;
        if (this.prev != null) this.prev.next = node;
        node.parent = this.parent;
        node.next = this;
        this.prev = node;
        if (parent != null && parent.first == this) {
            parent.first = node;
        }
    }

    public Match newMatch(byte flags) {
        return new Match(flags);
    }

    public void prepare() {
        memo = null;  // clean up from parsing
        plugin.prepare(this);
    }

    public Node prune() {
        Node result = next;
        if (isIgnored()) {
            unlink();
        } else if (isRoot()) {
//            Node node = parent.first;
//            while (node != null) {
//                Node nextNode = node.next;
//                if (node != this) {
//                    this.add(node);
//                }
//                node = nextNode;
//            }
            while (prev != null) {
                addFirst(prev);
            }
            while (next != null) {
                add(next);
            }
        } else if (isSubsumed()) {
            while (last != null) {
                insertAfter(last);
            }
            unlink();
        }
        return result;
    }

    public void render(String backend) {
        plugin.render(this, backend);
    }

    public void replaceWithChildren() {
        while (first != null) {
            insertBefore(first);
        }
        unlink();
    }

    public static void revert(List<Match> matched, int size) {
        while (matched.size() > size) {
            Match match = matched.remove(matched.size() - 1);
//            match.node().replaceWithChildren();
        }
    }

    public String toListTree() {
        final CodeBuilder buff = new CodeBuilder();
        toListTree(this, buff);
        return buff.toString();
    }

    protected static void toListTree(Node node, CodeBuilder buff) {
        while (node != null) {
            toTree(node, buff);
            node = node.next;
        }
    }

    public String toTree() {
        final CodeBuilder buff = new CodeBuilder();
        toTree(this, buff);
        return buff.toString();
    }

    protected static void toTree(Node node, CodeBuilder buff) {
        buff.tab().append(node).eol();
        if (node.first != null) {
            buff.tabMore();
            toListTree(node.first, buff);
            buff.tabLess();
        }
    }

    public Node transform() {
        return plugin.transform(this);
    }

    @Override
    public String toString() {
        CodeBuilder buff = new CodeBuilder(getName());
        if (value != null) {
            buff.append('(');
            buff.append(Strings.encode(String.valueOf(value)));
            buff.append(')');
        } else if (text != null) {
            buff.append('[');
            buff.append(Strings.encode(text));
            buff.append(']');
        }
        if (typeName != null) {
            buff.append('~');
            buff.append(typeName);
        }
        return buff.toString();
    }

    public void unlink() {
        if (parent != null) {
            if (parent.first == this) parent.first = this.next;
            if (parent.last == this) parent.last = this.prev;
        }
        if (this.prev != null) this.prev.next = this.next;
        if (this.next != null) this.next.prev = this.prev;
        this.prev = this.next = this.parent = null;
    }

    public void refine() {
        plugin.refine(this);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public class Match {
        private byte flags;

        public Match(byte flags) {
            this.flags = flags;
        }

        public byte getFlags() {
            return flags;
        }

        public Node node() {
            Node.this.setFlags(flags);
            return Node.this;
        }

        public Node getFirst() {
            return Node.this.getFirst();
        }

        @Override
        public String toString() {
            StringBuilder buff = new StringBuilder();
            if ((flags & IGNORE) != 0) buff.append('!');
            if ((flags & ROOT) != 0) buff.append('^');
            if ((flags & SUBSUME) != 0) buff.append('@');
            buff.append(Node.this.toString());
            return buff.toString();
        }

    }

}
