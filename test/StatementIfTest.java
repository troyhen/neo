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
public class StatementIfTest {

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
    public void compileIf1() {
        final String expr = "execute if true";
        lang.compile(expr);
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing if", program.indexOf("if (true)") > 0);
        assertTrue("missing execute", program.indexOf("execute()") > 0);
    }

    @Test
    public void compileWhile1() {
//Log.logger.setLevel(Level.ALL);
        final String expr = "execute while true";
        lang.compile(expr);
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing while", program.indexOf("while (true)") > 0);
        assertTrue("missing execute", program.indexOf("execute()") > 0);
    }

    @Test
    public void compileUnless1() {
//Log.logger.setLevel(Level.ALL);
        final String expr = "execute unless false";
        lang.compile(expr);
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing if", program.indexOf("if (!(false))") > 0);
        assertTrue("missing execute", program.indexOf("execute()") > 0);
    }

    @Test
    public void compileUntil1() {
//Log.logger.setLevel(Level.ALL);
        final String expr = "execute until false";
        lang.compile(expr);
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing while", program.indexOf("while (!(false))") > 0);
        assertTrue("missing execute", program.indexOf("execute()") > 0);
    }

}
