package org.neo.back;

import org.neo.parse.Node;

/**
 *
 * @author Troy Heninger
 */
public class JavaRegex extends JavaString {

    @Override
    public void render(Node node) {
        CodeBuilder buff = JavaCompilation.output();
        buff.append("java.util.regex.Pattern.compile(");
        super.render(node);
        buff.append(")");
    }

}
