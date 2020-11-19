import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.LinkedList;

public class Main {
    public static void main(String args[])throws IOException {
        BufferedImage img = null;
        File f = null;
        String inputName = "nubes.jpg";
        String outputName = "nitida_clustered.jpg";
        try {
            f = new File(inputName);
            img = ImageIO.read(f);
        }
        catch(IOException e) {
            System.out.println(e);
        }

        LinkedList<RGBDot> puntos = new LinkedList<RGBDot>();
        for(int i = 0; i < img.getWidth(); i++) {
            for(int j = 0; j < img.getHeight(); j++) {
                puntos.add(new RGBDot(i,j, img.getRGB(i, j)));
            }
        }

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

        BufferedImage clustered_img = new BufferedImage(img.getWidth(), img.getHeight(),  BufferedImage.TYPE_INT_RGB);
        System.out.println(clustered.get(0).getElements().size());
        System.out.println(clustered.get(1).getElements().size());
        for (RGBDot dot: clustered.get(0).getElements()) {
            clustered_img.setRGB(dot.get_x(), dot.get_y(), RGBDot.to_rgb(128,128,128));
        }
        for (RGBDot dot: clustered.get(1).getElements()) {
            clustered_img.setRGB(dot.get_x(), dot.get_y(), RGBDot.to_rgb(255,255,255));
        }

        //write image
        try {
            File nuevoF = new File(outputName);
            ImageIO.write(clustered_img, "jpg", nuevoF);
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }
}
