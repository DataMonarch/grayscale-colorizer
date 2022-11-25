import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class QuickStart {
    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main (String[] args) throws IOException {

        int width = 1400;
        int height = 1400;

        BufferedImage image = null;

        try {
            String inImgPath = "gray.png";

            // byte [] imgArr = ImageToByteArray.toRGB(inImgPath);

            System.out.println(Arrays.toString(imgArr));

            

            File inpuFile = new File(inImgPath);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            
            image = ImageIO.read(inpuFile);
            int [] imgArr = image;


            System.out.println("Reading complete!");

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        
        try {
            File outpFile = new File("out.png");
            ImageIO.write(image, "png", outpFile);
            System.out.println("writing complete!");

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        // String filename = args[0];
        // int sq_size = Integer.parseInt(args[1]);
        // String processing_mode = args[2];

        // System.out.println(filename);
        // System.out.println(sq_size);
        // System.out.println(processing_mode);



        // for (int i = 0; i < 5; i++) {
        //     MultiThreadedThing myThing = new MultiThreadedThing(i);
        //     Thread myThread = new Thread(myThing);
        //     myThread.start();
        //     myThread.join();
        // }        

    }
}

