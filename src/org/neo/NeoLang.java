package org.neo;

import org.neo.core.Classes;
import org.neo.core.Delimiter;
import org.neo.core.Compilation;
import org.neo.core.Control;
import org.neo.core.CorePlugin;
import org.neo.core.Expression;
import org.neo.core.Group;
import org.neo.core.Import;
import org.neo.core.Methods;
import org.neo.core.Numbers;
import org.neo.core.Operator;
import org.neo.core.Percent;
import org.neo.core.Range;
import org.neo.core.RegEx;
import org.neo.core.Strings;
import org.neo.core.Symbol;
import org.neo.core.Variable;
import org.neo.core.Whitespace;
import org.neo.core.Xml;

/**
 *
 * @author Troy Heninger
 */
public class NeoLang extends Compiler {
    
    private CorePlugin main;

    public NeoLang() {
        this("statements");
    }
    
    public NeoLang(String top) {
        boolean full = "statements".equals(top);
        plugins.add(new Whitespace());
        plugins.add(new Delimiter());
        if (full) plugins.add(new Import());  // must precede Expression
        plugins.add(new Numbers()); // must precede Expression
        plugins.add(new Strings());
        plugins.add(new RegEx());
        if (full) plugins.add(new Variable()); // must precede Expression
        if (full) plugins.add(new Methods());  // must precede Expression and follow Variable
        plugins.add(new Range()); // must precede Expression
        plugins.add(new Xml());  // must precede Operator
        plugins.add(new Percent()); // must precede Operator
        plugins.add(new Operator());
        plugins.add(new Group());
        plugins.add(new Symbol());
        plugins.add(new Control());
        plugins.add(new Classes());
        plugins.add(main = new Expression());
        if (full) plugins.add(main = new Compilation()); // must be last
    }

    @Override
    public void open() {
        super.open();
        setRoot(new Node(main, "compilation"));
    }

    @Override
    public Compiler subcompile(String top) {
        return new NeoLang(top);
    }

}
