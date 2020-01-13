
import crypto.CryptoMain;
import utils.UtilsGeneral;
import utils.UtilsImage;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {

        Gui gui = new Gui();
        JFrame jFrame = new JFrame("SMART SteGo");
        jFrame.setContentPane(gui.getjPanel());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void performSteganography(String path, String messageFromGui) throws Exception {
        CryptoMain cryptoMain = new CryptoMain();
        UtilsImage utilsImage = new UtilsImage();
        UtilsGeneral utilsGeneral = new UtilsGeneral();
        int cryptoMode; // 0 -> LSB; 1 -> DCT; 100 -> error
        String pathToImage;
        BufferedImage sourceImage;

        pathToImage = path;
        String decide = cryptoMain.decideCodeOrDecode(pathToImage);

        if(decide == "code") {
            System.out.println("CODE");
            String textToHide = messageFromGui;

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
