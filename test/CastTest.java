import org.junit.After;
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
public class CastTest {

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
        String simple = "1+2.0~int-8d";
        lang.load(simple);
        Node tokens = lang.tokenize();
        assertTrue("missing ~", tokens.get(3).isNamed(OPERATOR_AS));
        assertEquals("missing ~", "~", tokens.get(3).getValue().toString());
    }


    @Test
    public void parseCast() {
        final String expr = "1~float";
//Log.testStart();
        lang.load(expr);
        Node node = lang.parse();
        assertNotNull(node);
        assertEquals("Wrong number of children: " + node.childNames(), 1, node.countChildren());
        node = node.get(0);
        assertTrue("Top of tree", node.isNamed("statements"));
        node = node.get(0);
        assertTrue("Second level", node.isNamed("statement"));
        node = node.get(0);
        assertTrue("Third level", node.isNamed("expression"));
        node = node.get(0);
        assertTrue("Forth level", node.isNamed("expression"));
        node = node.getNext();
        assertTrue("Forth level", node.isNamed("cast"));
    }

    @Test
    public void pruneCast() {
        final String expr = "'10.4'~int~float";
//Log.testStart();
        lang.load(expr);
        Node node = lang.prune();
        Log.info(node.toTree());
        assertNotNull(node);
        assertEquals("Wrong number of children: " + node.childNames(), 1, node.countChildren());
        node = node.get(0);
        assertTrue("Top of tree", node.isNamed("statements"));
        node = node.get(0);
        assertTrue("Second level", node.isNamed("statement"));
        node = node.get(0);
        assertTrue("Third level", node.isNamed("cast"));
        node = node.get(0);
        assertTrue("Forth level", node.isNamed("cast"));
        assertTrue("Forth level", node.getNext().isNamed("operator_as"));
        node = node.get(0);
        assertTrue("Fifth level", node.isNamed("string_single"));
        assertTrue("Fifth level", node.getNext().isNamed("operator_as"));
        node = node.getNext().get(0);
        assertTrue("Sixth level", node.isNamed("symbol_int"));
    }

    @Test
    public void compileCast1() {
        final String expr = "1+2~float";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue(program.indexOf("class Main") > 0);
        assertTrue(program.indexOf("public static void main") > 0);
        assertTrue(program.indexOf("tofloat(2)") > 0);
        assertTrue(program.indexOf("1 +") > 0);
    }

    @Test
    public void compileCast2() {
        final String expr = "2~float+1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue(program.indexOf("class Main") > 0);
        assertTrue(program.indexOf("public static void main") > 0);
        assertTrue(program.indexOf("tofloat(2)") > 0);
        assertTrue(program.indexOf("+ 1") > 0);
    }

    @Test
    public void compileCastFloat() {
        final String expr = "2~java.lang.Float";
//Log.testStart();
        lang.compile(expr);
        String program = lang.get("output");
        Log.info(program);
        assertTrue(program.indexOf("class Main") > 0);
        assertTrue(program.indexOf("public static void main") > 0);
        assertTrue(program.indexOf("to(2, (java.lang.Float) null)") > 0);
    }
}
