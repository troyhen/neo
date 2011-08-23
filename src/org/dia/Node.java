package org.dia;

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
        if (first == null) {
            first = last = child;
        } else {
            last.next = child;
            last = child;
        }
        child.parent = this;
        child.next = null;
    }

    public void addFirst(Node child) {
        if (child == null) throw new NullPointerException();
        if (first == null) {
            child.next = null;
            first = last = child;
        } else {
            child.next = first;
            first = child;
        }
        child.parent = this;
    }

    public void addAll(Node child) {
        if (child == null) throw new NullPointerException();
        while (child != null) {
            if (first == null) {
                first = last = child;
            } else {
                last.next = child;
                last = child;
            }
            child.parent = this;
            child = child.next;
        }
    }

    public void addNext(Node nextNode) {
        if (nextNode == null) throw new NullPointerException();
        nextNode.next = next;
        nextNode.parent = parent;
        next = nextNode;
        if (parent != null && parent.last == this) {
            parent.last = nextNode;
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

    @Override
    public String toString() {
        return getName() + (text != null ? "(" + text + ')' : "");
    }

}
