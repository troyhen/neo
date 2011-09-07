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
public class CastTest {

    private NeoLang lang;

    @Before
    public void setUp() {
        lang = new NeoLang();
    }

    public void tearDown() {
        Log.logger.setLevel(Level.WARNING);
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
        lang.load(expr);
        Node root = lang.parse();
        assertNotNull(root);
        assertEquals("Wrong number of children: " + root.childNames(), 1, root.countChildren());
        assertEquals("Top of tree", "statements", root.get(0).getName());
        assertEquals("Second level", "statement", root.get(0).get(0).getName());
        assertEquals("Third level", "expression", root.get(0).get(0).get(0).getName());
        assertEquals("Forth level", "operator.as", root.get(0).get(0).get(0).get(1).getName());
    }

    @Test
    public void parseCast2() {
        final String expr = "'10.4'~int~float";
        lang.load(expr);
        Node root = lang.parse();
        assertNotNull(root);
        assertEquals("Wrong number of children: " + root.childNames(), 1, root.countChildren());
        assertEquals("Top of tree", "statements", root.get(0).getName());
        assertEquals("Second level", "statement", root.get(0).get(0).getName());
        assertEquals("Third level", "expression", root.get(0).get(0).get(0).getName());
        assertEquals("Forth level", "operator.as", root.get(0).get(0).get(0).get(1).getName());
        assertEquals("Forth level", "symbol.float", root.get(0).get(0).get(0).get(2).getName());
        assertEquals("Fifth level", "string.single", root.get(0).get(0).get(0).get(0).get(0).get(0).getName());
        assertEquals("Fifth level", "operator.as", root.get(0).get(0).get(0).get(0).get(1).getName());
        assertEquals("Fifth level", "symbol.int", root.get(0).get(0).get(0).get(0).get(2).getName());
    }

    @Test
    public void compileCast1() {
        final String expr = "1+2~float";
        lang.compile(expr);
        String program = lang.get("output");
//        System.out.println(program);
        assertTrue(program.indexOf("class Main") > 0);
        assertTrue(program.indexOf("public static void main") > 0);
        assertTrue(program.indexOf("tofloat(2)") > 0);
        assertTrue(program.indexOf("1 +") > 0);
    }

    @Test
    public void compileCast2() {
        final String expr = "2~float+1";
        lang.compile(expr);
        String program = lang.get("output");
//        System.out.println(program);
        assertTrue(program.indexOf("class Main") > 0);
        assertTrue(program.indexOf("public static void main") > 0);
        assertTrue(program.indexOf("tofloat(2)") > 0);
        assertTrue(program.indexOf("+ 1") > 0);
    }

    @Test
    public void compileCastFloat() {
        final String expr = "2~java.lang.Float";
        lang.compile(expr);
        String program = lang.get("output");
//        System.out.println(program);
        assertTrue(program.indexOf("class Main") > 0);
        assertTrue(program.indexOf("public static void main") > 0);
        assertTrue(program.indexOf("to(2, (java.lang.Float) null)") > 0);
    }
}
