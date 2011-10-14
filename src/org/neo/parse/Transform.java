package org.neo.parse;

/**
 *
 * @author Troy Heninger
 */
public interface Transform {

    void prepare(Node node);

    Node transform(Node node);
    
}
