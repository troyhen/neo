package org.neo.core;

/**
 *
 * @author Troy Heninger
 */
public class Import extends CorePlugin {

    public static final String NAME = "import";
    public static final String STATEMENT = "statement_import";

    @Override
    public void open() {
        super.open();
        names.add(NAME);
        addKeyword(NAME);
        addParser(STATEMENT, "!keyword_import (symbol operator_dot)* symbol");
    }

}
