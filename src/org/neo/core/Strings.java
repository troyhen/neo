package org.neo.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.neo.Compiler;
import org.neo.Node;
import org.neo.lex.LexerPattern;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Strings extends CorePlugin {

    public static final String STRING_DOUBLE = "string.double";
//    public static final String STRING_DOUBLE_BAD = "string.double.bad";
//    public static final String STRING_DOUBLE_BAD2 = "string.double.bad2";
    public static final String STRING_MULTILINE = "string.multiline";
    public static final String STRING_SINGLE = "string.single";
//    public static final String STRING_SINGLE_BAD = "string.single.bad";
//    public static final String STRING_SINGLE_BAD2 = "string.single.bad2";
    public static final String STRING_WORD = "string.word";

    private static final Pattern expession = Pattern.compile("#\\{((\\\\}|[^}])*)}");
    
    @Override
    public void open() {
        super.open();
        names.add("string");
        add(new LexerPattern(this, STRING_WORD, "`([^ \\t\\r\\n,.;:#~\\[\\]\\(\\)\\{\\}']+)", 1));
        add(new LexerPattern(this, STRING_SINGLE, "'((\\\\'|[^'\\r\\n])*)'", 1));
//        add(new LexerPattern(this, STRING_SINGLE_BAD, "'((\\\\'|[^'\\r\\n])*)[\\r\\n]", 1));
//        add(new LexerPattern(this, STRING_SINGLE_BAD2, "'((\\\\'|[^'\\r\\n])*)$", 1));
            // multiline must come before double
        add(new LexerPattern(this, STRING_MULTILINE, "\"\"\"((\"\"[^\"]|\"[^\"]|[^\"])*)\"\"\"", 1));
        add(new LexerPattern(this, STRING_DOUBLE, "\"((\\\\\"|[^\"\\r\\n])*)\"", 1));
//        add(new LexerPattern(this, STRING_DOUBLE_BAD, "\"((\\\"|[^\"\\r\\n])*)[\\r\\n]", 1));
    }

    @Override
    public Token consume(String name, int chars, Object value, String type) {
        return super.consume(name, chars, value, "java.lang.String");
    }

    @Override
    public Node transform(Node node) {
        String name = node.getName();
        if (name.equals(STRING_DOUBLE) || name.equals(STRING_MULTILINE)) {
            final CharSequence text = (CharSequence) node.getValue();
            Matcher match = expession.matcher(text);
            int index = 0;
            while (match.find()) {
                String content = match.group(1);
                if (content.length() == 0) continue;
                if (match.start() > index) {
                    CharSequence between = text.subSequence(index, match.start());
                    Node string = new Node(this, STRING_SINGLE, between, between, "java.lang.String");
                    node.add(string);
                }
                Compiler current = Compiler.compiler();
                Compiler comp = current.subcompile("expression");
                comp.load(content);
                Node node1 = comp.transform();
                node.add(node1.getFirst().getFirst());
                comp.close(current);
                index = match.end();
            }
            int end = text.length();
            if (index > 0 && index < end) {
                CharSequence between = text.subSequence(index, end);
                Node string = new Node(this, STRING_SINGLE, between, between, "java.lang.String");
                node.add(string);
            }
        }
        return node;
    }

}
