package org.neo;

import org.neo.Compiler;
import org.neo.back.JavaCompilation;
import org.neo.core.Delimiter;
import org.neo.core.Compilation;
import org.neo.core.Expression;
import org.neo.core.Group;
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
public class RioLang extends Compiler {

    public RioLang() {
        plugins.add(new Compilation());
        plugins.add(new Whitespace());
        plugins.add(new Delimiter());
        plugins.add(new Group());
        plugins.add(new Range());
        plugins.add(new Operator());
        plugins.add(new Numbers());
        plugins.add(new Strings());
        plugins.add(new Symbol());
        plugins.add(new RegEx());
        plugins.add(new Xml());
        plugins.add(new Expression());
        backends.put(DEFAULT_BACKEND, new JavaCompilation());
    }

}
