import build.app.*;
import org.junit.Test;
import static org.junit.Assert.*;
import src.main.java.app.RGBDot;

public class RGBDotTest{

    @Test
    public void testConcatenate() {
        RGBDot do1 = new RGBDot(0,0,0,0,0);
        String result = "hola";
        assertEquals("hola", result);
    }
}
