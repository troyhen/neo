package org.neo.back;

import org.neo.parse.Node;

/**
 *
 * @author theninger
 */
public interface Render {

    void render(Node node, String backend);

}
