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
public class ClassTest {

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
    public void testClassDef() {
        final String expr
                = "class Test\n"
                + "    def method\n"
                + "        1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing import", (i1 = program.indexOf("import neo.lang.*;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing import static", (i1 = program.indexOf("import static neo.lang.N.*;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing class declaration", (i1 = program.indexOf("class Test {", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing method declaration", (i1 = program.indexOf("public int method() {", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing return", (i1 = program.indexOf("return 1;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing method close", (i1 = program.indexOf("}", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing class close", (i1 = program.indexOf("}", i0)) > i0);
        i0 = i1 + 1;
    }

    @Test
    public void testClassExtends() {
        final String expr
                = "class Test~Object\n"
                + "    def method\n"
                + "        1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing import", (i1 = program.indexOf("import neo.lang.*;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing import static", (i1 = program.indexOf("import static neo.lang.N.*;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing class declaration", (i1 = program.indexOf("class Test extends Object {", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing method declaration", (i1 = program.indexOf("public int method() {", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing return", (i1 = program.indexOf("return 1;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing method close", (i1 = program.indexOf("}", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing class close", (i1 = program.indexOf("}", i0)) > i0);
        i0 = i1 + 1;
    }

    @Test
    public void testClassImplements() {
        final String expr
                = "class Test Runnable\n"
                + "    def method\n"
                + "        1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing import", (i1 = program.indexOf("import neo.lang.*;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing import static", (i1 = program.indexOf("import static neo.lang.N.*;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing class declaration", (i1 = program.indexOf("class Test implements Runnable {", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing method declaration", (i1 = program.indexOf("public int method() {", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing return", (i1 = program.indexOf("return 1;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing method close", (i1 = program.indexOf("}", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing class close", (i1 = program.indexOf("}", i0)) > i0);
        i0 = i1 + 1;
    }

    @Test
    public void testClassImplements2() {
        final String expr
                = "class Test Runnable java.io.Serializable\n"
                + "    def method\n"
                + "        1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing import", (i1 = program.indexOf("import neo.lang.*;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing import static", (i1 = program.indexOf("import static neo.lang.N.*;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing class declaration", (i1 = program.indexOf("class Test implements Runnable, java.io.Serializable {", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing method declaration", (i1 = program.indexOf("public int method() {", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing return", (i1 = program.indexOf("return 1;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing method close", (i1 = program.indexOf("}", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing class close", (i1 = program.indexOf("}", i0)) > i0);
        i0 = i1 + 1;
    }

    @Test
    public void testClassExtendsImplements() {
        final String expr
                = "class Test~java.lang.Object java.lang.Runnable\n"
                + "    def method\n"
                + "        1";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing import", (i1 = program.indexOf("import neo.lang.*;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing import static", (i1 = program.indexOf("import static neo.lang.N.*;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing class declaration", (i1 = program.indexOf("class Test extends java.lang.Object implements java.lang.Runnable {", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing method declaration", (i1 = program.indexOf("public int method() {", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing return", (i1 = program.indexOf("return 1;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing method close", (i1 = program.indexOf("}", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing class close", (i1 = program.indexOf("}", i0)) > i0);
        i0 = i1 + 1;
    }

}