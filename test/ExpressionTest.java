import org.junit.After;
import java.util.logging.Level;
import org.neo.util.Log;
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
        assertEquals(OPERATOR_ADD, tokens.get(2).getName());
        assertEquals("+", tokens.get(2).getValue().toString());
    }


    @Test
    public void parseExpression() {
        final String expr = "1+2";
//Log.testStart();
        lang.load(expr);
        Node root = lang.parse();
        Log.info(root.toTree());
        assertNotNull(root);
        assertEquals("Wrong number of children", 3, root.countChildren());
        assertEquals("Top of tree should be ", "statements_compilation", root.get(1).getName());
        assertEquals("Second level should be ", "statement_expression", root.get(1).get(0).getName());
        assertEquals("Third level should be ", "expression_add", root.get(1).get(0).get(0).getName());
    }

    @Test
    public void parseExpression2() {
        final String expr = "1+2*5";
//Log.testStart();
        lang.load(expr);
        Node root = lang.parse();
        assertNotNull(root);
        assertEquals("Wrong number of children", 3, root.countChildren());
        assertEquals("missing statements", "statements_compilation", root.get(1).getName());
        assertEquals("missing statement", "statement_expression", root.get(1).get(0).getName());
        assertEquals("missing expression", "expression_add", root.get(1).get(0).get(0).getName());
    }

    @Test
    public void compileExpression1() {
        final String expr = "1+2*5";
//Log.testStart();
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
//Log.testStart();
        lang.compile(expr);
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing class", program.indexOf("class Main") > 0);
        assertTrue("missing main", program.indexOf("public static void main") > 0);
        assertTrue("multiplication should be grouped", program.indexOf("2 * 5") > 0);
        assertTrue("addition should be outside", program.indexOf("+ 1") > 0);
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
