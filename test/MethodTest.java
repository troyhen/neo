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
public class MethodTest {

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
    public void emptyParens() {
        final String expr
                = "import neo.os\n"
                + "def test()\n"
                + "    println 'hello'";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("void test()") > 0);
        assertTrue("missing open brace", program.indexOf("{") > 0);
        assertTrue("missing println", program.indexOf("println(\"hello\");") > 0);
        assertTrue("missing close brace", program.indexOf("}") > 0);
    }

    @Test
    public void noParens() {
        final String expr
                = "import neo.os\n"
                + "def test\n"
                + "    println 'hello'\n";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("void test()") > 0);
        assertTrue("missing open brace", program.indexOf("{") > 0);
        assertTrue("missing println", program.indexOf("println(\"hello\");") > 0);
        assertTrue("missing close brace", program.indexOf("}") > 0);
    }

    @Test
    public void splitArgs() {
        final String expr
                = "import neo.os\n"
                + "def test(a~int\n"
                + "b~String)\n"
                + "    println a + b\n"
                + "    0\n";
Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("int test(int a, String b)") > 0);
        assertTrue("missing open brace", program.indexOf("{") > 0);
        assertTrue("missing println", program.indexOf("println(a + b);") > 0);
        assertTrue("missing return", program.indexOf("return 0;") > 0);
        assertTrue("missing close brace", program.indexOf("}") > 0);
    }

    @Test
    public void noParenArgs() {
        final String expr
                = "import neo.os\n"
                + "def test a~int b~String\n"
                + "    println a + b\n";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("void test(int a, String b)") > 0);
        assertTrue("missing open brace", program.indexOf("{") > 0);
        assertTrue("missing println", program.indexOf("println(a + b);") > 0);
        assertTrue("missing close brace", program.indexOf("}") > 0);
    }

    @Test
    public void typeDeclared() {
        final String expr
                = "import neo.os\n"
                + "def test~int c~int b~String\n"
                + "    println b + c\n"
                + "    0";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        assertTrue("missing declaration", program.indexOf("int test(int c, String b)") > 0);
        assertTrue("missing open brace", program.indexOf("{") > 0);
        assertTrue("missing println", program.indexOf("println(b + c);") > 0);
        assertTrue("missing return", program.indexOf("return 0;") > 0);
        assertTrue("missing close brace", program.indexOf("}") > 0);
    }

}
