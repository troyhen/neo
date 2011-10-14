package org.neo.lex;

import java.util.regex.Pattern;
import org.neo.Plugin;
import org.neo.parse.Engine;

/**
 *
 * @author Troy Hheninger
 */
public class LexerPattern extends LexerBase {

    public final Pattern pattern;
    public final int group;

    public LexerPattern(Plugin plugin, String name, Pattern pattern) {
        this(plugin, name, pattern, 0);
    }

    public LexerPattern(Plugin plugin, String name, Pattern pattern, int group) {
        super(plugin, name);
        this.pattern = pattern;
        this.group = group;
    }

    public LexerPattern(Plugin plugin, String name, String regex) {
        this(plugin, name, regex, 0);
    }

    public LexerPattern(Plugin plugin, String name, String regex, int group) {
        this(plugin, name, toPattern(regex), group);// + (regex.startsWith("(") ? 0 : 1));
    }

    public static Pattern toPattern(String regex) {
        return Pattern.compile(regex.startsWith("^") ? regex : '^' + regex);
    }

    @Override
    public Token nextToken() {
        CharSequence buffer = Engine.buffer();
        java.util.regex.Matcher match = pattern.matcher(buffer);
        if (match.find()) {
            if (group <= 0) return consume(match.end());
            return consume(match.end(), match.group(group));
        }
        return null;
    }
}
