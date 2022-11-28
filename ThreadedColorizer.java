import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ThreadedColorizer implements Runnable{
    private BufferedImage image;
    private JFrame frame;
    private int leftCorner;
    private int topCorner;
    private int width;
    private int height;

    public ThreadedColorizer (BufferedImage image, JFrame frame, 
                                    int leftCorner, int topCorner, int width, int height) {
        
                                        this.image = image;
        this.frame = frame;
        this.leftCorner = leftCorner;
        this.topCorner = topCorner;
        this.width = width;
        this.height = height;
    }

    @Override
    public void run() {
        colorImage(this.image, this.frame, this.leftCorner, this.topCorner, this.width, this.height);
    }

    public static void colorImage(BufferedImage image, JFrame frame, int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width; ++x) {
            for (int y = topCorner; y < topCorner + height; ++y) {
                colorPixel(image, x, y);

                if ((x + y) % 150 == 0) {
                    frame.revalidate();
                    frame.repaint();
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
