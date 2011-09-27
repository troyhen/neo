package org.neo.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.neo.Compiler;
import org.neo.Log;
import org.neo.Node;
import org.neo.lex.LexerPattern;
import org.neo.lex.Token;

/**
 *
 * @author Troy Heninger
 */
public class Strings extends CorePlugin {
    
    public static final String CLASS = "java.lang.String";

    public static final String STRING_DOUBLE = "string_double";
//    public static final String STRING_DOUBLE_BAD = "string_double_bad";
//    public static final String STRING_DOUBLE_BAD2 = "string_double_bad2";
    public static final String STRING_MULTILINE = "string_multiline";
    public static final String STRING_SINGLE = "string_single";
//    public static final String STRING_SINGLE_BAD = "string_single_bad";
//    public static final String STRING_SINGLE_BAD2 = "string_single_bad2";
    public static final String STRING_WORD = "string_word";

    private static final Pattern expession = Pattern.compile("#\\{((\\\\}|[^}])*)}");

    @Override
    public Token consume(String name, int chars, Object value, String type) {
        return super.consume(name, chars, decode((CharSequence) value), CLASS);
    }

    public static String decode(CharSequence chars) {
        if (chars == null) return null;
        String string = chars.toString();
        StringBuilder buff = new StringBuilder(string.length());
        for (int ix = 0, iz = string.length(); ix < iz;) {
            char ch = string.charAt(ix++);
            if (ch == '\\' && ix < iz) {
                ch = string.charAt(ix++);
                switch (ch) {
                    case 'b':
                        buff.append('\b');
                        break;
                    case 'f':
                        buff.append('\f');
                        break;
                    case 'n':
                        buff.append('\n');
                        break;
                    case 'r':
                        buff.append('\r');
                        break;
                    case 's':
                        buff.append(' ');
                        break;
                    case 't':
                        buff.append('\t');
                        break;
                    case '\\':
                        buff.append('\\');
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        if (ix + 2 < iz) {
                            buff.append((char) Integer.parseInt(string.substring(ix - 1, ix + 2), 8));
                            ix += 2;
                        } else {
                            buff.append((char) Integer.parseInt(string.substring(ix - 1), 8));
                            ix = iz;
                        }
                        break;
                    case 'x':
                        if (ix + 1 < iz) {
                            buff.append((char) Integer.parseInt(string.substring(ix - 1, ix + 1), 16));
                            ix++;
                        } else {
                            buff.append((char) Integer.parseInt(string.substring(ix - 1), 16));
                            ix = iz;
                        }
                        break;
                    case 'u':
                        if (ix + 3 < iz) {
                            buff.append((char) Integer.parseInt(string.substring(ix - 1, ix + 3), 16));
                            ix += 2;
                        } else {
                            buff.append((char) Integer.parseInt(string.substring(ix - 1), 16));
                            ix = iz;
                        }
                        break;
                    default:
                        buff.append(ch);
                }
            } else {
                buff.append(ch);
            }
        }
        return buff.toString();
    }

    public static String encode(CharSequence string) {
        return encode(string, '"');
    }

    public static String encode(CharSequence string, char delimiter) {
        if (string == null) return null;
        StringBuilder buff = new StringBuilder(string.length() * 11 / 10);
        for (int ix = 0, iz = string.length(); ix < iz; ix++) {
            char ch = string.charAt(ix);
            if (ch == '\\') {
                buff.append("\\\\");
            } else if (ch == '\b') {
                buff.append("\\b");
            } else if (ch == '\f') {
                buff.append("\\f");
            } else if (ch == '\n') {
                buff.append("\\n");
            } else if (ch == '\r') {
                buff.append("\\r");
            } else if (ch == '\t') {
                buff.append("\\t");
            } else if (ch == delimiter) {
                buff.append('\\');
                buff.append(ch);
            } else if (ch >= 256) {
                buff.append(String.format("\\u%4x",  ch));
            } else if (ch < ' ' || ch >= 127) {
                buff.append(String.format("\\x%2x",  ch));
            } else {
                buff.append(ch);
            }
        }
        return buff.toString();
    }
    
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
        addParser("expression", "string");
    }

    @Override
    public Node transform(Node node) {
        String type = null;
        if (node.isNamed("expression")) {
            type = node.getFirst().getTypeName();
        }
        if (type != null) node.setTypeName(type);
        String name = node.getName();
        if (name.equals(STRING_DOUBLE) || name.equals(STRING_MULTILINE)) {
            final CharSequence text = (CharSequence) node.getValue();
            Matcher match = expession.matcher(text);
            int index = 0;
            while (match.find()) {
                String content = match.group(1).trim();
                if (content.length() == 0) continue;
                if (match.start() > index || index == 0) {  // We need the first string even if empty so the Java expression will do String conversion and concatenation
                    CharSequence between = text.subSequence(index, match.start());
                    Node string = new Node(this, STRING_SINGLE, between, between, CLASS);
                    node.add(string);
                }
                Compiler current = Compiler.compiler();
                Compiler comp = current.subcompile("expression");
                comp.load(content);
                Log.info("compiling string content: " + content);
                Node node1 = comp.transform();
                do {
                    node1 = node1.getFirst();
                } while (node1.isNamed("expression"));
                node.add(node1);
                comp.close(current);
                index = match.end();
            }
            int end = text.length();
            if (index > 0 && index < end) {
                CharSequence between = text.subSequence(index, end);
                Node string = new Node(this, STRING_SINGLE, between, between, CLASS);
                node.add(string);
            }
        }
        return node;
    }

}
