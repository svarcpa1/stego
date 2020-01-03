import jpegEncoder.JpegEncoder;
import utils.UtilsGeneral;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {

        JpegEncoder jpegEncoder;
        CryptoLSB cryptoLSB = new CryptoLSB();
        UtilsGeneral utilsGeneral = new UtilsGeneral();
        int sourceMode = 0;
        int cryptoMode; // 0 -> LSB; 1 -> DCT; 100 -> error
        boolean isPossibleJpeg= true;
        long startTime, endTime;

        Path pathSource = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\fullHD.jpg");
        Path pathDecode = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\fullHD_output.png");

        //URL url = new URL("https://www.biggmagg.cz/system/newsitems/perexes/000/007/443/article/DHsDCSMzQr2VeCg0KaNvxg.jpg?1561098480");

        String textToHide = "Ahoj";

        Image image = new Image();
        BufferedImage sourceImage = image.readImageFile(pathSource.toString());
        //BufferedImage sourceImageURL = image.readImageURL(url.toString());

        int charCapacityLSB = (sourceImage.getHeight()*sourceImage.getWidth())/8-4;
        int charCapacityDCT = ((((sourceImage.getHeight()/8)*(sourceImage.getWidth()/8))*3)/8)-1;

        if (isPossibleJpeg) {
            if (charCapacityDCT < textToHide.length()) {
                cryptoMode = 0;
            } else if (charCapacityLSB >= textToHide.length()) {
                cryptoMode = 1;
            } else {
                cryptoMode = 100;
            }
        } else {
            cryptoMode = 0;
        }

        //for testing purposes
        cryptoMode = 0;

        if (cryptoMode==0) {
            startTime = System.nanoTime();
            System.out.println("LSB performed");

            cryptoLSB.code(pathSource, sourceImage ,textToHide, sourceMode);
            //cryptoLSB.codeURL(url, sourceImage, textToHide, sourceMode);
            System.out.println("Retrieved message: ");
            System.out.println(cryptoLSB.decode(pathDecode));
            endTime = System.nanoTime();
            System.out.println("Execution time = " + (endTime - startTime)/1000000 + " milliseconds");

        } else if (cryptoMode==1) {
            startTime = System.nanoTime();
            System.out.println("DCT performed");

            FileOutputStream fileOutputStream = new FileOutputStream(new File("dct_output.jpg"));
            char endCharDCT = '°';

            //jpg creation
            CryptoDCT cryptoDCT = new CryptoDCT();
            jpegEncoder = new JpegEncoder(sourceImage, 20, fileOutputStream);
            jpegEncoder.Compress(textToHide+endCharDCT);
            fileOutputStream.close();

            //jpg decode
            File file = new File("dct_output.jpg");
            FileInputStream fileInputStream = new FileInputStream(file);
            int [] coefficients = cryptoDCT.extractCoefficients(fileInputStream, (int) file.length());
            int [] bitArray = cryptoDCT.extractLSBFromCoefficientsMessage(coefficients);
            byte [] byteArray = cryptoDCT.getByteArrayDCT(bitArray);
            String a = cryptoDCT.decodeDCT(byteArray);
            String b = utilsGeneral.trimFront(a, endCharDCT);
            System.out.println("Retrieved message: ");
            System.out.println(b);
            endTime = System.nanoTime();
            System.out.println("Execution time = " + (endTime - startTime)/1000000 + " milliseconds");
        } else {
            System.out.println("Text is too long for this image");
        }
    }
}
