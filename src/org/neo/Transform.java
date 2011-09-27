package org.neo;

/**
 *
 * @author Troy Heninger
 */
public interface Transform {

    void prepare(Node node);

    Node transform(Node node);
    
}
