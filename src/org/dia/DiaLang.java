package org.dia;

import org.dia.core.Delimiter;
import org.dia.core.Compilation;
import org.dia.core.Expression;
import org.dia.core.Group;
import org.dia.core.Numbers;
import org.dia.core.Operator;
import org.dia.core.Range;
import org.dia.core.RegEx;
import org.dia.core.Strings;
import org.dia.core.Symbol;
import org.dia.core.Whitespace;
import org.dia.core.Xml;

/**
 *
 * @author Troy Heninger
 */
public class DiaLang extends Compiler {

    public DiaLang() {
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
