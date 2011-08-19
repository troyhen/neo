package org.dia.parse;

import java.util.Stack;
import org.dia.Compiler;
import org.dia.Node;

class RuleIdent implements Rule {
    private final String ident;
    private final String identBase;

    RuleIdent(String id) {
        this.ident = id;
        this.identBase = id.startsWith("^") || id.startsWith("!") ? id.substring(1) : id;
    }

//    @Override
//    public Node match(boolean first) {
//        Compiler compiler = Compiler.compiler();
////        return compiler.parse(ident, first);
//        return null;
//    }

    @Override
    public int match(Stack<Node> stack, int start) {
        if (start >= stack.size()) return -1;
        if (stack.get(start).getName().startsWith(identBase)) return start + 1;
        return -1;
    }
}
