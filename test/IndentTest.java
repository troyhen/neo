import org.neo.Log;
import org.junit.After;
import org.neo.lex.LexException;
import org.neo.NeoLang;
import org.neo.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class IndentTest {

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
    public void testTokenizer() {
        String simple = "1\n"
                + "  2\n"
                + "    3\n"
                + "  4";
        lang.load(simple);
        Node tokens = lang.tokenize();
        Node node = tokens.getFirst();
        assertEquals("number_integer", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("start_block", node.getName());
        node = node.getNext();
        assertEquals("number_integer", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("start_block", node.getName());
        node = node.getNext();
        assertEquals("number_integer", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("end_block", node.getName());
        node = node.getNext();
        assertEquals("number_integer", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("end_block", node.getName());
        node = node.getNext();
        assertEquals("eof", node.getName());
    }

    @Test
    public void testComments() {
        String simple = "1\n"
                + "      # test\n"
                + "  2\n"
                + "# test\n"
                + "    3\n"
                + "  # test\n"
                + "  4";
        lang.load(simple);
        Node tokens = lang.tokenize();
        Node node = tokens.getFirst();
        assertEquals("number_integer", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("start_block", node.getName());
        node = node.getNext();
        assertEquals("number_integer", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("start_block", node.getName());
        node = node.getNext();
        assertEquals("number_integer", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("end_block", node.getName());
        node = node.getNext();
        assertEquals("number_integer", node.getName());
        node = node.getNext();
        assertEquals("eol", node.getName());
        node = node.getNext();
        assertEquals("end_block", node.getName());
        node = node.getNext();
        assertEquals("eof", node.getName());
    }

    @Test
    public void testError() {
        String simple = "1\n"
                + "    2\n"
                + "\t   3";
        lang.load(simple);
        try {
            lang.tokenize();
            fail("Should have thrown an Exception");
        } catch (LexException e) {
        }
    }

}
