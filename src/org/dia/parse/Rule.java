package org.dia.parse;

import java.util.List;
import org.dia.Node;

interface Rule {

    int complexity();   // TODO Remove: not used
    Node match(Node node, List<Node> matched);

}

