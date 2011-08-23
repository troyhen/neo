package org.dia.core;

import org.dia.Plugin;
import org.dia.lex.LexerChar;
import org.dia.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class Operator extends Plugin {
    
    public static final String OPERATOR = "operator";
    public static final String OPERATOR_ADD = "operator.add";
    public static final String OPERATOR_ASSIGN = "operator.assign";
    public static final String OPERATOR_MUL = "operator.mul";
    public static final String OPERATOR_POW = "operator.pow";

    @Override
    public void open() {
        super.open();
        names.add(OPERATOR);
        add(new LexerPattern(this, OPERATOR_ADD, "[-+]"));
        add(new LexerPattern(this, OPERATOR_MUL, "[*/%]"));
        add(new LexerChar(this, OPERATOR_POW, '^'));
        add(new LexerPattern(this, OPERATOR_ASSIGN, "[-+~!@#$%^&*/?:]*=[-+~!@#$%^&*/?:]*"));
        add(new LexerPattern(this, OPERATOR, "[-+~!@#$%^&*/?:]+"));
    }

}
