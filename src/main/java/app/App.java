package app;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.imageio.ImageIO;

import java.util.LinkedList;
import java.util.function.Predicate;

/**
 * Clase qué se encarga de determinar el Índice de Cobertura Nubosa de una
 * imagen del cielo, y generar su versión en blanco y negro si se lo
 * solicitan.
 */
public class App {

    public static String folder = "src/main/resources/";
    public static String maskName = "mask.png";
    public static String inputName = folder;
    public static String outputName = folder;
    public static boolean generateImg = false;

    public static void main(String args[])throws IOException {

        BufferedImage img = null;
        BufferedImage mask = null;

        try {
            checkParameters(args);
        } catch (Exception e) {
            System.out.println("Ingresa los parámetros solicitados");
            return;
        }

        try {
            img = readImage(inputName);
            mask = readImage(folder + maskName);
        }
        catch(IOException e) {
            System.out.println("Ha ocurrido un error con el archivo.");
            System.out.println(e);
            return;
        }

        int img_width = img.getWidth();
        int img_height = img.getHeight();

        int blanco = 0xfff;
        LinkedList<RGBDot> pixels = new LinkedList<RGBDot>();
        for(int i = 0; i < mask.getWidth(); i++) {
            for(int j = 0; j < mask.getHeight(); j++) {
                int color = mask.getRGB(i, j);
                if ((blanco & color) != 0) {
                    int x = i + 834;
                    int y = j + 106;
                    pixels.add(new RGBDot(x,y, img.getRGB(x, y)));
                }
            }
        }
        Predicate<RGBDot> separaSol = dot -> dot.get_r() + dot.get_b() + dot.get_g() == 255 *3;
        Predicate<RGBDot> separaNubes = dot -> (float) dot.get_r()/dot.get_b() < 0.95;
        LinkedList<RGBDot> cielo = new LinkedList<RGBDot>();
        LinkedList<RGBDot> cieloNube = new LinkedList<RGBDot>();
        LinkedList<RGBDot> nubes = new LinkedList<RGBDot>();
        LinkedList<RGBDot> sol = new LinkedList<RGBDot>();

        separateRGBDot(pixels, sol, cieloNube, separaSol);
        separateRGBDot(cieloNube, cielo, nubes, separaNubes);
        cielo.addAll(sol);

        LinkedList<LinkedList<RGBDot>> clustered = new LinkedList<LinkedList<RGBDot>>();
        clustered.add(cielo);
        clustered.add(nubes);

        float icc = calculateIcc(clustered);
        System.out.println("Índice de cobertura nubosa: " + icc);

        if(generateImg) {
            BufferedImage clustered_img = generateClusteredImg(clustered, img_width, img_height);
            try {
                File nuevoF = new File(outputName);
                ImageIO.write(clustered_img, "jpg", nuevoF);
            }
            catch(IOException e) {
                System.out.println(e);
            }
        }
    }

    public static BufferedImage readImage(String name) throws IOException {
        File f = new File(name);
        return ImageIO.read(f);
    }

    /**
     * Método que verifica que se reciban los parámetros solitcitados.
     * @param args Arreglo que contiene los parámetros solicitados.
     */
    public static void checkParameters(String[] args){
        inputName += args[0];
        int indexDot = args[0].indexOf(".");
        outputName += args[0].substring(0, indexDot) + "-seg.jpg";

        if(args.length > 1){
            char flag = args[1].charAt(0);
            if(flag == 's' || flag == 'S')
                generateImg = true;
        }
    }

    /**
     * Método que calcula el Índice de Cobertura Nubosa de la imagen.
     * @param pixeles Lista de pixeles que conforman la imagen.
     * @return float  Índice de cobertura nubosa.
     */
    public static float calculateIcc(LinkedList<LinkedList<RGBDot>> pixeles) {
        int sky_n = pixeles.get(0).size();
        int cloud_n = pixeles.get(1).size();
        float icc = (float) cloud_n/(cloud_n + sky_n);
        return icc;
    }

    /**
     * Método que genera la imagen a blanco y negro, de la imagen original.
     * @param clustered
     * @param width Ancho de la imagen.
     * @param height Alto de la imagen.
     * @return BufferedImage
     */
    public static BufferedImage generateClusteredImg(LinkedList<LinkedList<RGBDot>> clustered, int width, int height) {
        BufferedImage clustered_img = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = clustered_img.createGraphics();
        graphics.setPaint ( new Color(128, 128, 128) );
        graphics.fillRect ( 0, 0, clustered_img.getWidth(), clustered_img.getHeight() );

        for (RGBDot dot: clustered.get(0)) {
            clustered_img.setRGB(dot.get_x(), dot.get_y(), RGBDot.to_rgb(0,0,0));
        }

        for (RGBDot dot: clustered.get(1)) {
            clustered_img.setRGB(dot.get_x(), dot.get_y(), RGBDot.to_rgb(255,255,255));
        }

        return clustered_img;
    }

    /**
     * Método que separa una lista de RGBDot en dos listas a partir de un predicado
     * @param dots Lista original
     * @param trueL Si el predicado es verdadero, aquí se meten los pixeles
     * @param falseL Si el predicado es falso, aquí se meten los pixeles
     * @return predicado El predicado a evaluar en la lista de puntos
     */
    public static void separateRGBDot(LinkedList<RGBDot> dots, LinkedList<RGBDot> trueL, LinkedList<RGBDot> falseL, Predicate<RGBDot> predicado) {
        for (RGBDot dot: dots) {
            if (predicado.test(dot)) {
                trueL.add(dot);
            } else {
                falseL.add(dot);
            }
        }
    }
}
