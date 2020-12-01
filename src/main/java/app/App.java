package app;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.LinkedList;

import k_means.*;

public class App {
    public static String folder = "src/main/resources/";
    public static void main(String args[])throws IOException {

        BufferedImage img = null;
        File f = null;
        boolean generateImg = false;
        String inputName = folder + args[0];
        String outputName = "nitida_clustered.jpg";

        if(args.length > 1){
            if(args[1].toLowerCase() == "s")
            generateImg = true;
        }

        try {
            f = new File(inputName);
            img = ImageIO.read(f);
            System.out.println("Lei la imagen");
        }
        catch(IOException e) {
            System.out.println(e);
        }

        int img_width = img.getWidth();
        int img_height = img.getHeight();

        LinkedList<RGBDot> puntos = new LinkedList<RGBDot>();
        for(int i = 0; i < img_width; i++) {
            for(int j = 0; j < img_height; j++) {
                double d = Math.sqrt(Math.pow(2184 - i,2) + Math.pow(1456 - j,2));
                if(d <= 1324)
                puntos.add(new RGBDot(i,j, img.getRGB(i, j)));
            }
        }

        System.out.println("Aqui estoy");

        LinkedList<RGBDot> init = new LinkedList<RGBDot>();

        init.add(new RGBDot(0,0, 255,255,255));
        init.add(new RGBDot(0,0, 50, 50, 150));

        KMeans<RGBDot> clusterer = new KMeans<RGBDot>(puntos);
        LinkedList<LinkedList<RGBDot>> clustered = clusterer.getClusters(init, (RGBDot a,  RGBDot b) -> {
            return (float) Math.abs(a.get_r() - b.get_r());
        });

        System.out.println("Llegué aqui");

        float icc = calculateIcc(clustered);
        System.out.println("Índice de cobertura nubosa: " + icc);

        //write image
        if(generateImg) {
            BufferedImage clustered_img = generateClusteredImg(clustered, img_width, img_height);
            try {
                File nuevoF = new File(outputName);
                ImageIO.write(clustered_img, "jpg", nuevoF);
                System.out.println("Ya kdo tu imagen");
            }
            catch(IOException e) {
                System.out.println(e);
            }
        }
    }

    public static float calculateIcc(LinkedList<LinkedList<RGBDot>> pixeles) {
        int cloud_n = pixeles.get(0).size();
        int sky_n = pixeles.get(1).size();
        float icc = (float) cloud_n/(cloud_n + sky_n);
        return icc;
    }

    public static BufferedImage generateClusteredImg(LinkedList<LinkedList<RGBDot>> clustered, int width, int height) {
        BufferedImage clustered_img = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);

        for (RGBDot dot: clustered.get(0)) {
            clustered_img.setRGB(dot.get_x(), dot.get_y(), RGBDot.to_rgb(255,255,255));
        }

        for (RGBDot dot: clustered.get(1)) {
            clustered_img.setRGB(dot.get_x(), dot.get_y(), RGBDot.to_rgb(128,128,128));
        }

        return clustered_img;
    }
}
