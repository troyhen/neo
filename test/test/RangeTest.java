package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo.NeoLang;
import org.neo.util.Log;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author Troy Heninger
 */
public class RangeTest {

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
    public void testInclusive() {
        final String expr = "1..10";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing range", program.indexOf("new neo.lang.Range(1, 10, true)") > 0);
    }

    @Test
    public void testExclusive() {
        final String expr = "1...10";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing range", program.indexOf("new neo.lang.Range(1, 10, false)") > 0);
    }

    @Test
    public void testMatch() {
        final String expr = "1..10 ~= 5";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing range", program.indexOf("match(new neo.lang.Range(1, 10, true), 5)") > 0);
    }

    @Test
    public void testStep() {
        final String expr = "1..10@2";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing range", program.indexOf("new neo.lang.Range(1, 10, true, 2)") > 0);
    }

}
