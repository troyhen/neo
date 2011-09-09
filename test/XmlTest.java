import org.neo.Node;

import org.neo.NeoLang;

import org.junit.Before;
import org.junit.Test;

import static org.neo.core.Xml.*;
import static org.junit.Assert.*;

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
    
    @Test
    public void testSimple() throws Exception {
        final String simple = "<test/>";
        lang.load(simple);
        Node tokens = lang.tokenize();
        assertEquals(NAME, tokens.get(0).getName());
        assertEquals(simple, tokens.get(0).getValue().toString());
    }

    @Test
    public void testComplex() throws Exception {
        final String complex = "<div id=\"content\">\r\n<p class=\"main\">This is a\n<span>test</span><br/> of the xml parser</p>\r</div>";
        lang.load(complex);
        Node tokens = lang.tokenize();
        assertEquals(NAME, tokens.get(0).getName());
        assertEquals(complex, tokens.get(0).getValue().toString());
    }

}
