package org.neo.core;

import org.neo.Node;
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
        add(new LexerString(this, RANGE_INCLUSIVE, "..."));
        add(new LexerString(this, RANGE_EXCLUSIVE, ".."));
        addParser("expression_range", "@expression ^range @expression");
    }

    @Override
    public Node transform(Node node) {
        String type = "neo.lang.Range";
        final String type1 = node.get(0).getType();
        final String type2 = node.get(1).getType();
        if (!type1.equals(type2)) {
            throw new ParseException("Range limit types do not match: " + type1 + " and " + type2);
        }
        if (type != null) node.setType(type);
        return node;
    }

}
