package org.neo.core;

import org.neo.Compiler;
import org.neo.Node;
import org.neo.lex.LexerPattern;
import org.neo.lex.LexerString;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Numbers extends CorePlugin {

    public static final String TRUE = "number.true";
    public static final String FALSE = "number.false";
    public static final String NULL = "number.null";
    public static final String LONG = "number.long";
    public static final String INTEGER = "number.integer";
    public static final String REAL = "number.real";
//    public static final String REAL_FORCED = "number.real.forced";

    @Override
    public Token consume(String name, int chars, Object value, String type) {
        String text = Compiler.chars(chars).toString();
        if (name.equals(TRUE) || name.equals(FALSE)) {
            value = text;
            type = "boolean";
        } else if (name.equals(NULL)) {
        } else if (name.equals(LONG)) {
            value = new Long(text.substring(0, text.length() - 1));
            type = "long";
        } else if (name.equals(INTEGER)) {
            value = new Integer(text);
            type = "int";
        } else {
                // Note, Java will ignore the trailing 'f'
            if (text.toLowerCase().endsWith("f")) {
                value = new Float(text);
                type = "float";
            } else {
                value = new Double(text);
                type = "double";
            }
        }
        return super.consume(name, chars, value, type);
    }

    @Override
    public void open() {
        super.open();
        names.add("number");
        add(new LexerString(this, TRUE, "true"));
        add(new LexerString(this, FALSE, "false"));
        add(new LexerString(this, NULL, "null"));
        add(new LexerPattern(this, REAL, "[0-9_]+\\.[0-9_]([eE]-?[0-9_]+)?[fF]?"));
        add(new LexerPattern(this, REAL, "[0-9_]+[fF]"));
        add(new LexerPattern(this, LONG, "[0-9_]+[lL]"));
        add(new LexerPattern(this, INTEGER, "[0-9_]+"));
        addParser("expression", "number");
    }

    @Override
    public Node transform(Node node) {
        String type = null;
        if (node.getName().equals("expression")) {
            type = node.getFirst().getType();
        }
        if (type != null) node.setType(type);
        return node;
    }

}