package app;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.imageio.ImageIO;

import java.util.LinkedList;

import k_means.*;
/**
 * Clase qué se encarga de determinar el Índice de Cobertura Nubosa de una
 * imagen del cielo, y generar su versión en blanco y negro si se lo
 * solicitan.
 */
public class App {

    public static String folder = "src/main/resources/";
    public static String inputName = folder;
    public static String outputName = folder;
    public static boolean generateImg = false;

    public static void main(String args[])throws IOException {

        BufferedImage img = null;

        try {
            checkParameters(args);
        } catch (Exception e) {
            System.out.println("Ingresa los parámetros solicitados");
            return;
        }

        try {
            File f = new File(inputName);
            img = ImageIO.read(f);
        }
        catch(IOException e) {
            System.out.println("Ha ocurrido un error con el archivo.");
            System.out.println(e);
        }

        int img_width = img.getWidth();
        int img_height = img.getHeight();

        //cambiar el nombre de puntos
        LinkedList<RGBDot> puntos = new LinkedList<RGBDot>();
        for(int i = 0; i < img_width; i++) {
            for(int j = 0; j < img_height; j++) {
                double d = Math.sqrt(Math.pow(2184 - i,2) + Math.pow(1456 - j,2));
                if(d <= 1324)
                    puntos.add(new RGBDot(i,j, img.getRGB(i, j)));
            }
        }
        LinkedList<RGBDot> cielo = new LinkedList<RGBDot>();
        LinkedList<RGBDot> sol = new LinkedList<RGBDot>();
        filterSun(puntos, cielo, sol);

        LinkedList<RGBDot> init = new LinkedList<RGBDot>();

        init.add(new RGBDot(0,0, 0, 0 , 255));
        init.add(new RGBDot(0,0, 255, 0, 0));

        KMeans<RGBDot> clusterer = new KMeans<RGBDot>(cielo);
        LinkedList<LinkedList<RGBDot>> clustered = clusterer.getClusters(init, (RGBDot a,  RGBDot b) -> {
            return (float) Math.abs(a.get_r() - b.get_r()*.95);
        });

        for (RGBDot dot: sol) {
            clustered.get(0).add(dot);
        }

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
        int cloud_n = pixeles.get(0).size();
        int sky_n = pixeles.get(1).size();
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

    public static void filterSun(LinkedList<RGBDot> dots, LinkedList<RGBDot> sky, LinkedList<RGBDot> sun) {
        for (RGBDot dot: dots) {
            if ((float) dot.get_r()/dot.get_b() < 0.95) {
                sun.add(dot);
            } else {
                sky.add(dot);
            }
        }
    }
}
