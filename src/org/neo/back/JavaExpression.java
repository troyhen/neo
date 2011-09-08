package org.neo.back;

import org.neo.Node;
import org.neo.NeoException;

/**
 *
 * @author Troy Heninger
 */
public class JavaExpression implements Backend {

    @Override
    public void render(Node node) {
//        CodeBuilder buff = JavaCompilation.output();
        throw new NeoException("Unexpected node: " + node + " (" + node.childNames() + ')');
    }

 }
