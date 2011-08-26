package org.neo.parse;

import java.util.List;
import org.neo.Node;

interface Rule {

    Node match(Node node, List<Node> matched);

}

