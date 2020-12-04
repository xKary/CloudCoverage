package app;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Clase que modela un punto con atributos RGB y coordenadas x,y.
 */
public class RGBDot {
    int x;
    int y;

    int r;
    int g;
    int b;
    /**
     * Constructor por parámetros.
     * @param x Coordenada x.
     * @param y Coordenada y.
     * @param r Componente rojo.
     * @param g Componente verde.
     * @param b Componente azul.
     */
    public RGBDot(int x, int y, int r, int g, int b) {
        this.x = x;
        this.y = y;

        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Constructor por parámetros.
     * @param x Coordenada x.
     * @param y Coordenada y.
     * @param color Color del punto.
     */
    public RGBDot(int x, int y, int color) {
        this(x,y, RGBDot.get_r(color),RGBDot.get_g(color),RGBDot.get_b(color));
    }

    /**
     * Método que regresa el valor del componente rojo del color dado.
     * @param color Color del que se obtendrá el valor del componente.
     * @return int Valor del componente rojo del color.
     */
    public static int get_r(int color) {
        return (color>>16) & 0xff;
    }

    /**
     * Método que regresa el valor del componente rojo del color dado.
     * @param color Color del que se obtendrá el valor del componente.
     * @return int Valor del componente verde del color.
     */
    public static int get_g(int color) {
        return (color>>8) & 0xff;
    }

    /**
     * Método que regresa el valor del componente rojo del color dado.
     * @param color Color del que se obtendrá el valor del componente.
     * @return int Valor del componente azul del color.
     */
    public static int get_b(int color) {
        return color & 0xff;
    }

    /**
     * Método qué codifica los componentes rojo, verde y azul de un color (RGB) a
     * un solo valor entero.
     * @return int RGB codificado a un solo valor entero.
     */
    public static int to_rgb(int r, int g, int b) {
        return (r<<16) | (g<<8) | b;
    }

    /**
     * Método qué regresa la coordenada x del punto.
     * @return int Coordenada x del punto.
     */
    public int get_x() {
        return x;
    }

    /**
     * Método qué regresa la coordenada y del punto.
     * @return int Coordenada y del punto.
     */
    public int get_y() {
        return y;
    }

    /**
     * Método qué regresa el valor del componente rojo del punto.
     * @return int Valor del componente rojo del punto.
     */
    public int get_r() {
        return r;
    }

    /**
     * Método qué regresa el valor del componente verde del punto.
     * @return int Valor del componente verde del punto.
     */
    public int get_g() {
        return g;
    }

    /**
     * Método qué regresa el valor del componente azul del punto.
     * @return int Valor del componente azul del punto.
     */
    public int get_b() {
        return b;
    }

    /**
     * Método qué codifica los componentes rojo, verde y azul del punto a
     * un solo valor entero.
     * @return int RGB codificado a un solo valor entero.
     */

    public int to_rgb() {
        return (r<<16) | (g<<8) | b;
    }

    /**
     * Método que detertima si dos objetos RGBDot son iguales.
     * @param obj Objeto con el que se comparará.
     * @return boolean True si son iguales, false en otro caso.
     */
    @Override
    public boolean equals(Object obj) {
        RGBDot dot = (RGBDot) obj;

        return r == dot.r && g == dot.g && b == dot.b;
    }

    // /**
    //  * Método que calcula el promedio de una lista de puntos.
    //  * @param iterable Objeto contiene los puntos con los que se
    //  *                 calculará el promedio
    //  * @return RGBDot Objeto RGBDot con el color promedio.
    //  */
    //  @Override
    // public RGBDot mean(Iterable<RGBDot> iterable) {
    //     int total_r = 0;
    //     int total_g = 0;
    //     int total_b = 0;
    //     int n = 0;
    //
    //     for(RGBDot dot: iterable) {
    //         total_r += dot.get_r();
    //         total_g += dot.get_g();
    //         total_b += dot.get_b();
    //         n += 1;
    //     }
    //
    //     int color = to_rgb(total_r/n, total_g/n, total_b/n);
    //     return new RGBDot(0,0, color);
    // }
}
