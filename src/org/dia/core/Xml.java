package org.dia.core;

import java.util.regex.Pattern;

import org.dia.Compiler;
import org.dia.Plugin;
import org.dia.lex.LexerPattern;
import org.dia.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Xml extends Plugin {
    
    public static final Pattern xmlStart = Pattern.compile("^<([_A-Za-z][-:_A-Za-z0-9]*)[^>]*>");
    public static final Pattern xmlEnd = Pattern.compile("</([_A-Za-z][-:_A-Za-z0-9]*)[ \\t\\r\\n]*>");
    public static final String NAME = "xml";
    public static final String ABREV = "x";

    @Override
    public void open() {
        names.add(NAME);
        names.add(ABREV);
        add(new LexerPattern(this, NAME, "/((\\[(\\\\\\]|[^\\]])+|\\/|[^/])+)/[iop]?", 1));
    }

    @Override
    public Token nextToken() {
        CharSequence buffer = Compiler.buffer();
        java.util.regex.Matcher matcher = xmlStart.matcher(buffer);
        if (matcher.find()) {
            String found = matcher.group();
            String tag = matcher.group(1);
            boolean slash = found.endsWith("/>");
            if (slash) return consume(this, matcher.end());
            int offset = matcher.end();
            int end = matchEnd(buffer.subSequence(offset, buffer.length()), tag);
            if (end > 0) {
                return consume(this, offset + end);
            }
        }
        return null;
    }

    /**
     * Find the matching end tag.
     * TODO Each time a new opening tag is encountered it should be recursively searched so we can match the correct end tag, in case
     * there are more than one.
     * TODO This could exceed the buffer so there needs to be a way to refill it.
     * @param buffer buffer to look in
     * @param tag tag to find
     * @return offset found or 0 if not
     */
    private int matchEnd(CharSequence buffer, String tag) {
        java.util.regex.Matcher matcher = xmlEnd.matcher(buffer);
        while (matcher.find()) {
            if (matcher.group(1).equalsIgnoreCase(tag)) {
                return matcher.end();
            }
        }
        return 0;
    }

}
