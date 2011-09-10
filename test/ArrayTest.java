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
public class ArrayTest {

    private NeoLang lang;

    @Before
    public void setUp() {
        lang = new NeoLang();
    }

    @After
    public void tearDown() {
        Log.logger.setLevel(Level.WARNING);
    }

    @Test
    public void intArray() {
        final String expr = "var a = [1 2 3 4]";
//Log.logger.setLevel(Level.ALL);
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("int[] a = new int[]") > 0);
        assertTrue("missing content", program.indexOf("1, 2, 3, 4") > 0);
        assertTrue("missing end", program.indexOf("};") > 0);
    }

    @Test
    public void commasArray() {
        final String expr = "var a = [1, 2 3, 4]";
//Log.logger.setLevel(Level.ALL);
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("int[] a = new int[]") > 0);
        assertTrue("missing content", program.indexOf("1, 2, 3, 4") > 0);
        assertTrue("missing end", program.indexOf("};") > 0);
    }

    @Test
    public void explicitIntArray() {
        final String expr = "var a~int[]";
//Log.logger.setLevel(Level.ALL);
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("int[] a;") > 0);
    }

    @Test
    public void explicitArrayAssign() {
        final String expr = "var a~long[] = [0l 1l 2l 3l]";
//Log.logger.setLevel(Level.ALL);
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("long[] a = new long[]") > 0);
        assertTrue("missing content", program.indexOf("0l, 1l, 2l, 3l") > 0);
        assertTrue("missing end", program.indexOf("};") > 0);
    }

    @Test
    public void stringArray() {
//Log.logger.setLevel(Level.ALL);
        final String expr = "var a~String[] = [`a \"b\" 'c']";
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("String[] a = new java.lang.String[]") > 0);
        assertTrue("missing content", program.indexOf("\"a\", \"b\", \"c\"") > 0);
        assertTrue("missing end", program.indexOf("};") > 0);
    }

    @Test
    public void mixedArray() {
//Log.logger.setLevel(Level.ALL);
        final String expr = "var a = [1 2.2 `c]";
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("java.lang.Object[] a = new java.lang.Object[]") > 0);
        assertTrue("missing content", program.indexOf("1, 2.2, \"c\"") > 0);
        assertTrue("missing end", program.indexOf("};") > 0);
    }

}
