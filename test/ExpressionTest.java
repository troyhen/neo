import org.dia.lex.Token;
import java.util.List;
import org.dia.DiaLang;
import org.dia.Node;
//import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;

import static org.dia.core.Operator.*;
import static org.junit.Assert.*;

/**
 *
 * @author theninger
 */
public class ExpressionTest {

//    public ExpressionTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//    }
//
//    @AfterClass
//    public static void tearDownClass() throws Exception {
//    }
//
    private DiaLang lang;

    @Before
    public void setUp() {
        lang = new DiaLang();
    }

//    @After
//    public void tearDown() {
//    }

    @Test
    public void testPlus() throws Exception {
        String simple = "1+2.0-8d";
        lang.load(simple);
        List<Token> tokens = lang.tokenize();
        assertEquals(OPERATOR_ADD, tokens.get(1).getName());
        assertEquals("+", tokens.get(1).getValue().toString());
    }

}
