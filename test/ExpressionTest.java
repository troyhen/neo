import org.junit.After;
import java.util.logging.Level;
import org.neo.Log;
import org.neo.NeoLang;
import org.neo.Node;
import org.junit.Before;
import org.junit.Test;

import static org.neo.core.Operator.*;
import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class ExpressionTest {

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
        assertEquals("Wrong number of children", 1, root.countChildren());
        assertEquals("Top of tree should be ", "statements", root.get(0).getName());
        assertEquals("Second level should be ", "statement", root.get(0).get(0).getName());
        assertEquals("Third level should be ", "expression", root.get(0).get(0).get(0).getName());
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
    public void compileExpression1() {
        final String expr = "1+2*5";
        lang.compile(expr);
        String program = lang.get("output");
        Log.info(program);
        assertTrue(program.indexOf("class Main") > 0);
        assertTrue(program.indexOf("public static void main") > 0);
        assertTrue(program.indexOf("2 * 5") > 0);
        assertTrue(program.indexOf("1 +") > 0);
    }

    @Test
    public void compileExpression2() {
        final String expr = "2*5+1";
        lang.compile(expr);
        String program = lang.get("output");
        Log.info(program);
        assertTrue(program.indexOf("class Main") > 0);
        assertTrue(program.indexOf("public static void main") > 0);
        assertTrue(program.indexOf("2 * 5") > 0);
        assertTrue(program.indexOf("+ 1") > 0);
    }

    @Test
    public void compileCompare() {
        final String expr = "1 <=> 2";
        lang.compile(expr);
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing comare", program.indexOf("compare(1, 2)") > 0);
    }
}
