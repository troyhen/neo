import org.rio.RioLang;
import org.rio.Node;
import org.junit.Before;
import org.junit.Test;

import static org.rio.core.Operator.*;
import static org.junit.Assert.*;

/**
 *
 * @author theninger
 */
public class ExpressionTest {

    private RioLang lang;

    @Before
    public void setUp() {
        lang = new RioLang();
    }

    @Test
    public void testPlus() throws Exception {
        String simple = "1+2.0-8d";
        lang.load(simple);
        Node tokens = lang.tokenize();
        assertEquals(OPERATOR_ADD, tokens.get(1).getName());
        assertEquals("+", tokens.get(1).getValue().toString());
    }

}
