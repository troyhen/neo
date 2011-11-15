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
public class EqualsTest {

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
    public void testEquals() {
        final String expr
                = "val a = `word\n"
                + "val b = `wo\n"
                + "val c = `rd\n"
                + "val d = b + c\n"
                + "val ad1 = a == d         # true\n"
                + "val ad2 = a === d        # false\n"
                + "val ad3 = a === d.intern # true\n"
                + "val ad4 = a ~= `word     # true\n"
                + "val aS = a ~= String     # true\n"
                + "val aa = a ~= /\\w+/     # true";
//Log.testStart();
        lang.compile(expr);
        Log.info(lang.toTree());
        String program = lang.get("output");
        Log.info(program);
        int i0 = 0, i1;
        assertTrue("missing a declaration", (i1 = program.indexOf("final java.lang.String a = \"word\";", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing b declaration", (i1 = program.indexOf("final java.lang.String b = \"wo\";", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing c declaration", (i1 = program.indexOf("final java.lang.String c = \"rd\";", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing d declaration", (i1 = program.indexOf("final java.lang.String d = b + c;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing ad1", (i1 = program.indexOf("final boolean ad1 = equal(a, d);", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing ad2", (i1 = program.indexOf("final boolean ad2 = a == d;", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing ad3", (i1 = program.indexOf("final boolean ad3 = a == d.intern();", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing ad4", (i1 = program.indexOf("final java.lang.Object ad4 = match(a, \"word\");", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing aS", (i1 = program.indexOf("final java.lang.Object aS = match(a, String);", i0)) > i0);
        i0 = i1 + 1;
        assertTrue("missing aa", (i1 = program.indexOf("final java.lang.Object aa = match(a, java.util.regex.Pattern.compile(\"\\\\w+\"));", i0)) > i0);
        i0 = i1 + 1;
    }

}
