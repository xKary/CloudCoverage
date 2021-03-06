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

        LinkedList<RGBDot> pixels = readPixels(mask,img);
        LinkedList<LinkedList<RGBDot>> clustered = separateClouds(pixels);

        float icc = calculateIcc(clustered);
        System.out.println("Índice de cobertura nubosa: " + icc);

        if(generateImg)
            saveImage(clustered, img.getWidth(), img.getHeight());
    }

    /**
     * Método que abre una imagen a partir de un nombre
     * @param name El nombre de la imagen a abrir
     * @return BufferedImage imagen abierta
     * @throws IOException
     */
    public static BufferedImage readImage(String name) throws IOException {
        File f = new File(name);
        return ImageIO.read(f);
    }

    /**
     * Método que obtiene los pixeles de la imagen que están dentro de la máscara.
     * @param mask Imagén que se usa cómo máscara.
     * @param img Imagén de la cual se obtendrán los pixeles.
     * @return LinkedList<RGBDot> Lista con los punto de la imagen que están dentro de la máscara.
     */
    public static LinkedList<RGBDot> readPixels(BufferedImage mask, BufferedImage img){
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
        return pixels;
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
     * Método que separa una lista de RGBDot en otra si cumple un predicado
     * @param dots Lista original
     * @param trueL Si el predicado es verdadero, aquí se meten los pixeles
     * @param predicado El predicado a evaluar en la lista de puntos
     * @return LinkedList<RGBDot> Si el predicado es false, se guarda en una
     * lista y la devuelve
     */
    public static LinkedList<RGBDot> separateRGBDot(LinkedList<RGBDot> dots, LinkedList<RGBDot> trueL, Predicate<RGBDot> predicado) {
        LinkedList<RGBDot> falseL = new LinkedList<RGBDot>();
        for (RGBDot dot: dots) {
            if (predicado.test(dot)) {
                trueL.add(dot);
            } else {
                falseL.add(dot);
            }
        }
        return falseL;
    }

    /**
     * Método que separa una lista de RGBDot en cielo y nubes
     * @param pixels Lista original con nubes y cielo
     * @return LinkedList<LinkedList<RGBDot>> Lista con una lista de RGBDot,
     * el primero es la lista de pixeles con el cielo, el segundo con nubes
     */
    public static LinkedList<LinkedList<RGBDot>> separateClouds(LinkedList<RGBDot> cielo) {
        Predicate<RGBDot> separaSol = dot -> dot.get_r() + dot.get_b() + dot.get_g() == 255 *3;
        Predicate<RGBDot> separaNubes = dot -> (float) dot.get_r()/dot.get_b() >= 0.95;

        LinkedList<RGBDot> nubes = new LinkedList<RGBDot>();
        LinkedList<RGBDot> sol = new LinkedList<RGBDot>();

        cielo = separateRGBDot(cielo, sol, separaSol);
        cielo = separateRGBDot(cielo, nubes, separaNubes);
        cielo.addAll(sol);

        LinkedList<LinkedList<RGBDot>> clustered = new LinkedList<LinkedList<RGBDot>>();
        clustered.add(cielo);
        clustered.add(nubes);
        return clustered;
    }

    /**
     * Método que guarda la imagen a blanco y negro.
     * @param clustered Conjunto de datos a escribir en la imagen.
     * @param img_Widht Ancho de la imagen.
     * @param img_Height Alto de la imagen.
     */
    public static void saveImage(LinkedList<LinkedList<RGBDot>> clustered, int img_Widht, int img_Height){
        BufferedImage clustered_img = generateClusteredImg(clustered, img_Widht, img_Height);
        try {
            File nuevoF = new File(outputName);
            ImageIO.write(clustered_img, "jpg", nuevoF);
            System.out.println("La imagen se guardó con nombre: " + outputName);
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }
}
