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
public class OpenClassTest {

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
    public void openClass1() {
        final String expr
                = "def capitalize~String value~String\n"
                + "    return value if value == null || value.isEmpty()\n"
                + "    Character.toUpperCase(value.charAt 0) + value.substring 1\n"
                + "\n"
                + "\"this is a test\".capitalize()";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing declaration", (i1 = program.indexOf("String capitalize(String value)")) > i0);
        i0 = i1;
        assertTrue("missing if", (i1 = program.indexOf("if (")) > i0);
        i0 = i1;
        assertTrue("missing first compare", (i1 = program.indexOf("value == null")) > i0);
        i0 = i1;
        assertTrue("missing second compare", (i1 = program.indexOf("value.isEmpty()")) > i0);
        i0 = i1;
        assertTrue("missing first return", (i1 = program.indexOf("return value;")) > i0);
        i0 = i1;
        assertTrue("missing second return", (i1 = program.indexOf("return Character.toUpperCase(value.charAt(0)) + value.substring(1);")) > i0);
        i0 = i1;
    }

}
