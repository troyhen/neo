package org.neo.core;

import org.neo.lex.LexerKeyword;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.neo.Compiler;
import org.neo.lex.LexerPattern;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Symbol extends CorePlugin {

    public static final String SYMBOL = "symbol";

    @Override
    public Token consume(String name, int chars, Object value, String text) {
        text = Compiler.buffer().subSequence(0, chars).toString();
        return super.consume(name + '_' + text, chars, value, text);
    }
    
    @Override
    public void open() {
        super.open();
        names.add(SYMBOL);
//        add(new LexerPattern(this, "keyword", "(def|do|else|if|then|unless|until|return|var|val|while)"));
        add(new LexerKeyword(this, "[A-Za-z_$][A-Za-z0-9_$]*\\??"));
    }

}
