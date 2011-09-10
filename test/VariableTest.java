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
public class VariableTest {

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
    public void implicitVar() {
        final String expr = "var a = 1";
//Log.logger.setLevel(Level.ALL);
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("int a = 1;") > 0);
    }

    @Test
    public void explicitVar() {
//Log.logger.setLevel(Level.ALL);
        final String expr = "var a~String";
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("String a;") > 0);
    }

    @Test
    public void explicitVarAssign() {
//Log.logger.setLevel(Level.ALL);
        final String expr = "var a~long = 0";
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("long a = 0;") > 0);
    }

    @Test
    public void implicitVal() {
        final String expr = "val a = 1";
//Log.logger.setLevel(Level.ALL);
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("final int a = 1;") > 0);
    }

    @Test
    public void explicitVal() {
//Log.logger.setLevel(Level.ALL);
        final String expr = "val a~String";
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("final String a;") > 0);
    }

    @Test
    public void explicitValAssign() {
//Log.logger.setLevel(Level.ALL);
        final String expr = "val a~long = 0";
        lang.compile(expr);
//lang.printTree();
        String program = lang.get("output");
//System.out.println(program);
        assertTrue("missing declaration", program.indexOf("final long a = 0;") > 0);
    }

}
