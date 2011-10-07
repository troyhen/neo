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
public class IfUnlessTest {

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
    public void ifThenElseEndLine() {
        final String expr
                = "import neo.os\n"
                + "val a = 1\n"
                + "if a < 0 then println \"too low\" else println \"ok\" end if";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (a < 0) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 1", (i1 = program.indexOf("println(\"too low\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 2", (i1 = program.indexOf("println(\"ok\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void ifThenElseLine() {
        final String expr
                = "import neo.os\n"
                + "val a = 1\n"
                + "if a < 0 then println \"too low\" else println \"ok\"";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (a < 0) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 1", (i1 = program.indexOf("println(\"too low\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 2", (i1 = program.indexOf("println(\"ok\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void ifBlock() {
        final String expr
                = "import neo.os\n"
                + "val a = 1\n"
                + "if a > 0\n"
                + "  println \"too high\"\n"
                + "  a = 0\n";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (a > 0) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println", (i1 = program.indexOf("println(\"too high\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing a = 0", (i1 = program.indexOf("a = 0;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 1", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 2", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace 3", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void unlessThenElseEndLine() {
        final String expr
                = "import neo.os\n"
                + "val a = 1\n"
                + "unless a < 0 then println \"too low\" else println \"ok\" end unless";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (!(a < 0)) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 1", (i1 = program.indexOf("println(\"too low\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 2", (i1 = program.indexOf("println(\"ok\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void unlessThenElseLine() {
        final String expr
                = "import neo.os\n"
                + "val a = 1\n"
                + "unless a < 0 then println \"too low\" else println \"ok\"";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (!(a < 0)) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 1", (i1 = program.indexOf("println(\"too low\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 2", (i1 = program.indexOf("println(\"ok\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void unlessThenLine() {
        final String expr
                = "import neo.os\n"
                + "val b = 1\n"
                + "unless b == 1 then return";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (!(b == 1)) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 1", (i1 = program.indexOf("return;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void unlessBlock() {
        final String expr
                = "import neo.os\n"
                + "var tries=0\n"
                + "def ready\n"
                + "    tries == 10\n"
                + "unless ready\n"
                + "    print \"not ready\"\n"
                + "    tries += 1\n"
                + "end unless\n";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing if", (i1 = program.indexOf("if (!(ready())) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing println 1", (i1 = program.indexOf("print(\"not ready\");", i0)) > 0);
        i0 = i1;
        assertTrue("missing tries increment", (i1 = program.indexOf("tries += 1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing close brace", (i1 = program.indexOf("}", i0)) > 0);
        i0 = i1;
        assertTrue("missing ready method", (i1 = program.indexOf("public boolean ready() {", i0)) > 0);
        i0 = i1;
        assertTrue("missing ready condition", (i1 = program.indexOf("return tries == 10;", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void ifExpressionLine() {
        final String expr
                = "var result = if b < 0 then \"too low\" else if b > 10 then \"too high\" else \"ok\"";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing control2 declaration", (i1 = program.indexOf("java.lang.String control2;", i0)) > 0);
        i0 = i1;
        assertTrue("missing outer if", (i1 = program.indexOf("if (b < 0) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control2 assignment 1", (i1 = program.indexOf("control2 = \"too low\";", i0)) > 0);
        i0 = i1;
        assertTrue("missing outer else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control1 declaration", (i1 = program.indexOf("java.lang.String control1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing inner if", (i1 = program.indexOf("if (b > 10) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control1 assignment 1", (i1 = program.indexOf("control1 = \"too high\";", i0)) > 0);
        i0 = i1;
        assertTrue("missing inner else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control1 assignment 1", (i1 = program.indexOf("control1 = \"ok\";", i0)) > 0);
        i0 = i1;
        assertTrue("missing control2 assignment 2", (i1 = program.indexOf("control2 = control1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing result assignment", (i1 = program.indexOf("java.lang.String result = control2;", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void ifExpressionMultiLine() {
        final String expr
                = "var result = if b < 0 then \"too low\"\n"
                + "else if b > 10 then \"too high\"\n"
                + "else \"ok\"";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing control2 declaration", (i1 = program.indexOf("java.lang.String control2;", i0)) > 0);
        i0 = i1;
        assertTrue("missing outer if", (i1 = program.indexOf("if (b < 0) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control2 assignment 1", (i1 = program.indexOf("control2 = \"too low\";", i0)) > 0);
        i0 = i1;
        assertTrue("missing outer else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control1 declaration", (i1 = program.indexOf("java.lang.String control1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing inner if", (i1 = program.indexOf("if (b > 10) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control1 assignment 1", (i1 = program.indexOf("control1 = \"too high\";", i0)) > 0);
        i0 = i1;
        assertTrue("missing inner else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control1 assignment 1", (i1 = program.indexOf("control1 = \"ok\";", i0)) > 0);
        i0 = i1;
        assertTrue("missing control2 assignment 2", (i1 = program.indexOf("control2 = control1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing result assignment", (i1 = program.indexOf("java.lang.String result = control2;", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void ifExpressionBlocks() {
        final String expr
                = "var result = if b < 0\n"
                + "    \"too low\"\n"
                + "else if b > 10\n"
                + "    \"too high\"\n"
                + "else\n"
                + "    \"ok\"\n"
                + "";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing control2 declaration", (i1 = program.indexOf("java.lang.String control2;", i0)) > 0);
        i0 = i1;
        assertTrue("missing outer if", (i1 = program.indexOf("if (b < 0) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control2 assignment 1", (i1 = program.indexOf("control2 = \"too low\";", i0)) > 0);
        i0 = i1;
        assertTrue("missing outer else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control1 declaration", (i1 = program.indexOf("java.lang.String control1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing inner if", (i1 = program.indexOf("if (b > 10) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control1 assignment 1", (i1 = program.indexOf("control1 = \"too high\";", i0)) > 0);
        i0 = i1;
        assertTrue("missing inner else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control1 assignment 1", (i1 = program.indexOf("control1 = \"ok\";", i0)) > 0);
        i0 = i1;
        assertTrue("missing control2 assignment 2", (i1 = program.indexOf("control2 = control1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing result assignment", (i1 = program.indexOf("java.lang.String result = control2;", i0)) > 0);
        i0 = i1;
    }

    @Test
    public void unlessExpressionLine() {
        final String expr
                = "var num = unless passthru then num1 else 0";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing control1 declaration", (i1 = program.indexOf("java.lang.Object control1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing if", (i1 = program.indexOf("if (!toboolean(passthru)) {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control2 assignment 1", (i1 = program.indexOf("control1 = num1;", i0)) > 0);
        i0 = i1;
        assertTrue("missing outer else", (i1 = program.indexOf("} else {", i0)) > 0);
        i0 = i1;
        assertTrue("missing control1 assignment 1", (i1 = program.indexOf("control1 = 0;", i0)) > 0);
        i0 = i1;
        assertTrue("missing num assignment", (i1 = program.indexOf("java.lang.Object num = control1;", i0)) > 0);
        i0 = i1;
    }

}
