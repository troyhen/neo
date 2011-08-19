import org.dia.lex.Token;
import java.util.List;
import org.dia.core.Xml;
import org.dia.Node;

import org.dia.DiaLang;

//import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;

import static org.dia.core.Xml.*;
import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class XmlTest {

    private DiaLang lang;

    @Before
    public void setUp() {
        lang = new DiaLang();
    }
    
    @Test
    public void testSimple() throws Exception {
        final String simple = "<test/>";
        lang.load(simple);
        List<Token> tokens = lang.tokenize();
        assertEquals(NAME, tokens.get(0).getName());
        assertEquals(simple, tokens.get(0).getValue().toString());
    }

    @Test
    public void testComplex() throws Exception {
        final String complex = "<div id=\"content\">\r\n<p class=\"main\">This is a\n<span>test</span><br/>\r</div>";
        lang.load(complex);
        List<Token> tokens = lang.tokenize();
        assertEquals(NAME, tokens.get(0).getName());
        assertEquals(complex, tokens.get(0).getValue().toString());
    }

}
