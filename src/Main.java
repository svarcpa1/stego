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
        Path pathSource = Paths.get("");
        Path pathDecode = Paths.get("");
        int charCapacityLSB, charCapacityDCT;
        BufferedImage sourceImage;

        String textToHide = "Ahoj světe";
        Image image = new Image();

        //if file
        if (sourceMode == 0) {
            pathSource = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\fullHD.jpg");
            pathDecode = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\fullHD_output.png");

            sourceImage = image.readImageFile(pathSource.toString());

            //total number of pixels
            charCapacityLSB = sourceImage.getHeight()*sourceImage.getWidth();
            //total number of chars (Bytes) image can holds (each pixel can hold one bit of message)
            charCapacityLSB = charCapacityLSB / 8;
            //4 Bytes are used for length storing
            charCapacityLSB = charCapacityLSB - 4;
            //result is number of chars that can be inserted
            //for image 64x64px the result is 508

            //total number of 8x8 squares of pixels in the image
            charCapacityDCT = (sourceImage.getHeight()/8)*(sourceImage.getWidth()/8);
            //R, G, B bands of 8x8 squares (each band cen holds one bit of message)
            charCapacityDCT = charCapacityDCT * 3;
            //8 bit is needed fo storing one char of message
            charCapacityDCT = charCapacityDCT / 8;
            //one Byte is used for determining text length
            charCapacityDCT = charCapacityDCT - 1;
            //result is number of chars that can be inserted
            //for image 64x64px the result is 23

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

        } else {
            URL url = new URL("https://www.biggmagg.cz/system/newsitems/perexes/000/007/443/article/DHsDCSMzQr2VeCg0KaNvxg.jpg?1561098480");
            URL url2 = new URL("https://nofilmschool.com/sites/default/files/styles/article_wide/public/once_upon_a_time_in_hollywood_margot_robbie.jpg?itok=r8TKHtRn");

            pathDecode = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\url_output.png");

            sourceImage = image.readImageURL(url.toString());
            charCapacityLSB = (sourceImage.getHeight()*sourceImage.getWidth())/8-4;
            charCapacityDCT = ((((sourceImage.getHeight()/8)*(sourceImage.getWidth()/8))*3)/8)-1;

            cryptoMode=0;
        }

        //for testing purposes
        cryptoMode = 0;
        //textToHide = "";

        if (cryptoMode==0) {
            startTime = System.nanoTime();
            System.out.println("LSB performed");

            if (sourceMode == 0) {
                cryptoLSB.code(pathSource, sourceImage ,textToHide, sourceMode);
            } else {
                cryptoLSB.code(null, sourceImage ,textToHide, sourceMode);
            }

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
            jpegEncoder = new JpegEncoder(sourceImage, 100, fileOutputStream);
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
        } else if (cryptoMode==100) {
            System.out.println("Text is too long for this image");
        } else {
            System.out.println("Unexpected error");
        }
    }
}
