import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;


public class QuickStart extends Panel {
    /**
     *
     */
    private static final int pixelUpperBound = 256;

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main (String[] args) throws IOException {

        BufferedImage image = null;

        try {   
            String inImgPath = "gray.png";

            // byte [] imgArr = ImageToByteArray.toRGB(inImgPath);


            File inpuFile = new File(inImgPath);
            image = ImageIO.read(inpuFile);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        long startTime = System.currentTimeMillis();
//        recolorSingleThreaded(originalImage, resultImage);
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        int sq_size = 50;

        JFrame frame = new JFrame("Processing Your Image...");
        JLabel jLabel = new JLabel();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon(image);
        jLabel.setIcon(imageIcon);
        frame.setIconImage(image);
        frame.getContentPane().add(jLabel, BorderLayout.CENTER);

        colorMultithread(image, frame, numberOfThreads, sq_size);
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println(String.valueOf(duration));

        // int width = image.getWidth();
        // int height = image.getHeight();


        // for (int i = 0; i < height; i++) {
        //     for (int j = 0; j < width; j++) {

        //         colorPixel(image, j, i);
        //         if ((i + j) % 500 == 0) {



        //         }
                
        //     }
        // }

        System.out.println("Coloring complete!");

        
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

    public static void colorMultithread(BufferedImage  image, JFrame frame, int numberOfThreads, int sq_size) {
        List<Thread> threads = new ArrayList<>();
        int width =  sq_size;
        int height =  sq_size;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int numProcessBoxes = (imageHeight * imageWidth) / (sq_size * sq_size);
        
        int leftCorner = 0;
        int topCorner = 0;

        for (int i = 0; i < numProcessBoxes; i+=numberOfThreads) {

            for (int j = 0; j < numberOfThreads && (i + j) <= numProcessBoxes; ++j) {
                final int threadMultiplier = j;

                MultiThreadedColoring mtc = new MultiThreadedColoring(numberOfThreads, image, frame, leftCorner, topCorner, width, height);
                Thread thread = new Thread(mtc);
                threads.add(thread);
                thread.start();

                leftCorner += sq_size;

                if (leftCorner >= imageWidth) {
                    leftCorner = 0;
                    topCorner += sq_size;
                }
            }

    
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
        
    }

    public static void colorSingleThread(BufferedImage image, JFrame frame) {
        colorImage(image, frame, 0, 0, image.getWidth(), image.getHeight());
    }

    public static void colorImage(BufferedImage image, JFrame frame, int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < image.getWidth(); ++x) {
            for (int y = topCorner; y < topCorner + height && y < image.getHeight(); ++y) {
                colorPixel(image, x, y);

                if ((x + y) % 150 == 0) {
                    frame.revalidate();
                    frame.repaint();
                    // frame.update(frame.getGraphics());
                    frame.pack();
                    frame.setVisible(true);
                }
            }
        }
    }

    public static void colorPixel (BufferedImage image, int x, int y) {
        
        int p = image.getRGB(x, y);
        int a = (p>>24)&0xff;
        int r = (p>>16)&0xff;
        int g = (p>>8)&0xff;
        int b = p&0xff;

        // int r = pixelUpperBound;
        // int g = rand.nextInt(pixelUpperBound);
        // int b = rand.nextInt(pixelUpperBound);
        
        if (isShadeOfGray(r, g, b)) {
            r = Math.min(r + 10, 255);
            g = Math.max(g - 80, 0);
            b = Math.max(b - 20, 0);
        }

        p = (a<<24) | (r<<16) | (g<<8) | b;
        
        image.setRGB(x, y, p);
    }

    public static boolean isShadeOfGray(int red, int blue, int green) {
        // just check if all the channels are roughly of the same intensity
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

}

