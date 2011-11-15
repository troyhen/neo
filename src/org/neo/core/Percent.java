package org.neo.core;

import org.neo.parse.Node;
import org.neo.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class Percent extends CorePlugin {

    @Override
    public void open() {
        super.open();
        names.add("percent");

        add(new LexerPattern(this, "percent_word", "%w\\(([^)]*)\\)", 1));
        add(new LexerPattern(this, "percent_word", "%w\\[([^]]*)\\]", 1));
        add(new LexerPattern(this, "percent_word", "%w\\{([^}]*)\\}", 1));
        add(new LexerPattern(this, "percent_word", "%w<([^>]*)>", 1));
        add(new LexerPattern(this, "percent_word", "%w\"([^\"]*)\"", 1));
        add(new LexerPattern(this, "percent_word", "%w'([^']*)'", 1));
        add(new LexerPattern(this, "percent_word", "%w`([^`]*)`", 1));
        add(new LexerPattern(this, "percent_word", "%w/([^/]*)/", 1));
        add(new LexerPattern(this, "percent_word", "%w\\|([^|]*)\\|", 1));
        addParser("expression0", "percent");
    }

    public Node transform_percent_word(Node node) {
        node.setName("array_word");
        node.setTypeName("java.lang.String[]");
        String text = (String) node.getValue();
        for (String part : text.split("\\s")) {
            if (part.length() > 0) {
                Node n = new Node(this, "string_single"/*, node.getIndex()*/, part, part, "java.lang.String");
                node.add(n);
            }
        }
        return node;
    }
    
}
