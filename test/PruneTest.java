import org.rio.RioLang;
import org.rio.Node;
import org.junit.Before;
import org.junit.Test;

import static org.rio.core.Operator.*;
import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class PruneTest {

    private RioLang lang;

    @Before
    public void setUp() {
        lang = new RioLang();
    }

    @Test
    public void testExpr() throws Exception {
        String simple = "1 + 2";
        lang.load(simple);
        Node root = lang.prune();
        final Node statements = root.get(0);
        assertEquals("statements", statements.getName());
        assertNull(statements.get(1));
        final Node statement = statements.get(0);
        assertEquals("statement", statement.getName());
        final Node operator = statement.get(0);
        assertNull(statement.get(1));
        assertEquals(OPERATOR_ADD, operator.getName());
        final Node integer1 = operator.get(0);
        final Node integer2 = operator.get(1);
        assertNull(operator.get(2));
        assertEquals("number", integer1.getName());
        assertEquals("number", integer2.getName());
    }
}