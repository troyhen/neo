package org.rio.parse;

import java.util.List;
import org.rio.Node;

interface Rule {

    int complexity();   // TODO Remove: not used
    Node match(Node node, List<Node> matched);

}

