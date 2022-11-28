import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.naming.InitialContext;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;


public class QuickStart extends Panel {
    /**
     * @param args
     * @throws Exception
     * @throws InterruptedException
     */
    public static void main (String[] args) throws IOException {

        String inImgPath = args[0];
        int sqSize = Integer.parseInt(args[1]);
        String processingMode = args[2];



        int cntPeriod = 0;

        for (int i = 0; i < inImgPath.length(); i++) {
            if ('.' == inImgPath.charAt(i)) {
                cntPeriod++;
            }
        }

        if (cntPeriod > 1) {
            throw new IOException("Filename is not acceptable");
        }

        String [] arrOfImgPath = inImgPath.split("\\.", 2);
        String imgName = arrOfImgPath[0];
        String imgExtension = arrOfImgPath[1];

        System.out.println(imgExtension);

        BufferedImage image = null;

        try {   
            File inpuFile = new File(inImgPath);
            image = ImageIO.read(inpuFile);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        int numberOfThreads = Runtime.getRuntime().availableProcessors();

        JFrame frame = new JFrame("Coloring Your Image...");
        JLabel jLabel = new JLabel();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon(image);
        jLabel.setIcon(imageIcon);
        frame.setIconImage(image);
        frame.getContentPane().add(jLabel, BorderLayout.CENTER);

        long startTime = System.currentTimeMillis();

        if ("M".equals(processingMode)) {
            System.out.println("--- Processing in MultiThreaded Mode ---");
            multiThreadedColorizer(image, frame, numberOfThreads, sqSize);
        } else {
            System.out.println("--- Processing in SingleThreaded Mode ---");
            singleThreadedColorizer(image, frame, sqSize);
        }

        frame.revalidate();
        frame.repaint();
        frame.pack();
        frame.setVisible(true);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
      

        System.out.println(">>> Coloring complete!");
        System.out.println(">>> Total time taken (ms): " + String.valueOf(duration));

        
        try {
            String outFilePath = imgName + "_out." + imgExtension;
            File outpFile = new File(outFilePath);
            ImageIO.write(image, imgExtension, outpFile);
            System.out.println(">>> Writing complete: file saved as " + outFilePath);

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
      

    }

    public static void multiThreadedColorizer(BufferedImage  image, JFrame frame, int numberOfThreads, int sqSize) {

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int numVerBoxes = intDivisionCeil(imageHeight, sqSize);
        int numHorBoxes = intDivisionCeil(imageWidth, sqSize);
        int numProcessBoxes = numHorBoxes * numVerBoxes;
    
        int numStages = intDivisionCeil(numProcessBoxes, numberOfThreads);
        
        System.out.println("Required number of stages: " + numStages);
        System.out.println("Required number of processing boxes: " + numProcessBoxes);

        int leftCorner = 0;
        int topCorner = 0;
        System.out.println("number of threads: " + numberOfThreads);

        int totalNumThreads = 0;

        outerloop:

        for (int i = 0; i <= numStages; i++) {
            List<Thread> threads = new ArrayList<>();

            System.out.println(">>> Processing Stage: " + i);

            for (int j = 0; j < numberOfThreads; j++) {


                boolean rowEnd = false;
                boolean colEnd = false;

                int width =  sqSize;
                int height = sqSize;

                if ((leftCorner + width) >= imageWidth) {
                    width = imageWidth - leftCorner;
                    rowEnd = true;
                    System.out.println("Row Complete!");
                }

                if ((topCorner + height) >= imageHeight) {
                    height = imageHeight - topCorner;
                    colEnd = true;
                }

                System.out.println("Generated thread No." + j);

                ThreadedColorizer mtc = new ThreadedColorizer(image, frame, leftCorner, topCorner, width, sqSize);
                Thread thread = new Thread(mtc);
                threads.add(thread);
                thread.start();

                totalNumThreads++;

                leftCorner += sqSize;

                if (rowEnd) {
                    leftCorner = 0;
                    topCorner += sqSize;
                    
                    if (colEnd){
                        break outerloop;
                    }
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
        frame.update(frame.getGraphics());
        System.out.println(">>> Total No. of threads generated: " + totalNumThreads);
    }   

    public static void singleThreadedColorizer(BufferedImage  image, JFrame frame, int sqSize) {

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int numVerBoxes = intDivisionCeil(imageHeight, sqSize);
        int numHorBoxes = intDivisionCeil(imageWidth, sqSize);
        int numProcessBoxes = numHorBoxes * numVerBoxes;
            
        System.out.println("Required number of processing boxes: " + numProcessBoxes);

        int leftCorner = 0;
        int topCorner = 0;

        for (int j = 0; j < numProcessBoxes; j++) {


            boolean rowEnd = false;
            boolean colEnd = false;

            int width =  sqSize;
            int height = sqSize;

            if ((leftCorner + width) > imageWidth) {
                width = imageWidth - leftCorner;
                rowEnd = true;
            }

            if ((topCorner + height) > imageHeight) {
                height = imageHeight - topCorner;
                colEnd = true;
            }

            ThreadedColorizer mtc = new ThreadedColorizer(image, frame, leftCorner, topCorner, width, sqSize);
            Thread thread = new Thread(mtc);
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            leftCorner += sqSize;

            if (rowEnd) {
                leftCorner = 0;
                topCorner += sqSize;
                
                if (colEnd){
                    break;
                }
            }
        }

        frame.update(frame.getGraphics());

    }


    public static int intDivisionCeil(int dividend, int divisor) {
        if (divisor == 0) {
            return 0;
        }
        return (dividend + divisor - 1) / divisor;
    }

}

