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

    @Before
    public void setUp() {
        lang = new NeoLang();
    }

    @Test
    public void testHelloWorld() {
        lang.set("file", "examples/helloWorld.neo");
        lang.compile();
        String result = lang.get("output");
        Log.logger.info(result);
        assertTrue("should contain import static", result.contains("import static "));
        assertTrue("should contain path to Console", result.contains("neo.os.Console"));
        assertTrue("should contain a call to println", result.contains("println("));
        assertTrue("should contain the message string", result.contains("hello world"));
    }

}
