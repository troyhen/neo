package org.dia.core;

import org.dia.Plugin;
import org.dia.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class RegEx extends Plugin {

    public static final String NAME = "regex";
    public static final String ABREV = "re";

    @Override
    public void open() {
        super.open();
        names.add(NAME);
        names.add(ABREV);
        add(new LexerPattern(this, NAME, "/((\\[(\\\\\\]|[^\\]])+|\\/|[^/])+)/[iop]?", 1));
    }

}
