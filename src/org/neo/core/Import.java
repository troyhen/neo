package org.neo.core;

/**
 *
 * @author Troy Heninger
 */
public class Import extends CorePlugin {

    public static final String NAME = "import";
    public static final String STATEMENT = "statement.import";

    @Override
    public void open() {
        super.open();
        names.add(NAME);
        addParser(STATEMENT, "!symbol.import (symbol operator.dot)* symbol");
    }

}
