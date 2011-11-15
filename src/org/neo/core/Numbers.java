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
        } else if (name.equals(LONG + "Binary")) {
            value = Long.parseLong(text.substring(2, text.length() - 1), 2);
            type = "long";
        } else if (name.equals(LONG + "Octal")) {
            value = Long.parseLong(text.substring(1, text.length() - 1), 8);
            type = "long";
        } else if (name.equals(LONG + "Hex")) {
            value = Long.parseLong(text.substring(2, text.length() - 1), 16);
            type = "long";
        } else if (name.equals(INTEGER)) {
            value = new Integer(text);
            type = "int";
        } else if (name.equals(INTEGER + "Binary")) {
            value = Integer.parseInt(text.substring(2, text.length()), 2);
            type = "long";
        } else if (name.equals(INTEGER + "Octal")) {
            value = Integer.parseInt(text.substring(1, text.length()), 8);
            type = "long";
        } else if (name.equals(INTEGER + "Hex")) {
            value = Integer.parseInt(text.substring(2, text.length()), 16);
            type = "long";
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
        add(new LexerPattern(this, LONG + "Octal", "0[0-7_]+[lL]"));
        add(new LexerPattern(this, LONG + "Hex", "0x[0-9_]+[lL]"));
        add(new LexerPattern(this, LONG + "Binary", "0b[01_]+[lL]"));
        add(new LexerPattern(this, LONG, "[0-9_]+[lL]"));
        add(new LexerPattern(this, INTEGER + "Octal", "0[0-7_]+"));
        add(new LexerPattern(this, INTEGER + "Hex", "0x[0-9_]+"));
        add(new LexerPattern(this, INTEGER + "Binary", "0b[01_]+"));
        add(new LexerPattern(this, INTEGER, "[0-9_]+"));
        addParser("expression0", "number");
    }

    public Node transform_expression(Node node) {
        String type = node.getFirst().getTypeName();
        if (type != null) node.setTypeName(type);
        return node;
    }

}