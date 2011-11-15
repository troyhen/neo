package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo.NeoLang;
import org.neo.parse.Node;
import org.neo.util.Log;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author theninger
 */
public class NumberTest {

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
        String simple = "10 "
                + "010 "
                + "0x10 "
                + "0b10 "
                + "10l "
                + "010l "
                + "0x10l "
                + "0b10l "
                ;
        lang.load(simple);
        Node tokens = lang.tokenize();
        assertEquals("terminator_bof", tokens.get(0).getName());
        assertEquals("number_integer", tokens.get(1).getName());
        assertEquals(10, tokens.get(1).getValue());
        assertEquals("number_integerOctal", tokens.get(2).getName());
        assertEquals(010, tokens.get(2).getValue());
        assertEquals("number_integerHex", tokens.get(3).getName());
        assertEquals(0x10, tokens.get(3).getValue());
        assertEquals("number_integerBinary", tokens.get(4).getName());
        assertEquals(2, tokens.get(4).getValue());
        assertEquals("number_long", tokens.get(5).getName());
        assertEquals(10l, tokens.get(5).getValue());
        assertEquals("number_longOctal", tokens.get(6).getName());
        assertEquals(010l, tokens.get(6).getValue());
        assertEquals("number_longHex", tokens.get(7).getName());
        assertEquals(0x10l, tokens.get(7).getValue());
        assertEquals("number_longBinary", tokens.get(8).getName());
        assertEquals(2l, tokens.get(8).getValue());
        assertEquals("terminator_eof", tokens.get(9).getName());
    }

}
