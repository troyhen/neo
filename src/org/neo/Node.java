package org.neo;

import java.util.List;

/**
 *
 * @author Troy Heninger
 */
public class Node {
    
    public static final byte IGNORE = 1;
    public static final byte ROOT = 2;
    public static final byte SUBSUME = 4;
    
    private final CharSequence text;
    private final Named named;
    private final Object value;
    private Node parent;
    private Node first;
    private Node last;
    private Node next;
    private Node prev;
    private byte flags;

    public Node(Named named) {
        this(named, null, null);
    }
    
    public Node(Named named, CharSequence text) {
        this(named, text, null);
    }
    
    public Node(Named named, CharSequence text, Object value) {
        this.named = named;
        String name = named.getName();
        flags |= name.startsWith("!") ? IGNORE : 0; // lexers use this for ignored tokens
        this.value = value;
        this.text = text;
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
            Node next = child.next;
            add(child);
            child = next;
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
        if (first == null) return "(none)";
        StringBuilder buff = new StringBuilder();
        Node node = first;
        String comma = "";
        while (node != null) {
            buff.append(comma);
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
    public Node getLast() { return last; }

    public int getLine() {
        if (first != null) return first.getLine();
        return 0;
    }

    public String getName() { return named.getName(); }
    
    public Named getNamed() { return named; }
    public Node getNext() { return next; }
    public Node getParent() { return parent; }
    public Plugin getPlugin() { return named.getPlugin(); }
    public CharSequence getText() { return text; }
    public Object getValue() { return value; }
    public boolean isIgnored() { return (flags & IGNORE) != 0; }
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

    public Node prune() {
        Node result = next;
        if (isIgnored()) {
            unlink();
        } else if (isRoot()) {
            Node node = parent.first;
            while (node != null) {
                Node nextNode = node.next;
                if (node != this) {
                    this.add(node);
                }
                node = nextNode;
            }
        } else if (isSubsumed()) {
            Node node = first;
            while (node != null) {
                Node nextNode = node.next;
                insertAfter(node);
                node = nextNode;
            }
            unlink();
        }
        return result;
    }

    public static void revert(List<Node> matched, int size) {
        while (matched.size() > size) {
            matched.remove(matched.size() - 1);
        }
    }

    @Override
    public String toString() {
        return getName() + (text != null ? "(" + text + ')' : "");
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

}
