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
    private final String sameBlock;
    private final String endBlock;
    private final String eol;

    public LexerIndent(Plugin plugin, String eol, String start, String same, String end) {
        super(plugin);
        this.eol = eol;
        startBlock = start;
        sameBlock = same;
        endBlock = end;
    }

    public boolean atBol() {
        Node last = Compiler.compiler().lastToken;
        return last == null || last.isNamed(eol);
    }

    private boolean checkMatch(CharSequence indent, CharSequence lastIndent) {
        if (indent.toString().equals(lastIndent.toString())) return true;
        int len = Math.min(indent.length(), lastIndent.length());
        if (indent.subSequence(0, len).equals(lastIndent.subSequence(0, len))) return false;
        throw new LexException("Indentation characters don't match");
    }
    
    @Override
    public Token nextToken() {
        if (atEof()) {
            if (indents.isEmpty()) return super.nextToken();
            if (!atBol()) {
                return getPlugin().consume(eol, 0, null, null);
            }
            indents.pop();
            return getPlugin().consume(endBlock, 0, null, null);
        }
        if (!atBol()) return null;
        CharSequence indent = getIndent();
        if (indents.isEmpty()) {
            if (indent.length() == 0) return null;
            indents.push(indent);
            return getPlugin().consume(startBlock, indent.length(), null, null);
        }
        CharSequence lastIndent = indents.peek();
        if (checkMatch(indent, lastIndent)) {
            return getPlugin().consume(sameBlock, indent.length(), null, null);
        }
        if (indent.length() > lastIndent.length()) {
            indents.push(indent);
            return getPlugin().consume(startBlock, indent.length(), null, null);
        }
        indents.pop();
        return getPlugin().consume(endBlock, indent.length(), null, null);
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

