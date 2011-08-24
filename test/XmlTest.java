import org.rio.Node;

import org.rio.RioLang;

import org.junit.Before;
import org.junit.Test;

import static org.rio.core.Xml.*;
import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class XmlTest {

    private RioLang lang;

    @Before
    public void setUp() {
        lang = new RioLang();
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
        final String complex = "<div id=\"content\">\r\n<p class=\"main\">This is a\n<span>test</span><br/>\r</div>";
        lang.load(complex);
        Node tokens = lang.tokenize();
        assertEquals(NAME, tokens.get(0).getName());
        assertEquals(complex, tokens.get(0).getValue().toString());
    }

}
