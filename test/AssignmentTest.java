import org.junit.After;
import org.neo.Log;
import org.neo.NeoLang;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class AssignmentTest {

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
    public void compileAssign() {
        final String expr = "a = 1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing assign", program.indexOf("a = 1") > 0);
    }

    @Test
    public void compileAssign2() {
        final String expr = "a = 1 + 2";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing assign", program.indexOf("a = (1 + 2)") > 0);
    }

    @Test
    public void compileAssign3() {
        final String expr = "b = c = 2";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing c", program.indexOf("c = 2") > 0);
        assertTrue("missing b", program.indexOf("b = (") > 0);
    }

    @Test
    public void compileAssignComplex() {
        final String expr = "a = (b = 1) + 2";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing assignments", program.indexOf("a = ((b = 1) + 2)") > 0);
    }

    @Test
    public void compileAssignElement() {
        final String expr = "a[1] = 2";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing assignments", program.indexOf("a[1] = 2") > 0);
    }
}
