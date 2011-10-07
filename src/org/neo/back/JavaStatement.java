package org.neo.back;

import org.neo.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaStatement implements Backend {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.tab();
        node = node.getFirst();
        node.render("java");
        if (!buff.isAtBol())
            buff.append(';').eol();
    }

}
