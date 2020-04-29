package crypto;

import org.jetbrains.annotations.NotNull;
import utils.UtilsGeneral;
import utils.UtilsImage;
import utils.UtilsText;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CryptoLSB2 {
    private UtilsImage utilsImage = new UtilsImage();
    private UtilsText utilsText = new UtilsText();
    private UtilsGeneral utilsGeneral = new UtilsGeneral();
    private int initialShift = 32;

    public CryptoLSB2() {
    }

    public void code(String path, BufferedImage sourceImage, String message) throws Exception {
        //duplicates source image - more stable
        BufferedImage cryptoImage = utilsImage.getNewImage(sourceImage);
        //add encrypted text to source image
        cryptoImage = addTextLengthToImage(cryptoImage, message);
        cryptoImage = addTextToImage(cryptoImage, message);

        //creates file
        File outputFile;
        if (!utilsGeneral.isImageLoadedFromURL(path)) {
            outputFile = new File("output." + utilsImage.getImageType(path));
            ImageIO.write(cryptoImage, utilsImage.getImageType(path), outputFile);
        } else {
            outputFile = new File("output.png");
            ImageIO.write(cryptoImage, "png", outputFile);
        }
    }

    public String decode(@NotNull String path) throws IOException {
        byte[] decodedByteArray;

        BufferedImage imageToBeDecoded = utilsImage.readImageFile(path);
        BufferedImage newImageToBeDecoded = utilsImage.getNewImage(imageToBeDecoded);
        decodedByteArray = extractTextFromImage(newImageToBeDecoded);

        return new String(decodedByteArray);
    }

    public BufferedImage addTextLengthToImage(BufferedImage image, String message) {
        initialShift = 0;
        byte[] imageByteArray = this.utilsImage.getBytesFromImage(image);
        byte[] textByteArray = utilsText.getBytesFromText(message);
        byte[] textLengthByteArray = utilsText.getTextLength(textByteArray.length);

        for (byte b : textLengthByteArray) {
            for (int j = 6; j >= 0; j = j - 2) {
                imageByteArray[initialShift] =
                        (byte) ((imageByteArray[initialShift] & 0xFC) | ((int) b >>> j) & 3);
                initialShift++;
            }
        }
        return image;
    }

    public BufferedImage addTextToImage(BufferedImage image, String message) {
        initialShift = 16;
        byte[] imageByteArray = this.utilsImage.getBytesFromImage(image);
        byte[] textByteArray = utilsText.getBytesFromText(message);

        if (imageByteArray.length + (initialShift / 8) < textByteArray.length) {
            throw new IllegalArgumentException("Image is too small for the text");
        }
        for (byte b : textByteArray) {
            for (int j = 6; j >= 0; j=j-2) {
                initialShift++;
                imageByteArray[initialShift] =
                        (byte) ((imageByteArray[initialShift] & 0xFC) | ((int) b >>> j) & 3);
            }
        }
        return image;
    }

    public int extractTextLength(byte[] image) {
        int textLength = 0;
        //bytes 0-3 for text length
        for (int i = 0; i < 16; i++) {
            textLength = (textLength << 2) | (image[i] & 3);
        }
        return textLength;
    }

    public byte[] extractTextFromImage(BufferedImage image) {
        initialShift = 16;
        byte[] imageByteArray = this.utilsImage.getBytesFromImage(image);
        //size of return array depends on length of hidden text
        int textLength = extractTextLength(imageByteArray);
        byte[] result = new byte[textLength];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j <= 6; j=j+2) {
                initialShift++;
                result[i] = (byte) ((result[i] << 2) | (imageByteArray[initialShift] & 3));
            }
        }
        return result;
    }

    public String decodeFirstChar(@NotNull String path) throws IOException {
        byte[] decodedByteArray;

        BufferedImage imageToBeDecoded;
        if (utilsGeneral.isImageLoadedFromURL(path)) {
            imageToBeDecoded = utilsImage.readImageURL(path);
        } else {
            imageToBeDecoded = utilsImage.readImageFile(path);
        }
        BufferedImage newImageToBeDecoded = utilsImage.getNewImage(imageToBeDecoded);
        decodedByteArray = extractTextFromImageFirstChar(newImageToBeDecoded);

        return new String(decodedByteArray);
    }

    public byte[] extractTextFromImageFirstChar(BufferedImage image) {
        initialShift = 16;
        byte[] imageByteArray = this.utilsImage.getBytesFromImage(image);
        byte[] result = new byte[1];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j <= 6; j=j+2) {
                initialShift++;
                result[i] = (byte) ((result[i] << 2) | (imageByteArray[initialShift] & 3));
            }
        }
        return result;
    }
}
