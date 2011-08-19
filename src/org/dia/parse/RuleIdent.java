package org.dia.parse;

import java.util.Stack;
import org.dia.Compiler;
import org.dia.Node;

class RuleIdent implements Rule {
    private final String ident;
    private final String identBase;
    private final String identPlus;

    RuleIdent(String id) {
        this.ident = id;
        this.identBase = id.startsWith("^") || id.startsWith("!") ? id.substring(1) : id;
        this.identPlus = this.identBase + '.';
    }

    @Override
    public int complexity() {
        return 1;
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
        final String name = stack.get(start).getName();
        if (name.equals(identBase) || name.startsWith(identPlus)) {
            return start + 1;
        }
        return -1;
    }
}
