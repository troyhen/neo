import java.util.logging.Level;
import org.dia.Log;
import org.dia.DiaException;
import org.dia.Node;
import org.dia.DiaLang;
import org.junit.After;
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

    @After
    public void tearDown() {
    }

    @Test
    public void parseExpression() throws DiaException {
        final String expr = "1+2";
        lang.load(expr);
        Node tokens = lang.tokenize();
        Node root = lang.parse();
        assertNotNull(root);    // TODO need real test
    }

}
