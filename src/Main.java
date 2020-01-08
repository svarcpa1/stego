
import crypto.CryptoMain;
import utils.UtilsGeneral;
import utils.UtilsImage;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) throws Exception {

        CryptoMain cryptoMain = new CryptoMain();
        UtilsImage utilsImage = new UtilsImage();
        UtilsGeneral utilsGeneral = new UtilsGeneral();
        int cryptoMode; // 0 -> LSB; 1 -> DCT; 100 -> error
        String pathToImage;
        BufferedImage sourceImage;

        //pathToImage = "https://pbs.twimg.com/profile_images/1131115264563662848/7b4Wvlgv.png";
        pathToImage = "C:\\_DATA-local\\WS_Java\\stego\\output.jpg";

        String decide = cryptoMain.decideCodeOrDecode(pathToImage);

        if(decide == "code") {
            System.out.println("CODE");
            String textToHide = "AHOJ";

            if (utilsGeneral.isImageLoadedFromURL(pathToImage)) {
                sourceImage = utilsImage.readImageURL(pathToImage);
            } else {
                sourceImage = utilsImage.readImageFile(pathToImage);
            }

            cryptoMode = cryptoMain.codeMethod(sourceImage, textToHide);
            cryptoMain.code(cryptoMode, pathToImage, textToHide);

        } else {
            System.out.println("DECODE");
            //TODO decode URL
            if (!utilsGeneral.isImageLoadedFromURL(pathToImage)) {
                String message = cryptoMain.decode(pathToImage);
                System.out.println(message);
            } else {
                System.out.println("URL address cannot be decoded yet");
            }
        }
    }
}
