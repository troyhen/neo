/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neo.parse;

import java.util.List;
import org.neo.parse.Node.Match;

/**
 *
 * @author theninger
 */
public class Start extends State {

    public static final State over = new Start(null);

    public Start(Progress progress) {
        super(progress);
    }

    @Override
    public void link(String name, State state) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public State parse(Node node, List<Match> matched) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
