package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo.NeoLang;
import org.neo.core.Strings;
import org.neo.parse.Node;
import org.neo.util.Log;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author theninger
 */
public class CharTest {

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
        String simple = "?a "
                + "?02 "
                + "?\\12 "
                + "?\\xab "
                + "?\\u1234 ";
        lang.load(simple);
        Node tokens = lang.tokenize();
        assertEquals("terminator_bof", tokens.get(0).getName());
        assertEquals("character", tokens.get(1).getName());
        assertEquals('a', tokens.get(1).getValue());
        assertEquals("character", tokens.get(2).getName());
        assertEquals('0', tokens.get(2).getValue());
        assertEquals("number_integer", tokens.get(3).getName());
        assertEquals("character", tokens.get(4).getName());
        assertEquals('\12', tokens.get(4).getValue());
        assertEquals("character", tokens.get(5).getName());
        assertEquals('\u00ab', tokens.get(5).getValue());
        assertEquals("character", tokens.get(6).getName());
        assertEquals('\u1234', tokens.get(6).getValue());
        assertEquals("terminator_eof", tokens.get(7).getName());
    }

}
