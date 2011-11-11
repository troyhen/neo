import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.neo.util.Log;
import org.neo.NeoLang;

import static org.neo.core.Operator.*;

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
        Log.testStop();
    }

    @Test
    public void intArray() {
        final String expr = "var a = [1 2 3 4]";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("int[] a = new int[]") > 0);
        assertTrue("missing content", program.indexOf("1, 2, 3, 4") > 0);
        assertTrue("missing end", program.indexOf("};") > 0);
    }

    @Test
    public void commasArray() {
        final String expr = "var a = [1, 2 3, 4]";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("int[] a = new int[]") > 0);
        assertTrue("missing content", program.indexOf("1, 2, 3, 4") > 0);
        assertTrue("missing end", program.indexOf("};") > 0);
    }

    @Test
    public void explicitIntArray() {
        final String expr = "var a~[int]";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("int[] a;") > 0);
    }

    @Test
    public void explicitArrayAssign() {
        final String expr = "var a~[long] = [0l 1l 2l 3l]";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("long[] a = new long[]") > 0);
        assertTrue("missing content", program.indexOf("0l, 1l, 2l, 3l") > 0);
        assertTrue("missing end", program.indexOf("};") > 0);
    }

    @Test
    public void stringArray() {
//Log.testStart();
        final String expr = "var a~[String] = [`a \"b\" 'c']";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("String[] a = new java.lang.String[]") > 0);
        assertTrue("missing content", program.indexOf("\"a\", \"b\", \"c\"") > 0);
        assertTrue("missing end", program.indexOf("};") > 0);
    }

    @Test
    public void mixedArray() {
//Log.testStart();
        final String expr = "var a = [1 2.2 `c]";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("java.lang.Object[] a = new java.lang.Object[]") > 0);
        assertTrue("missing content", program.indexOf("1, 2.2, \"c\"") > 0);
        assertTrue("missing end", program.indexOf("};") > 0);
    }

}
