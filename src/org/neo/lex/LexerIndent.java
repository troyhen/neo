package org.neo.lex;

import java.util.ArrayDeque;
import java.util.Deque;

import org.neo.Compiler;
import org.neo.Node;
import org.neo.Plugin;

/**
 *
 * @author Troy Heninger
 */
public class LexerIndent extends LexerEof {

    private final Deque<CharSequence> indents = new ArrayDeque<CharSequence>();
    private final String startBlock;
    private final String endBlock;
    private final String eol;

    public LexerIndent(Plugin plugin, String eol, String start, String end) {
        super(plugin);
        this.eol = eol;
        startBlock = start;
        endBlock = end;
    }

    public boolean atBol() {
        Node last = Compiler.compiler().getRoot().getLast();
        return last == null || last.getName().equals(eol);
    }

    private void checkMatch(CharSequence indent, CharSequence lastIndent) {
        if (indent.equals(lastIndent)) return;
        int len = Math.min(indent.length(), lastIndent.length());
        if (indent.subSequence(0, len).equals(lastIndent.subSequence(0, len))) return;
        throw new LexException("Indentation characters don't match");
    }
    
    @Override
    public Token nextToken() {
        if (atEof()) {
            if (indents.isEmpty()) return super.nextToken();
            indents.pop();
            return getPlugin().consume(endBlock, 0);
        }
        if (!atBol()) return null;
        CharSequence indent = getIndent();
        if (indents.isEmpty()) {
            if (indent.length() == 0) return null;
            indents.push(indent);
            return getPlugin().consume(startBlock, indent.length());
        }
        CharSequence lastIndent = indents.peek();
        checkMatch(indent, lastIndent);
        if (indent.equals(lastIndent)) return null;
        if (indent.length() > lastIndent.length()) {
            indents.push(indent);
            return getPlugin().consume(startBlock, indent.length());
        }
        indents.pop();
        return getPlugin().consume(endBlock, 0);
    }

    public CharSequence getIndent() {
        CharSequence buf = Compiler.buffer();
        StringBuilder indent = new StringBuilder();
        for (int ix = 0, iz = buf.length(); ix < iz; ix++) {
            char ch = buf.charAt(ix);
            if (ch == ' ' || ch == '\t') {
                indent.append(ch);
            } else {
                break;
            }
        }
        return indent;
    }

}

