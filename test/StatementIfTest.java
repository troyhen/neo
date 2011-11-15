import org.junit.After;
import org.neo.util.Log;
import org.neo.NeoLang;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class StatementIfTest {

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
    public void compileIf1() {
        final String expr = "execute if 1 < 2";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing if", program.indexOf("if (1 < 2)") > 0);
        assertTrue("missing execute", program.indexOf("execute") > 0);
    }

    @Test
    public void compileIf2() {
        final String expr = "execute if 1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing if", program.indexOf("if (toboolean(1))") > 0);
        assertTrue("missing execute", program.indexOf("execute") > 0);
    }

    @Test
    public void compileWhile1() {
//Log.testStart();
        final String expr = "execute while 1 > 2";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing while", program.indexOf("while (1 > 2)") > 0);
        assertTrue("missing execute", program.indexOf("execute") > 0);
    }

    @Test
    public void compileWhile2() {
//Log.testStart();
        final String expr = "execute while 1";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing while", program.indexOf("while (toboolean(1))") > 0);
        assertTrue("missing execute", program.indexOf("execute") > 0);
    }

    @Test
    public void compileUnless1() {
//Log.testStart();
        final String expr = "execute unless 1 >= 2";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing if", program.indexOf("if (!(1 >= 2))") > 0);
        assertTrue("missing execute", program.indexOf("execute") > 0);
    }

    @Test
    public void compileUnless2() {
//Log.testStart();
        final String expr = "execute unless 1";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing if", program.indexOf("if (!toboolean(1))") > 0);
        assertTrue("missing execute", program.indexOf("execute") > 0);
    }

    @Test
    public void compileUntil1() {
//Log.testStart();
        final String expr = "execute until 1 == 2";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing while", program.indexOf("while (!(equal(1, 2)))") > 0);
        assertTrue("missing execute", program.indexOf("execute") > 0);
    }

    @Test
    public void compileUntil2() {
//Log.testStart();
        final String expr = "execute until 1";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing while", program.indexOf("while (!toboolean(1))") > 0);
        assertTrue("missing execute", program.indexOf("execute") > 0);
    }

}
