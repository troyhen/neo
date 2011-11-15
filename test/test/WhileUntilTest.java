package test;

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
public class WhileUntilTest {

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
    public void whileLine() {
        final String expr
                = "var a = 0\n"
                + "while a < 10 do a = a + 1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing while", (i1 = program.indexOf("while (a < 10) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing increment", (i1 = program.indexOf("a = (a + 1);", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void statementWhile() {
        final String expr
                = "var a = 0\n"
                + "a = a + 1 while a < 10";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing while", (i1 = program.indexOf("while (a < 10) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing increment", (i1 = program.indexOf("a = (a + 1);", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void whileBlock() {
        final String expr
                = "import neo.os\n"
                + "var a = 10\n"
                + "while a > 0\n"
                + "    println \"too high\"\n"
                + "    a -= 1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing while", (i1 = program.indexOf("while (a > 0) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println", (i1 = program.indexOf("println(\"too high\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing decrement", (i1 = program.indexOf("a -= 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void untilLine() {
        final String expr
                = "var b = 10\n"
                + "until b == 1 do b -= 1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing while", (i1 = program.indexOf("while (!(equal(b, 1))) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing decrement", (i1 = program.indexOf("b -= 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void statementUntil() {
        final String expr
                = "var b = 10\n"
                + "b -= 1 until b == 1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing while", (i1 = program.indexOf("while (!(equal(b, 1))) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing decrement", (i1 = program.indexOf("b -= 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void untilBlockEnd() {
        final String expr
                = "import neo.os\n"
                + "var b = 10\n"
                + "until b >= 0\n"
                + "    println \"not ready\"\n"
                + "    b -= 1\n"
                + "end until";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing while", (i1 = program.indexOf("while (!(b >= 0)) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println", (i1 = program.indexOf("println(\"not ready\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing decrement", (i1 = program.indexOf("b -= 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void whileElseLine() {
        final String expr
                = "import neo.os\n"
                + "var a = 0\n"
                + "while a < 0 do a += 1 else println \"ready\"";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (a < 0) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing do", (i1 = program.indexOf("do {", i0)) > 0);
        i0 = i1;
        assertTrue("missing increment", (i1 = program.indexOf("a += 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close while", (i1 = program.indexOf("} while (a < 0);", i0)) > 0);
        i0 = i1;
        assertTrue("missing else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println", (i1 = program.indexOf("println(\"ready\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void whileElse2Line() {
        final String expr
                = "import neo.os\n"
                + "var a = 0\n"
                + "while a < 0 do a += 1\n"
                + "else println \"ready\"";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (a < 0) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing do", (i1 = program.indexOf("do {", i0)) > 0);
        i0 = i1;
        assertTrue("missing increment", (i1 = program.indexOf("a += 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close while", (i1 = program.indexOf("} while (a < 0);", i0)) > 0);
        i0 = i1;
        assertTrue("missing else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println", (i1 = program.indexOf("println(\"ready\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void whileElseMultiLine() {
        final String expr
                = "import neo.os\n"
                + "var a = 0\n"
                + "while a < 0\n"
                + "    a += 1\n"
                + "    println \"not ready\"\n"
                + "else\n"
                + "    println \"ready\"";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (a < 0) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing do", (i1 = program.indexOf("do {", i0)) > 0);
        i0 = i1;
        assertTrue("missing increment", (i1 = program.indexOf("a += 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 1", (i1 = program.indexOf("println(\"not ready\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close while", (i1 = program.indexOf("} while (a < 0);", i0)) > 0);
        i0 = i1;
        assertTrue("missing else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 2", (i1 = program.indexOf("println(\"ready\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void untilElseLine() {
        final String expr
                = "import neo.os\n"
                + "var a = 0\n"
                + "until a > 10 do a += 1 else println \"ready\"";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (!(a > 10)) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing do", (i1 = program.indexOf("do {", i0)) > 0);
        i0 = i1;
        assertTrue("missing increment", (i1 = program.indexOf("a += 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close while", (i1 = program.indexOf("} while (!(a > 10));", i0)) > 0);
        i0 = i1;
        assertTrue("missing else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println", (i1 = program.indexOf("println(\"ready\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void untilElseLineEnd() {
        final String expr
                = "import neo.os\n"
                + "var a = 0\n"
                + "until a > 10 do a += 1 else println \"ready\" end";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (!(a > 10)) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing do", (i1 = program.indexOf("do {", i0)) > 0);
        i0 = i1;
        assertTrue("missing increment", (i1 = program.indexOf("a += 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close while", (i1 = program.indexOf("} while (!(a > 10));", i0)) > 0);
        i0 = i1;
        assertTrue("missing else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println", (i1 = program.indexOf("println(\"ready\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void untilElseLineEndUntil() {
        final String expr
                = "import neo.os\n"
                + "var a = 0\n"
                + "until a > 10 do a += 1 else println \"ready\" end until";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (!(a > 10)) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing do", (i1 = program.indexOf("do {", i0)) > 0);
        i0 = i1;
        assertTrue("missing increment", (i1 = program.indexOf("a += 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close while", (i1 = program.indexOf("} while (!(a > 10));", i0)) > 0);
        i0 = i1;
        assertTrue("missing else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println", (i1 = program.indexOf("println(\"ready\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

}
