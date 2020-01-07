
import crypto.CryptoMain;
import utils.UtilsImage;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {

        CryptoMain cryptoMain = new CryptoMain();
        UtilsImage utilsImage = new UtilsImage();
        int cryptoMode; // 0 -> LSB; 1 -> DCT; 100 -> error
        Path pathSource, pathDecode, pathDecide;
        BufferedImage sourceImage;

        //decode or code -----------------------------------------------------------------------------------------------
        pathDecide = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\margot.jpg");
        String decide = cryptoMain.decideCodeOrDecode(pathDecide);
        System.out.println(decide);

        //message definition -------------------------------------------------------------------------------------------
        String textToHide = "AHOJ";

        //selecting crypto method --------------------------------------------------------------------------------------
        /*pathSource = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\lenna.png");
        sourceImage = utilsImage.readImageFile(pathSource.toString());
        cryptoMode = cryptoMain.codeMethod(sourceImage, textToHide, false);*/

        //crypto performing --------------------------------------------------------------------------------------------
        /*cryptoMain.code(cryptoMode, pathSource, textToHide, 0);*/

        //decode performing --------------------------------------------------------------------------------------------
        /*pathDecode = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\output.jpg");
        String message = cryptoMain.decode(pathDecode);
        System.out.println(message);*/
    }
}
