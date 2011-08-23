import org.dia.DiaLang;
import org.dia.Node;
import org.junit.Before;
import org.junit.Test;

import static org.dia.core.Operator.*;
import static org.junit.Assert.*;

/**
 *
 * @author theninger
 */
public class ExpressionTest {

    private DiaLang lang;

    @Before
    public void setUp() {
        lang = new DiaLang();
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
