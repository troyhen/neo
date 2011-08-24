package org.rio;

import org.rio.Compiler;
import org.rio.core.Delimiter;
import org.rio.core.Compilation;
import org.rio.core.Expression;
import org.rio.core.Group;
import org.rio.core.Numbers;
import org.rio.core.Operator;
import org.rio.core.Range;
import org.rio.core.RegEx;
import org.rio.core.Strings;
import org.rio.core.Symbol;
import org.rio.core.Whitespace;
import org.rio.core.Xml;

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
    }

}
