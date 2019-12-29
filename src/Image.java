import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;

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
    public BufferedImage getNewImage (BufferedImage oldImage) throws IOException {

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

    //get matrix of pixels (used in jpegEncoder.DCT]
    public int[][] getMatrixPixels(BufferedImage image){
        int[][] matrixPixels = new int[image.getWidth()][image.getHeight()];
        for (int i = 0; i < matrixPixels[0].length; i++) {
            for (int j = 0; j < matrixPixels.length; j++) {
                matrixPixels[i][j] = image.getRGB(i, j);
            }
        }
        return matrixPixels;
    }

    //get matrix of pixels (used in jpegEncoder.DCT]
    public ArrayList<Array2D> getMatrixPixelsBands(BufferedImage image){
        int[][] matrixPixelsR = new int[image.getWidth()][image.getHeight()];
        int[][] matrixPixelsG = new int[image.getWidth()][image.getHeight()];
        int[][] matrixPixelsB = new int[image.getWidth()][image.getHeight()];

        for (int i = 0; i < matrixPixelsR[0].length; i++) {
            for (int j = 0; j < matrixPixelsR.length; j++) {
                matrixPixelsR[i][j] = (image.getRGB(i, j) >> 16) & 0x000000FF;
            }
        }
        for (int i = 0; i < matrixPixelsG[0].length; i++) {
            for (int j = 0; j < matrixPixelsG.length; j++) {
                matrixPixelsG[i][j] = (image.getRGB(i, j) >> 8) & 0x000000FF;
            }
        }
        for (int i = 0; i < matrixPixelsB[0].length; i++) {
            for (int j = 0; j < matrixPixelsB.length; j++) {
                matrixPixelsB[i][j] = (image.getRGB(i, j)) & 0x000000FF;
            }
        }

        ArrayList<Array2D> matrixPixelsBands = new ArrayList<>();
        matrixPixelsBands.add(new Array2D(matrixPixelsR, "red"));
        matrixPixelsBands.add(new Array2D(matrixPixelsG, "green"));
        matrixPixelsBands.add(new Array2D(matrixPixelsB, "blue"));

        return matrixPixelsBands;
    }

    //get matrix of pixels (used in jpegEncoder.DCT]
    public BufferedImage[] getBlocksOfImage(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        int arrayIndex = 0;
        //TODO specify dynamic length
        //TODO for 512x512 --> 4096 (8x8 block)
        BufferedImage[] blocksOfImage = new BufferedImage[4096];
        for (int i = 0; i<width; i=i+8) {
            for (int j = 0; j<height; j=j+8) {
                blocksOfImage[arrayIndex] = image.getSubimage(i,j,8,8);
                arrayIndex++;
            }
        }
        return blocksOfImage;
    }
}
