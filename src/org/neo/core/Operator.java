package org.neo.core;

import org.neo.lex.LexerChar;
import org.neo.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class Operator extends PluginCore {
    
    public static final String OPERATOR_OTHER = "operator.other";
    public static final String OPERATOR_AS = "operator.as";
    public static final String OPERATOR_COMPARE = "operator.compare";
    public static final String OPERATOR_ADD = "operator.add";
    public static final String OPERATOR_ASSIGN = "operator.assign";
    public static final String OPERATOR_MUL = "operator.mul";
    public static final String OPERATOR_POW = "operator.pow";
    public static final String OPERATOR_DOT = "operator.dot";

    @Override
    public void open() {
        super.open();
        names.add("operator");
        add(new LexerChar(this, OPERATOR_AS, '~'));
        add(new LexerPattern(this, OPERATOR_COMPARE, "(<=>|<=|>=|<|>|!=|===|==|~=)"));
        add(new LexerPattern(this, OPERATOR_ASSIGN, "[-+~!@$%^&*/?:|]*="));
        add(new LexerPattern(this, OPERATOR_ADD, "[-+]"));
        add(new LexerPattern(this, OPERATOR_MUL, "[*/%]"));
        add(new LexerChar(this, OPERATOR_POW, '^'));
        add(new LexerChar(this, OPERATOR_DOT, '.'));
        add(new LexerPattern(this, OPERATOR_OTHER, "[-+~!@$%^&*/?:]+"));
    }

}
