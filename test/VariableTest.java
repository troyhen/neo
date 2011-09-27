import org.neo.NeoException;
import org.junit.After;
import java.util.logging.Level;
import org.neo.Log;
import org.neo.NeoLang;
import org.neo.Node;
import org.junit.Before;
import org.junit.Test;
import org.neo.parse.ParseException;

import static org.neo.core.Operator.*;
import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class VariableTest {

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
    public void implicitVar() {
        final String expr = "var a = 1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("int a = 1;") > 0);
    }

    @Test
    public void explicitVar() {
//Log.testStart();
        final String expr = "var a~String";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("String a;") > 0);
    }

    @Test
    public void explicitVarAssign() {
//Log.testStart();
        final String expr = "var a~long = 0";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("long a = 0;") > 0);
    }

    @Test
    public void implicitVal() {
        final String expr = "val a = 1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("final int a = 1;") > 0);
    }

    @Test
    public void explicitValError() {
//Log.testStart();
        final String expr = "val a~String";
        try {
            lang.compile(expr);
            Log.info(lang.toTree());
            fail("should have thrown an exception because no value was assigned");
        } catch (ParseException e) {
            // expected exception
        }
    }

    @Test
    public void explicitValAssign() {
//Log.testStart();
        final String expr = "val a~long = 0";
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("final long a = 0;") > 0);
    }

    @Test
    public void multiVar() {
        final String expr = "var a~int, b~String, c=768, d~double=56";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing a declaration", program.indexOf("int a;") > 0);
        assertTrue("missing b declaration", program.indexOf("String b;") > 0);
        assertTrue("missing c declaration", program.indexOf("int c = 768;") > 0);
        assertTrue("missing d declaration", program.indexOf("double d = 56;") > 0);
    }

    @Test
    public void multiVal() {
        final String expr = "val e=56, f~CharSequence=`Hello";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing e declaration", program.indexOf("final int e = 56;") > 0);
        assertTrue("missing f declaration", program.indexOf("final CharSequence f = \"Hello\";") > 0);
    }

}
