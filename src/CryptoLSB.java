import org.jetbrains.annotations.NotNull;
import utils.TextUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class CryptoLSB {
    private Image image = new Image();
    private TextUtils textUtils = new TextUtils();
    private int initialShift = 32;

    public CryptoLSB() {
    }

    public void code (Path path,  BufferedImage sourceImage, String textToHide, int sourceMode) throws Exception {
        //duplicates source image - more stable
        BufferedImage cryptoImage = image.getNewImage(sourceImage);
        //add encrypted text to source image
        cryptoImage = addTextLengthToImage(cryptoImage, textToHide);
        cryptoImage = addTextToImage(cryptoImage, textToHide);

        //creates file
        File outputFile;
        if (sourceMode==0) {
            outputFile = new File(image.getImageOutName(path));
            ImageIO.write(cryptoImage, image.getImageType(path), outputFile);
        } else {
            outputFile = new File("url_output.png");
            ImageIO.write(cryptoImage, "png", outputFile);
        }
    }

    public String decode (@NotNull Path path) throws IOException {
        byte[] decodedByteArray;

        BufferedImage imageToBeDecoded = image.readImageFile(path.toString());
        BufferedImage newImageToBeDecoded = image.getNewImage(imageToBeDecoded);
        decodedByteArray = extractTextFromImage(newImageToBeDecoded);
        return new String(decodedByteArray);
    }

    public BufferedImage addTextToImage(BufferedImage image, String message){
        initialShift = 32;
        byte[] imageByteArray = this.image.getBytesFromImage(image);
        byte[] textByteArray = textUtils.getBytesFromText(message);
        //byte[] textLengthByteArray = text.getTextLength(message.length());

        if (imageByteArray.length + (initialShift/8) < textByteArray.length) {
            throw new IllegalArgumentException("Image is too small for the text");
        }

        //TODO isn't message too long?
        for (byte b : textByteArray) {

            for (int j = 7; j >= 0; j--) {
                initialShift++;
                imageByteArray[initialShift] =
                        (byte) ((imageByteArray[initialShift] & 0xFE) | ((int) b >>> j) & 1);
            }
        }
        return image;
    }

    public BufferedImage addTextLengthToImage(BufferedImage image, String message){
        initialShift = 0;
        byte[] imageByteArray = this.image.getBytesFromImage(image);
        byte[] textByteArray = textUtils.getBytesFromText(message);
        byte[] textLengthByteArray = textUtils.getTextLength(textByteArray.length);

        for (byte b : textLengthByteArray) {

            for (int j = 7; j >= 0; j--) {
                imageByteArray[initialShift] =
                        (byte) ((imageByteArray[initialShift] & 0xFE) | ((int) b >>> j) & 1);
                initialShift++;
            }
        }
        return image;
    }

    public byte[] extractTextFromImage(BufferedImage image){
        initialShift=32;
        byte[] imageByteArray = this.image.getBytesFromImage(image);
        //size of return array depends on length of hidden text
        int textLength = extractTextLength(imageByteArray);
        byte[] result = new byte[textLength];

        for(int i=0; i<result.length; i++)
        {
            for(int j=0; j<=7; j++)
            {
                initialShift++;
                result[i] = (byte)((result[i] << 1) | (imageByteArray[initialShift] & 1));
            }
        }
        return result;
    }

    public int extractTextLength(byte[] image){
        int textLength = 0;
        //first four bytes holding length
        for (int i = 0; i<32; i++){
            textLength = (textLength << 1) | (image[i] & 1);
        }
        return textLength;
    }
}
