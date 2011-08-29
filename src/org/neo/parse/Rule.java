package org.neo.parse;

import java.util.List;
import org.neo.Node;

public interface Rule {

    Node match(Node node, List<Node> matched);

}

