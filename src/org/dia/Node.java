package org.dia;

/**
 *
 * @author Troy Heninger
 */
public class Node {
    private final CharSequence text;
    private final int line;
    private final Plugin plugin;
    private final String name;
    private final boolean ignore;
    private final boolean root;
    private final Object value;
    private Node parent;
    private Node first;
    private Node last;
    private Node next;

    public Node(Plugin plugin, String name, CharSequence text, int line) {
        this(plugin, name, text, line, null);
    }
    
    public Node(Plugin plugin, String name, CharSequence text, int line, Object value) {
        this.plugin = plugin;
        this.ignore = name.startsWith("!");
        this.root = name.startsWith("^");
        this.name = ignore || root ? name.substring(1) : name;
        this.value = value;
        this.text = text;
        this.line = line;
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

    public void append(Node lastNode) {
        if (lastNode == null) throw new NullPointerException();
        Node last;
        if (parent != null) {
            last = parent.last;
            parent.last = lastNode;
        } else {
            last = this;
            while (last.next != null) {
                last = last.next;
            }
        }
        last.next = lastNode;
        lastNode.parent = parent;
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
    public String getName() { return name; }
    public Node getNext() { return next; }
    public Node getParent() { return parent; }
    public Plugin getPlugin() { return plugin; }
    public CharSequence getText() { return text; }
    public Object getValue() { return value; }
    public boolean isIgnored() { return ignore; }
    public boolean isRoot() { return root; }

    @Override
    public String toString() {
        return name + (text != null ? "(" + text + ')' : "");
    }

}
