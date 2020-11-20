package k_means;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class RGBDot implements Meanable<RGBDot> {
    int x;
    int y;

    int r;
    int g;
    int b;

    public RGBDot(int x, int y, int r, int g, int b) {
        this.x = x;
        this.y = y;

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public RGBDot(int x, int y, int color) {
        // int r = (color>>16) & 0xff;
        // int g =  (color>>8) & 0xff;
        // int b = color & 0xff;
        this(x,y, RGBDot.get_r(color),RGBDot.get_g(color),RGBDot.get_b(color));
    }
    public static int get_r(int color) {
        return (color>>16) & 0xff;
    }
    public static int get_g(int color) {
        return (color>>8) & 0xff;
    }
    public static int get_b(int color) {
        return color & 0xff;
    }
    public static int to_rgb(int r, int g, int b) {
        return (r<<16) | (g<<8) | b;
    }

    public int get_x() {
        return x;
    }
    public int get_y() {
        return y;
    }
    public int get_r() {
        return r;
    }
    public int get_g() {
        return g;
    }
    public int get_b() {
        return b;
    }

    public int to_rgb() {
        return (r<<16) | (g<<8) | b;
    }

    @Override
    public boolean equals(Object obj) {
        RGBDot dot = (RGBDot) obj;

        return r == dot.r && g == dot.g && b == dot.b;
    }

    @Override
    public RGBDot mean(Iterable<RGBDot> iterable) {
        int total_r = 0;
        int total_g = 0;
        int total_b = 0;
        int n = 0;

        for(RGBDot dot: iterable) {
            total_r += dot.get_r();
            total_g += dot.get_g();
            total_b += dot.get_b();
            n += 1;
        }
        // if (n == 0) {
        //     return null;
        // }

        int color = to_rgb(total_r/n, total_g/n, total_b/n);
        return new RGBDot(0,0, color);
    }
}
