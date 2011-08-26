import org.rio.RioLang;
import org.rio.Node;
import org.junit.Before;
import org.junit.Test;

import static org.rio.core.Operator.*;
import static org.junit.Assert.*;

/**
 *
 * @author theninger
 */
public class ExpressionTest {

    private RioLang lang;

    @Before
    public void setUp() {
        lang = new RioLang();
    }

    @Test
    public void testTokenizer() {
        String simple = "1+2.0-8d";
        lang.load(simple);
        Node tokens = lang.tokenize();
        assertEquals(OPERATOR_ADD, tokens.get(1).getName());
        assertEquals("+", tokens.get(1).getValue().toString());
    }


    @Test
    public void parseExpression() {
        final String expr = "1+2";
        lang.load(expr);
        Node root = lang.parse();
        assertNotNull(root);
        assertEquals(1, root.countChildren());
        assertEquals("statements", root.get(0).getName());
        assertEquals("statement", root.get(0).get(0).getName());
        assertEquals("expression", root.get(0).get(0).get(0).getName());
    }

    @Test
    public void parseExpression2() {
        final String expr = "1+2*5";
        lang.load(expr);
        Node root = lang.parse();
        assertNotNull(root);
        assertEquals(1, root.countChildren());
        assertEquals("statements", root.get(0).getName());
        assertEquals("statement", root.get(0).get(0).getName());
        assertEquals("expression", root.get(0).get(0).get(0).getName());
    }

    @Test
    public void compileExpression() {
        final String expr = "1+2*5";
        lang.compile(expr);
        String program = lang.get("output");
        assertTrue(program.indexOf("class Main") > 0);
        assertTrue(program.indexOf("public static void main") > 0);
        assertTrue(program.indexOf("2 * 5") > 0);
        assertTrue(program.indexOf("1 +") > 0);
    }
}
