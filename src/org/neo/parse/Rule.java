package org.neo.parse;

import java.util.List;

public interface Rule {

    Node match(Node node, List<Node.Match> matched);

}

