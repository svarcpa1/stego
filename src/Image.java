import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;

public class Image {

    public Image() {
    }

    //getting image from file
    public BufferedImage readImageFile(String filePath) throws IOException {
        File sourceImageFile = new File(filePath);
        return ImageIO.read(sourceImageFile);
    }

    //getting image from URL
    public BufferedImage readImageURL(String urlPath){
        BufferedImage bufferedImage;
        try {
            URL url = new URL(urlPath);
            bufferedImage = ImageIO.read(url);
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //get bytes array from image
    public byte[] getBytesFromImage(BufferedImage image){
        WritableRaster writableRaster;
        DataBufferByte buffer;

        writableRaster = image.getRaster();
        buffer = (DataBufferByte)writableRaster.getDataBuffer();

        return buffer.getData();
    }

    //new image creation
    public BufferedImage getNewImage (BufferedImage oldImage) {

        //Represents an image with 8-bit RGB color components, corresponding to a Windows-style BGR color model
        //with the colors Blue, Green, and Red stored in 3 bytes
        BufferedImage newImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        //TODO
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.drawRenderedImage(oldImage, null);

        return newImage;
    }

    public String getImageOutName(Path path){
        String fileName =  path.getFileName().toString();
        String[] fileNameArray =  fileName.split("\\.");
        return fileNameArray[0] + "_output." + fileNameArray[1];
    }

    public String getImageType(Path path){
        String fileName =  path.getFileName().toString();
        String[] fileNameArray =  fileName.split("\\.");
        return fileNameArray[1];
    }
}
