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
        assertEquals(OPERATOR_AS, tokens.get(3).getName());
        assertEquals("~", tokens.get(3).getValue().toString());
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
        assertEquals("Top of tree", "statements", node.getName());
        node = node.get(0);
        assertEquals("Second level", "statement", node.getName());
        node = node.get(0);
        assertEquals("Third level", "expression", node.getName());
        node = node.get(0);
        assertEquals("Forth level", "expression", node.getName());
        node = node.getNext();
        assertEquals("Forth level", "cast", node.getName());
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
        assertEquals("Top of tree", "statements", node.getName());
        node = node.get(0);
        assertEquals("Second level", "statement", node.getName());
        node = node.get(0);
        assertEquals("Third level", "cast", node.getName());
        node = node.get(0);
        assertEquals("Forth level", "cast", node.getName());
        assertEquals("Forth level", "operator_as", node.getNext().getName());
        node = node.get(0);
        assertEquals("Fifth level", "string_single", node.getName());
        assertEquals("Fifth level", "operator_as", node.getNext().getName());
        node = node.getNext().get(0);
        assertEquals("Sixth level", "symbol_int", node.getName());
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
