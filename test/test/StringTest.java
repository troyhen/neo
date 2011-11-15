package test;

import org.neo.util.Log;
import org.junit.After;
import org.neo.core.Strings;
import org.neo.NeoLang;
import org.neo.parse.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author theninger
 */
public class StringTest {

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
        String simple = "\"this is a string\" "
                + "'this is another' "
                + "`word "
                + "\"\"\"This\nis\na\nvery\nlong\nstring\"\"\" ";
        lang.load(simple);
        Node tokens = lang.tokenize();
        assertEquals("terminator_bof", tokens.get(0).getName());
        assertEquals(Strings.STRING_DOUBLE, tokens.get(1).getName());
        assertEquals(Strings.STRING_SINGLE, tokens.get(2).getName());
        assertEquals(Strings.STRING_WORD, tokens.get(3).getName());
        assertEquals(Strings.STRING_MULTILINE, tokens.get(4).getName());
    }

}
