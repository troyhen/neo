import org.neo.Log;
import org.junit.After;
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
public class StringExpressionTest {

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
    public void compileExpression1() {
        final String expr = "\"#{1}\"";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing 1", program.indexOf("(1);") > 0);
    }

    @Test
    public void compileExpression2() {
        final String expr = "'#{1}'";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing '1'", program.indexOf("\"#{1}\"") > 0);
    }
    
    @Test
    public void compileExpressions() {
        final String expr = "\"Your name is #{person.name} and age is #{person.birth.age + 1}.\"";
Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing string 1", program.indexOf("\"Your name is \"") > 0);
        assertTrue("missing name", program.indexOf("person.name") > 0);
        assertTrue("missing string 2", program.indexOf("\" and age is \"") > 0);
        assertTrue("missing birth", program.indexOf("(person.birth.age + 1)") > 0);
        assertTrue("missing string 3", program.indexOf("\".\"") > 0);
    }

}
