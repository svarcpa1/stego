
import crypto.CryptoMain;
import utils.UtilsGeneral;
import utils.UtilsImage;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {

        Gui gui = new Gui();
        JFrame jFrame = new JFrame("SMART StegGo");
        jFrame.setContentPane(gui.getjPanel());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void performSteganography(String path, BufferedImage placeholder, String messageFromGui) throws Exception {
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

            if (placeholder!=null) {
                sourceImage = placeholder;
            } else {
                if (utilsGeneral.isImageLoadedFromURL(pathToImage)) {
                    sourceImage = utilsImage.readImageURL(pathToImage);
                } else {
                    sourceImage = utilsImage.readImageFile(pathToImage);
                }
            }

            cryptoMode = cryptoMain.codeMethod(sourceImage, textToHide);
            long startTime = System.nanoTime();

            if (placeholder!=null) {
                cryptoMain.code(cryptoMode, pathToImage, placeholder, textToHide);
            } else {
                cryptoMain.code(cryptoMode, pathToImage, null, textToHide);
            }

            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000000;
            System.out.println("duration code: "+duration);

        } else {
            System.out.println("DECODE");
            //TODO decode URL
            if (!utilsGeneral.isImageLoadedFromURL(pathToImage)) {
                long startTime = System.nanoTime();
                String message = cryptoMain.decode(pathToImage);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime)/1000000;
                System.out.println("duration decode: "+duration);
                System.out.println(message);
            } else {
                System.out.println("URL address cannot be decoded yet");
            }
        }
    }
}
