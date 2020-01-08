
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
        String pathSource, pathDecode, pathDecide;
        BufferedImage sourceImage;

        //decode or code -----------------------------------------------------------------------------------------------
        //pathDecide = "C:\\_DATA-local\\WS_Java\\stego\\lenna.png";
        pathDecide = "https://pbs.twimg.com/profile_images/1131115264563662848/7b4Wvlgv.png";
        String decide = cryptoMain.decideCodeOrDecode(pathDecide);
        System.out.println(decide);

        //message definition -------------------------------------------------------------------------------------------
        String textToHide = "AHOJ";

        //selecting crypto method --------------------------------------------------------------------------------------
        //pathSource = "C:\\_DATA-local\\WS_Java\\stego\\lenna.png";
        pathSource = "https://pbs.twimg.com/profile_images/1131115264563662848/7b4Wvlgv.png";
        if (utilsGeneral.isImageLoadedFromURL(pathSource)) {
            sourceImage = utilsImage.readImageURL(pathSource);
        } else {
            sourceImage = utilsImage.readImageFile(pathSource);
        }

        cryptoMode = cryptoMain.codeMethod(sourceImage, textToHide);

        //crypto performing --------------------------------------------------------------------------------------------
        cryptoMain.code(cryptoMode, pathSource, textToHide);

        //decode performing --------------------------------------------------------------------------------------------
        //TODO decode URL
        pathDecode = "C:\\_DATA-local\\WS_Java\\stego\\output.jpg";
        String message = cryptoMain.decode(pathDecode);
        System.out.println(message);
    }
}
