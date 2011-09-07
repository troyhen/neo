package org.neo.core;

import org.neo.PluginBase;

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
//        add(new LexerString(this, "operator.dotStar", ".*"));
        addParser("importStatement", "!symbol.import symbol (operator.dot symbol)*");// operator.dotStar?");
    }

}
