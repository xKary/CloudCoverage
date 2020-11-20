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
        LinkedList<Cluster<RGBDot>> clustered = clusterer.getClusters(init, (RGBDot a,  RGBDot b) -> {
            // float difR = a.get_r() - b.get_r();
            // float difG = a.get_g() - b.get_g();
            // float difB = a.get_b() - b.get_b();
            //
            // float squareR = difR * difR;
            // float squareG = difG * difG;
            // float squareB = difB * difB;
            // return (float) Math.sqrt(squareR + squareG + squareB);
            return (float) Math.abs(a.get_r() - b.get_r());
        });

        System.out.println("Llegué aqui");

        BufferedImage clustered_img = new BufferedImage(img_width, img_height,  BufferedImage.TYPE_INT_RGB);
        System.out.println(clustered.get(0).getElements().size());
        System.out.println(clustered.get(1).getElements().size());

        for (RGBDot dot: clustered.get(0).getElements()) {
            clustered_img.setRGB(dot.get_x(), dot.get_y(), RGBDot.to_rgb(255,255,255));
        }

        for (RGBDot dot: clustered.get(1).getElements()) {
            clustered_img.setRGB(dot.get_x(), dot.get_y(), RGBDot.to_rgb(128,128,128));
        }

        //write image
        if(generateImg)
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
