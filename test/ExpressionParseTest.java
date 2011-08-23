import java.util.logging.Level;
import org.dia.Log;
import org.dia.DiaException;
import org.dia.Node;
import org.dia.DiaLang;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author theninger
 */
public class ExpressionParseTest {

    private DiaLang lang;

    @Before
    public void setUp() {
        Log.logger.setLevel(Level.ALL);
        lang = new DiaLang();
    }

    @Test
    public void parseExpression() throws DiaException {
        final String expr = "1+2";
        lang.load(expr);
        Node root = lang.parse();
        assertNotNull(root);
        assertEquals(1, root.countChildren());
        assertEquals("statements", root.get(0).getName());
        assertEquals("statement", root.get(0).get(0).getName());
        assertEquals("expression", root.get(0).get(0).get(0).getName());
    }

    @Test
    public void parseExpression2() throws DiaException {
        final String expr = "1+2*5";
        lang.load(expr);
        Node root = lang.parse();
        assertNotNull(root);
        assertEquals(1, root.countChildren());
        assertEquals("statements", root.get(0).getName());
        assertEquals("statement", root.get(0).get(0).getName());
        assertEquals("expression", root.get(0).get(0).get(0).getName());
    }

}
