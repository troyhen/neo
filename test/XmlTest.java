import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.neo.NeoLang;
import org.neo.util.Log;
import org.neo.parse.Node;

import static org.neo.core.Xml.*;

/**
 *
 * @author Troy Heninger
 */
public class XmlTest {

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
    public void testSimple() throws Exception {
        final String simple = "<test/>";
        lang.load(simple);
        Node tokens = lang.tokenize();
        assertTrue("Bad name", tokens.get(1).isNamed(NAME));
        assertEquals("Bad text", simple, tokens.get(1).getValue().toString());
    }

    @Test
    public void testComplex() throws Exception {
        final String complex = "<div id=\"content\">\r\n<p class=\"main\">This is a\n<span>test</span><br/> of the xml parser</p>\r</div>";
        lang.load(complex);
        Node tokens = lang.tokenize();
        assertEquals(NAME, tokens.get(1).getName());
        assertEquals(complex, tokens.get(1).getValue().toString());
    }

}
