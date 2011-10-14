package org.neo.core;

import org.neo.parse.Node;
import org.neo.lex.LexerPattern;
import org.neo.lex.LexerString;
import org.neo.lex.Token;
import org.neo.parse.Engine;

/**
 *
 * @author Troy Heninger
 */
public class Numbers extends CorePlugin {

    public static final String TRUE = "number_true";
    public static final String FALSE = "number_false";
    public static final String NULL = "number_null";
    public static final String LONG = "number_long";
    public static final String INTEGER = "number_integer";
    public static final String REAL = "number_real";
//    public static final String REAL_FORCED = "number_real_forced";

    @Override
    public Token consume(String name, int chars, Object value, String type) {
        String text = Engine.chars(chars).toString();
        if (name.equals(TRUE) || name.equals(FALSE)) {
            value = text;
            type = "boolean";
        } else if (name.equals(NULL)) {
        } else if (name.equals(LONG)) {
                // Note, Java won't ignore the trailing 'l' here
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

    public Node transform_expression(Node node) {
        String type = node.getFirst().getTypeName();
        if (type != null) node.setTypeName(type);
        return node;
    }

}