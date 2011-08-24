package org.rio.parse;

import java.util.List;
import org.rio.Node;

interface Rule {

    Node match(Node node, List<Node> matched);

}

