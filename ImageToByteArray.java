import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.Spring;

import java.io.ByteArrayOutputStream;

public class ImageToByteArray {

    /**
     * @param filePath
     * @return
     */

    public static byte [] toRGB(String filePath){
        System.out.println(filePath);
        byte [] data = null;

        try {
            BufferedImage bImage = ImageIO.read(new File(filePath));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            String [] filePathArr = filePath.split("\\.", 2);

            String fileExtension = filePathArr[filePathArr.length - 1];

            ImageIO.write(bImage, fileExtension, bos );

            data = bos.toByteArray();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;

     }
    
}
