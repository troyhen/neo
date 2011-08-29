import org.neo.core.Strings;
import org.neo.NeoLang;
import org.neo.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author theninger
 */
public class StringTest {

    private NeoLang lang;

    @Before
    public void setUp() {
        lang = new NeoLang();
    }

    @Test
    public void testTokenizer() {
        String simple = " \"this is a string\" "
                + "'this is another' "
                + "\"\"\"This\nis\na\nvery\nlong\nstring\"\"\" ";
        lang.load(simple);
        Node tokens = lang.tokenize();
        assertEquals(Strings.STRING_DOUBLE, tokens.get(0).getName());
        assertEquals(Strings.STRING_SINGLE, tokens.get(1).getName());
        assertEquals(Strings.STRING_MULTILINE, tokens.get(2).getName());
    }


//    @Test
//    public void parseExpression() {
//        final String expr = "1+2";
//        lang.load(expr);
//        Node root = lang.parse();
//        assertNotNull(root);
//        assertEquals(1, root.countChildren());
//        assertEquals("statements", root.get(0).getName());
//        assertEquals("statement", root.get(0).get(0).getName());
//        assertEquals("expression", root.get(0).get(0).get(0).getName());
//    }
//
//    @Test
//    public void parseExpression2() {
//        final String expr = "1+2*5";
//        lang.load(expr);
//        Node root = lang.parse();
//        assertNotNull(root);
//        assertEquals(1, root.countChildren());
//        assertEquals("statements", root.get(0).getName());
//        assertEquals("statement", root.get(0).get(0).getName());
//        assertEquals("expression", root.get(0).get(0).get(0).getName());
//    }
//
//    @Test
//    public void compileExpression1() {
//        final String expr = "1+2*5";
//        lang.compile(expr);
//        String program = lang.get("output");
//        System.err.println(program);
//        assertTrue(program.indexOf("class Main") > 0);
//        assertTrue(program.indexOf("public static void main") > 0);
//        assertTrue(program.indexOf("2 * 5") > 0);
//        assertTrue(program.indexOf("1 +") > 0);
//    }
//
//    @Test
//    public void compileExpression2() {
//        final String expr = "2*5+1";
//        lang.compile(expr);
//        String program = lang.get("output");
//        System.err.println(program);
//        assertTrue(program.indexOf("class Main") > 0);
//        assertTrue(program.indexOf("public static void main") > 0);
//        assertTrue(program.indexOf("2 * 5") > 0);
//        assertTrue(program.indexOf("+ 1") > 0);
//    }
}
