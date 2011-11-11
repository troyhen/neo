import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo.util.Log;
import org.neo.NeoLang;

import static org.junit.Assert.*;

/**
 *
 * @author theninger
 */
public class HelloWorldTest {

    private NeoLang lang;
    private File neoFile, javaFile, classFile;
    private File classFile2;

    @Before
    public void setUp() {
        lang = new NeoLang();
        neoFile = new File("examples/HelloWorld.neo");
        javaFile = new File("examples/HelloWorld.java");
        classFile = new File("examples/HelloWorld.class");
        classFile2 = new File("examples/HelloWorld$1.class");
    }

    @After
    public void tearDown() {
        Log.testStop();
        javaFile.delete();
        classFile.delete();
        classFile2.delete();
    }

    @Test
    public void compileHelloWorld() {
//Log.testStart();
        lang.set("file", "examples/HelloWorld.neo");
        lang.compile();
        Log.info(lang.toTree());
        String result = lang.get("output");
        Log.info(result);
        assertTrue("missing import static", result.contains("import static "));
        assertTrue("missing path to Console", result.contains("neo.os.N.*"));
        assertTrue("missing call to println", result.contains("println("));
        assertTrue("missing message string", result.contains("\"Hello World\""));
    }

    @Test
    public void javacHelloWorld() {
        lang.set("file", neoFile.getPath());
        lang.set("save", javaFile.getPath());
        lang.set("class", "javac");
        lang.compile();
        assertTrue("class file missing", classFile.exists());
        assertTrue("file is too small", classFile.length() > 100);
    }

    @Test
    public void runHelloWorld() {
        lang.set("file", neoFile.getPath());
        lang.set("save", javaFile.getPath());
        lang.set("class", "run");
        PrintStream out = System.out;
        final ByteArrayOutputStream newOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newOut));
        lang.compile();
        System.setOut(out);
        String result = newOut.toString();
        assertEquals("output doesn't match", "Hello World\r\n"
                + "Hello World\r\n"
                + "Hello World\r\n"
                + "Hello World\r\n"
                + "Hello World Hello World\r\n"
                + "Hello World\r\n", result);
    }

    @Test
    public void writeHelloWorld() {
        lang.set("file", neoFile.getPath());
        lang.set("save", javaFile.getPath());
        lang.compile();
        assertTrue("java file missing", javaFile.exists());
        assertTrue("file is too small", javaFile.length() > 100);
    }
}
