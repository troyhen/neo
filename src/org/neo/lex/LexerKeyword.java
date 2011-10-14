/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neo.lex;

import org.neo.Plugin;
import org.neo.parse.Engine;

/**
 *
 * @author theninger
 */
public class LexerKeyword extends LexerPattern {

    public LexerKeyword(Plugin plugin, String pattern) {
        super(plugin, "symbol", pattern);
    }

    @Override
    public Token nextToken() {
        Token token = super.nextToken();
        if (token == null) return null;
        if (Engine.engine().isKeyword(token.getText().toString())) {
            token.setName("keyword_" + token.getText());
        }
        return token;
    }
}
