import java.util.logging.Level;
import org.junit.After;
import java.io.File;
import org.neo.Log;
import org.neo.NeoLang;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author theninger
 */
public class HelloWorldTest {

    private NeoLang lang;
    private File neoFile, javaFile, classFile;

    @Before
    public void setUp() {
        lang = new NeoLang();
        neoFile = new File("examples/helloWorld.neo");
        javaFile = new File("examples/helloWorld.java");
        classFile = new File("examples/helloWorld.class");
    }

    @After
    public void tearDown() {
        Log.testStop();
        javaFile.delete();
        classFile.delete();
    }

    @Test
    public void compileHelloWorld() {
//Log.testStart();
        lang.set("file", "examples/helloWorld.neo");
        lang.compile();
        Log.info(lang.toTree());
        String result = lang.get("output");
        Log.info(result);
        assertTrue("missing import static", result.contains("import static "));
        assertTrue("missing path to Console", result.contains("neo.os.N.*"));
        assertTrue("missing call to println", result.contains("println("));
        assertTrue("missing message string", result.contains("\"hello world\""));
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
        lang.compile();
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
