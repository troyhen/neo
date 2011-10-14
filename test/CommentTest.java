import org.neo.parse.Engine;
import org.neo.util.Log;
import org.neo.lex.LexerEof;
import org.neo.parse.Node;
import org.neo.NeoLang;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.neo.core.Whitespace.*;
import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class CommentTest {

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
        final String single = "#This is a test";//"//This is a test";
        lang.load(single);
        Node tokens = lang.tokenize();
        assertEquals(1, Engine.engine().getLine());
        assertEquals(LexerEof.EOF, tokens.get(0).getName());
    }

    @Test
    public void testSimpleCrlf() throws Exception {
        final String singlecrlf = "#This is a test\r\n";//"//This is a test\r\n";
        lang.load(singlecrlf);
        Node tokens = lang.tokenize();
        assertEquals(2, Engine.engine().getLine());
        assertEquals(EOL, tokens.get(0).getName());
    }

    @Test
    public void testSlashStar() throws Exception {
        final String slashstar = "###This is a comment###";//"/*This is a comment*/";
        lang.load(slashstar);
        Node tokens = lang.tokenize();
        assertEquals(LexerEof.EOF, tokens.get(0).getName());
    }

    @Test
    public void testMultiline() throws Exception {
        final String multiline = "###This\r\nis\na\rtest###";//"/*This\r\nis\na\rtest*/";
        lang.load(multiline);
        Node tokens = lang.tokenize();
        assertEquals(Engine.engine().getLine(), 4);
        assertEquals(LexerEof.EOF, tokens.get(0).getName());
    }

    @Test
    public void testCombined() throws Exception {
        final String combined = "\r\n#Comment 1\r\n#########\r\nComment\r\n#########\r\n";//"\r\n//Comment 1\r\n/*\r\nComment\r\n*/\r\n";
        lang.load(combined);
        Node tokens = lang.tokenize();
        assertEquals(EOL, tokens.get(0).getName());
        assertEquals(EOL, tokens.get(1).getName());
        assertEquals(EOL, tokens.get(2).getName());
        assertEquals(LexerEof.EOF, tokens.get(3).getName());
    }

}
