package org.neo.core;

import org.neo.PluginBase;
import org.neo.lex.LexerPattern;

/**
 *
 * @author Troy Heninger
 */
public class RegEx extends PluginBase {

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
