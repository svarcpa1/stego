import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class Crypto {

    private Image image = new Image();
    private Text text = new Text();
    private int initialShift = 32;
    private BufferedImage sourceImage;
    private BufferedImage cryptoImage;

    public Crypto() {
    }

    public void code (@NotNull Path path, String textToHide, int sourceMode) throws Exception {
        //takes source image
        sourceImage = image.readImageFile(path.toString());
        //duplicates source image
        //TODO has to be created new???
        cryptoImage = image.getNewImage(sourceImage);
        //add encrypted text to source image
        cryptoImage = addTextToImage(cryptoImage, textToHide);

        //creates file
        File outputFile;
        outputFile = new File(image.getImageOutName(path));
        ImageIO.write(cryptoImage, image.getImageType(path), outputFile);
    }

    public void codeURL (@NotNull URL path, String textToHide, int sourceMode) throws Exception {
        //takes source image
        sourceImage = image.readImageURL(path.toString());
        //duplicates source image
        //TODO has to be created new???
        cryptoImage = image.getNewImage(sourceImage);
        //add encrypted text to source image
        cryptoImage = addTextToImage(cryptoImage, textToHide);

        //creates file
        File outputFile;
        outputFile = new File("url_output.png");
        ImageIO.write(cryptoImage, "png", outputFile);
    }

    public String decode (@NotNull Path path) throws IOException {
        byte[] decodedByteArray;

        //TODO has to be created new???
        BufferedImage imageToBeDecoded = image.readImageFile(path.toString());
        BufferedImage newImageToBeDecoded = image.getNewImage(imageToBeDecoded);

        decodedByteArray = extractTextFromImage(newImageToBeDecoded);

        String message = new String(decodedByteArray);
        return message;
    }

    public BufferedImage addTextToImage(BufferedImage image, String message){
        initialShift = 32;
        byte[] imageByteArray = this.image.getBytesFromImage(image);
        byte[] textByteArray = text.getBytesFromText(message);
        //byte[] textLengthByteArray = text.getTextLengh(textByteArray.length);

        //TODO isn't message too long?
        for(int i = 0; i<textByteArray.length; i++){

            for(int j = 7; j>=0; j--){
                initialShift++;
                //TODO simplify the code
                imageByteArray[initialShift] =
                        (byte)((imageByteArray[initialShift] & 0xFE) | ((int)textByteArray[i] >>> j) & 1);
            }
        }
        return image;
    }

    public byte[] extractTextFromImage(BufferedImage image){
        initialShift=32;
        byte[] imageByteArray = this.image.getBytesFromImage(image);
        byte[] result = new byte[100];

        for(int i=0; i<result.length; i++)
        {
            for(int j=0; j<=7; j++)
            {
                initialShift++;
                //TODO simplify the code
                result[i] = (byte)((result[i] << 1) | (imageByteArray[initialShift] & 1));
            }
        }
        return result;
    }
}
