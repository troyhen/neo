package org.neo.core;

import org.neo.lex.LexerString;

/**
 *
 * @author Troy Heninger
 */
public class Range extends CorePlugin {

    public static final String NAME = "range";
    public static final String RANGE_INCLUSIVE = "range.inclusive";
    public static final String RANGE_EXCLUSIVE = "range.exclusive";
    
    @Override
    public void open() {
        super.open();
        names.add(NAME);
        add(new LexerString(this, RANGE_INCLUSIVE, "..."));
        add(new LexerString(this, RANGE_EXCLUSIVE, ".."));
    }

}
