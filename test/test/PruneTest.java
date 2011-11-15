package test;

import org.neo.util.Log;
import org.junit.After;
import org.neo.NeoLang;
import org.neo.parse.Node;
import org.junit.Before;
import org.junit.Test;

import static org.neo.core.Operator.*;
import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class PruneTest {

    private NeoLang lang;

    @Before
    public void setUp() {
        lang = new NeoLang();
    }

    @After
    public void tearDown() {
        Log.testStop();
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
        assertEquals("statement_expression", statement.getName());
        final Node operator = statement.get(0);
        assertNull(statement.get(1));
        assertEquals(OPERATOR_ADD, operator.getName());
        final Node integer1 = operator.get(0);
        final Node integer2 = operator.get(1);
        assertNull(operator.get(2));
        assertEquals("number_integer", integer1.getName());
        assertEquals("number_integer", integer2.getName());
    }
}
