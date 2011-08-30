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
        javaFile.delete();
        classFile.delete();
    }

    @Test
    public void compileHelloWorld() {
        lang.set("file", "examples/helloWorld.neo");
        lang.compile();
        String result = lang.get("output");
        Log.logger.info(result);
        assertTrue("should contain import static", result.contains("import static "));
        assertTrue("should contain path to Console", result.contains("neo.os.Console"));
        assertTrue("should contain a call to println", result.contains("println("));
        assertTrue("should contain the message string", result.contains("\"hello world\""));
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
