package org.neo.core;

import org.neo.PluginBase;
import org.neo.lex.LexerPattern;
import org.neo.lex.LexerString;

/**
 *
 * @author Troy Heninger
 */
public class Import extends PluginBase {

    public static final String NAME = "import";

    @Override
    public void open() {
        super.open();
        names.add(NAME);
//        add(new LexerPattern(this, NAME, "symbol{import}", 1));
        add(new LexerString(this, "operator.dotStar", ".*"));
        addParser("importStatement", "!symbol.import symbol (operator.dot symbol)* operator.dotStar?");
    }

}
