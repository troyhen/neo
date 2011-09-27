import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo.Log;
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
        lang.set("file", "examples/helloWorld.neo");
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
