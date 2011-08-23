import org.dia.lex.LexerEof;
import org.dia.Node;
import org.dia.DiaLang;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.dia.core.Whitespace.*;
import static org.junit.Assert.*;

/**
 *
 * @author Troy Heninger
 */
public class CommentTest {

    private DiaLang lang;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        lang = new DiaLang();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSimple() throws Exception {
        final String single = "//This is a test";
        lang.load(single);
        Node tokens = lang.tokenize();
        assertEquals(1, lang.getLine());
        assertEquals(LexerEof.EOF, tokens.get(0).getName());
    }

    @Test
    public void testSimpleCrlf() throws Exception {
        final String singlecrlf = "//This is a test\r\n";
        lang.load(singlecrlf);
        Node tokens = lang.tokenize();
        assertEquals(2, lang.getLine());
        assertEquals(EOL, tokens.get(0).getName());
    }

    @Test
    public void testSlashStar() throws Exception {
        final String slashstar = "/*This is a comment*/";
        lang.load(slashstar);
        Node tokens = lang.tokenize();
        assertEquals(LexerEof.EOF, tokens.get(0).getName());
    }

    @Test
    public void testMultiline() throws Exception {
        final String multiline = "/*This\r\nis\na\rtest*/";
        lang.load(multiline);
        Node tokens = lang.tokenize();
        assertEquals(lang.getLine(), 4);
        assertEquals(LexerEof.EOF, tokens.get(0).getName());
    }

    @Test
    public void testCombined() throws Exception {
        final String combined = "\r\n//Comment 1\r\n/*\r\nComment\r\n*/\r\n";
        lang.load(combined);
        Node tokens = lang.tokenize();
        assertEquals(EOL, tokens.get(0).getName());
        assertEquals(EOL, tokens.get(1).getName());
        assertEquals(EOL, tokens.get(2).getName());
        assertEquals(LexerEof.EOF, tokens.get(3).getName());
    }

}
