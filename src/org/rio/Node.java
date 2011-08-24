package org.rio;

import java.util.List;

/**
 *
 * @author Troy Heninger
 */
public class Node {
    private final CharSequence text;
    private final Named named;
    private final boolean ignore;
    private final boolean root;
    private final boolean subsume;
    private final Object value;
    private Node parent;
    private Node first;
    private Node last;
    private Node next;
    private Node prev;

    public Node(Named named) {
        this(named, null, null);
    }
    
    public Node(Named named, CharSequence text) {
        this(named, text, null);
    }
    
    public Node(Named named, CharSequence text, Object value) {
        this.named = named;
        String name = named.getName();
        this.ignore = name.startsWith("!");
        this.root = name.startsWith("^");
        this.subsume = name.startsWith("@");
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
    public Node getLast() { return last; }

    public int getLine() {
        if (first != null) return first.getLine();
        return 0;
    }

    public String getName() {
        String name = named.getName();
        return ignore || root || subsume ? name.substring(1) : name;
    }
    
    public Node getNext() { return next; }
    public Node getParent() { return parent; }
    public Plugin getPlugin() { return named.getPlugin(); }
    public CharSequence getText() { return text; }
    public Object getValue() { return value; }
    public boolean isIgnored() { return ignore; }
    public boolean isRoot() { return root; }

    public void insertAfter(Node node) {
        if (node == null) throw new NullPointerException();
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
        node.prev = this.prev;
        if (this.prev != null) this.prev.next = node;
        node.parent = this.parent;
        node.next = this;
        this.prev = node;
        if (parent != null && parent.first == this) {
            parent.first = node;
        }
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
