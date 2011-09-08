package org.neo;

import org.neo.core.Delimiter;
import org.neo.core.Compilation;
import org.neo.core.Expression;
import org.neo.core.Group;
import org.neo.core.Import;
import org.neo.core.Numbers;
import org.neo.core.Operator;
import org.neo.core.Range;
import org.neo.core.RegEx;
import org.neo.core.Strings;
import org.neo.core.Symbol;
import org.neo.core.Whitespace;
import org.neo.core.Xml;

/**
 *
 * @author Troy Heninger
 */
public class NeoLang extends Compiler {
    
    private final Compilation main;

    public NeoLang() {
        plugins.add(new Whitespace());
        plugins.add(new Delimiter());
        plugins.add(new Import());  // must come before Expression
        plugins.add(new Expression());
        plugins.add(new Group());
        plugins.add(new Symbol());
        plugins.add(new Range());
        plugins.add(new Operator());
        plugins.add(new Numbers());
        plugins.add(new Strings());
        plugins.add(new RegEx());
        plugins.add(new Xml());
        plugins.add(main = new Compilation()); // must be last
    }

    @Override
    public void open() {
        super.open();
        setRoot(new Node(main, "compilation"));
    }

}
