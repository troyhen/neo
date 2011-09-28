package org.neo.core;

import org.neo.lex.LexerKeyword;
import org.neo.ClassDef;
import org.neo.Compiler;
import org.neo.MethodDef;
import org.neo.Node;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Symbol extends CorePlugin {

    public static final String SYMBOL = "symbol";

    @Override
    public Token consume(String name, int chars, Object value, String type) {
        String text = Compiler.buffer().subSequence(0, chars).toString();
        return super.consume(name + '_' + text, chars, text, type);
    }
    
    @Override
    public void open() {
        super.open();
        names.add(SYMBOL);
//        add(new LexerPattern(this, "keyword", "(def|do|else|if|then|unless|until|return|var|val|while)"));
        add(new LexerKeyword(this, "[A-Za-z_$][A-Za-z0-9_$]*\\??"));
    }

    public Node transform_symbol(Node node) {
        String typeName = node.getTypeName();
        Node parent = node.getParent();
        if (typeName == null && !parent.isNamed("statement_def") && !parent.isNamed("operator_as") 
                && !parent.isNamed("call_this") && !parent.isNamed("statement_import")
                && !parent.isNamed("statement_valAssign") && !parent.isNamed("statement_varAssign")
                && ((!parent.isNamed("call_dot") && !parent.isNamed("reference_dot")) || node == parent.getFirst())) {
            final String name = node.getValue().toString();
            MethodDef method = Compiler.compiler().methodFind(name);
            if (method != null) {
                typeName = method.getReturnType().getName();
                Node call = new Node(this, "call_this", null, method, typeName);
                node.insertBefore(call);
                call.add(node);
                node = call;
            } else {
                ClassDef type = Compiler.compiler().symbolFind(name);
                if (type != null) typeName = type.getName();
            }
        }
        if (typeName != null) node.setTypeName(typeName);
        return node;
    }
}
