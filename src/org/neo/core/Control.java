package org.neo.core;

import org.neo.ClassDef;
import org.neo.Compiler;
import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class Control extends CorePlugin {

    private int index = 1;

    @Override
    public void open() {
        addKeyword("if");
        addKeyword("do");
        addKeyword("else");
        addKeyword("end");
        addKeyword("then");
        addKeyword("unless");
        addKeyword("until");
        addKeyword("while");

        addParser("expression", "control");

        addParser("elseClause", "!keyword_else statement");
        addParser("elseClause", "!keyword_else !terminator @block");

        addParser("control_if",     "expression- < !keyword_if     @expression !keyword_then  statement          elseClause? (!keyword_end !keyword_if?)?");
        addParser("control_if",     "expression- < !keyword_if     @expression !keyword_then? !terminator @block elseClause? (!keyword_end !keyword_if?)?");
        addParser("control_unless", "expression- < !keyword_unless @expression !keyword_then  statement          elseClause? (!keyword_end !keyword_unless?)?");
        addParser("control_unless", "expression- < !keyword_unless @expression !keyword_then? !terminator @block elseClause? (!keyword_end !keyword_unless?)?");
        addParser("control_while",  "expression- < !keyword_while  @expression !keyword_do    statement          elseClause? (!keyword_end !keyword_while?)?");
        addParser("control_while",  "expression- < !keyword_while  @expression !keyword_do?   !terminator @block elseClause? (!keyword_end !keyword_while?)?");
        addParser("control_until",  "expression- < !keyword_until  @expression !keyword_do    statement          elseClause? (!keyword_end !keyword_until?)?");
        addParser("control_until",  "expression- < !keyword_until  @expression !keyword_do?   !terminator @block elseClause? (!keyword_end !keyword_until?)?");

        addParser("statement_if",     "statement_def- statement !keyword_if     @expression");
        addParser("statement_unless", "statement_def- statement !keyword_unless @expression");
        addParser("statement_while",  "statement_def- statement !keyword_while  @expression");
        addParser("statement_until",  "statement_def- statement !keyword_until  @expression");
    }

    public Node transform_control(Node node) {
        String typeName = Expression.commonType(node.get(1));
        if (typeName != null) node.setTypeName(typeName);
        Node parent = node.getParent();
        if (parent.isNamed("expression")) {
            parent.setTypeName(typeName);
        }
        Node grandParent = parent.getParent();
        if (parent.isNamed("expression") || grandParent.isNamed("control") || grandParent.isNamed("elseClause")) {
            Node statement = parent;
            while (!statement.isNamed("statement")) {
                statement = statement.getParent();
            }
            Node leg1 = node.get(1);
            Node leg2 = node.get(2).getLast();
            if (leg2.isNamed("statements")) {
                leg2 = leg2.getLast();
            }
            Node varStatement = new Node(this, "statement_varDeclare", null, null, typeName);
            String varName = "control" + index++;
            Node symbol1 = new Node(this, "symbol", varName, varName);
            Node symbol2 = new Node(this, "symbol", varName, varName);
            Node symbol3 = new Node(this, "symbol", varName, varName);
            Node ifStatement = new Node(this, "statement", null, null, typeName);
            Node eq1 = new Node(this, "operator_eq", "=", "=", typeName);
            Compiler.compiler().symbolAdd(varName, ClassDef.get(typeName));
            statement.insertBefore(varStatement);
            varStatement.add(symbol1);
            statement.insertBefore(ifStatement);
            node.insertBefore(symbol2);
            ifStatement.add(node);
            leg1.addFirst(eq1);
            eq1.add(symbol3);
            eq1.add(eq1.getNext());
            if (leg2 != null) {
                Node eq2 = new Node(this, "operator_eq", "=", "=", typeName);
                Node symbol4 = new Node(this, "symbol", varName, varName);
                leg2.addFirst(eq2);
                eq2.add(symbol4);
                eq2.add(eq2.getNext());
            }
            if (grandParent.isNamed("control") || grandParent.isNamed("elseClause")) {
                Node statements = new Node(this, "statements", null, null, typeName);
                statements.addAll(grandParent.getFirst());
                grandParent.add(statements);
            }
            node = symbol2;
        }
        return node;
    }

    public Node transform_elseClause(Node node) {
        return transform_statement(node);
    }

    public Node transform_expression(Node node) {
        return transform_statement(node);
    }

    public Node transform_statement(Node node) {
        String type = node.getFirst().getTypeName();
        if (type != null) node.setTypeName(type);
        return node;
    }

}
