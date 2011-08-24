import java.util.logging.Level;
import org.rio.Log;
import org.rio.RioException;
import org.rio.Node;
import org.rio.RioLang;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author theninger
 */
public class ExpressionParseTest {

    private RioLang lang;

    @Before
    public void setUp() {
        Log.logger.setLevel(Level.ALL);
        lang = new RioLang();
    }

    @Test
    public void parseExpression() throws RioException {
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
    public void parseExpression2() throws RioException {
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
