package org.neo.core;

import org.neo.parse.Node;
import org.neo.lex.LexerString;
import org.neo.parse.ParseException;

/**
 *
 * @author Troy Heninger
 */
public class Range extends CorePlugin {

    public static final String NAME = "range";
    public static final String RANGE_INCLUSIVE = "range_inclusive";
    public static final String RANGE_EXCLUSIVE = "range_exclusive";
    
    @Override
    public void open() {
        super.open();
        names.add(NAME);
        add(new LexerString(this, RANGE_EXCLUSIVE, "..."));
        add(new LexerString(this, RANGE_INCLUSIVE, ".."));
        addParser("expression_range", "@expression_root ^range @expression_root");
    }

    public Node transform_range(Node node) {
        String type = "neo.lang.Range";
        final String type1 = node.get(0).getTypeName();
        final String type2 = node.get(1).getTypeName();
        if (!type1.equals(type2)) {
            throw new ParseException("Range limit types do not match: " + type1 + " and " + type2);
        }
        if (type != null) node.setTypeName(type);
        return node;
    }

}
