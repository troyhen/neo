package org.neo.parse;

import org.neo.NeoException;

/**
 * Created by IntelliJ IDEA.
 * User: theninger
 * Date: 11/3/11
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AtEof extends Mismatch {
    public AtEof(String name) {
        super(null, name);
    }
}
