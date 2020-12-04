import org.junit.Test;
import static org.junit.Assert.*;
import app.RGBDot;

public class RGBDotTest{

    @Test
    public void testGetCoordinates() {
        RGBDot dot = new RGBDot(10,20,0,0,0);
        assertEquals(10,dot.get_x());
        assertEquals(20,dot.get_y());
    }

    @Test
    public void testGetRGB() {
        RGBDot dot = new RGBDot(10,20,0,100,90);
        assertEquals(0,dot.get_r());
        assertEquals(100,dot.get_g());
        assertEquals(90,dot.get_b());
    }

    @Test
    public void testColorToRGB() {
        int color = 25690;
        assertEquals(0,RGBDot.get_r(color));
        assertEquals(100,RGBDot.get_g(color));
        assertEquals(90,RGBDot.get_b(color));
    }

    @Test
    public void testToRGB() {
        RGBDot dot = new RGBDot(10,20,0,100,90);
        assertEquals(25690,dot.to_rgb());
    }

    @Test
    public void testEquals() {
        RGBDot dot = new RGBDot(10,20,0,100,90);
        RGBDot dot1 = new RGBDot(10,20,100,90,0);
        assertEquals(false,dot.equals(dot1));
    }
}
