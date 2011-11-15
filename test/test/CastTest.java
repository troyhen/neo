package test;

import org.junit.After;
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
        assertTrue("missing ~", tokens.get(4).isNamed(OPERATOR_AS));
        assertEquals("missing ~", "~", tokens.get(4).getValue().toString());
    }


    @Test
    public void parseCast() {
        final String expr = "1~float";
//Log.testStart();
        lang.load(expr);
        Node node = lang.parse();
        Log.info(node.toTree());
        assertNotNull(node);
        assertEquals("Wrong number of children: " + node.childNames(), 3, node.countChildren());
        assertTrue("missing compilation", node.isNamed("compilation"));
        node = node.get(1);
        assertTrue("missing statements", node.isNamed("statements"));
        node = node.get(0);
        assertTrue("missing statement", node.isNamed("statement"));
        node = node.get(0);
        assertTrue("missing expression", node.isNamed("expression"));
        node = node.get(0);
        assertTrue("missing expression6", node.isNamed("expression6"));
        node = node.get(0);
        assertTrue("missing expression5", node.isNamed("expression5"));
        node = node.get(0);
        assertTrue("missing expression4", node.isNamed("expression4"));
        node = node.get(0);
        assertTrue("missing expression3", node.isNamed("expression3"));
        node = node.get(0);
        assertTrue("missing expression2", node.isNamed("expression2"));
        node = node.get(0);
        assertTrue("missing expression1", node.isNamed("expression1"));
        node = node.get(0);
        assertTrue("missing expression1", node.isNamed("expression1"));
        node = node.getNext();
        assertTrue("missing cast", node.isNamed("cast"));
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
        assertTrue("missing compilation", node.isNamed("compilation"));
        node = node.get(0);
        assertTrue("missing statements", node.isNamed("statements"));
        node = node.get(0);
        assertTrue("missing statement", node.isNamed("statement"));
        node = node.get(0);
        assertTrue("missing cast 1", node.isNamed("cast"));
        node = node.get(0);
        assertTrue("missing cast 2", node.isNamed("cast"));
        assertTrue("missing operator_as 1", node.getNext().isNamed("operator_as"));
        node = node.get(0);
        assertTrue("missing string_single", node.isNamed("string_single"));
        assertTrue("missing operator_as 2", node.getNext().isNamed("operator_as"));
        node = node.getNext().get(0);
        assertTrue("missing class_path", node.isNamed("class_path"));
        node = node.get(0);
        assertTrue("missing symbol_int", node.isNamed("symbol_int"));
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
