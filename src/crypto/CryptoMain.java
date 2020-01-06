package crypto;

import jpegEncoder.JpegEncoder;
import utils.UtilsGeneral;
import utils.UtilsImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class CryptoMain {
    private CryptoLSB cryptoLSB = new CryptoLSB();
    private CryptoDCT cryptoDCT = new CryptoDCT();
    private UtilsGeneral utilsGeneral = new UtilsGeneral();
    private char endCharDCT = '°';

    public void code(int cryptoMode, Path pathSource, String message, int sourceMode) throws Exception {
        JpegEncoder jpegEncoder;
        UtilsImage utilsImage = new UtilsImage();

        //for testing
        //cryptoMode=0;

        if (cryptoMode == 0) {
            System.out.println("LSB code performed");
            BufferedImage sourceImage = utilsImage.readImageFile(pathSource.toString());
            cryptoLSB.code(pathSource, sourceImage, message, sourceMode);

        } else if (cryptoMode == 1) {
            System.out.println("DCT code performed");
            FileOutputStream fileOutputStream = new FileOutputStream(new File("output.jpg"));
            BufferedImage sourceImage = utilsImage.readImageFile(pathSource.toString());
            //jpg creation
            jpegEncoder = new JpegEncoder(sourceImage, 100, fileOutputStream);
            jpegEncoder.Compress(message + endCharDCT);
            fileOutputStream.close();

        } else if (cryptoMode == 100) {
            System.out.println("Text is too long for this image");

        } else {
            System.out.println("Unexpected error");
        }
    }

    public String decode(Path path) throws IOException {
        int decodeMethod = getDecodeMethod(path);

        //for testing
        //decodeMethod = 0;

        if (decodeMethod == 0) {
            System.out.println("LSB decode performed");
            return cryptoLSB.decode(path);

        } else if (decodeMethod == 1) {
            System.out.println("DCT decode performed");
            File file = new File(String.valueOf(path));
            FileInputStream fileInputStream = new FileInputStream(file);
            int[] coefficients = cryptoDCT.extractCoefficients(fileInputStream, (int) file.length());
            int[] bitArray = cryptoDCT.extractLSBFromCoefficientsMessage(coefficients);
            byte[] byteArray = cryptoDCT.getByteArrayDCT(bitArray);
            String a = cryptoDCT.decodeDCT(byteArray);
            String b = utilsGeneral.trimFront(a, endCharDCT);
            return b;

        } else {
            return "Unexpected error";
        }
    }

    public int codeMethod(BufferedImage sourceImage, String message, boolean isJpg) {
        int charCapacityLSB, charCapacityDCT;

        //total number of pixels
        charCapacityLSB = sourceImage.getHeight() * sourceImage.getWidth();
        //total number of chars (Bytes) image can holds (each pixel can hold one bit of message)
        charCapacityLSB = charCapacityLSB / 8;
        //4 Bytes are used for length storing
        charCapacityLSB = charCapacityLSB - 4;
        //result is number of chars that can be inserted
        //for image 64x64px the result is 508

        //total number of 8x8 squares of pixels in the image
        double height = sourceImage.getHeight();
        int intHeight = (int) Math.round(height / 8);
        double width = sourceImage.getWidth();
        int intWidth = (int) Math.round(width / 8);
        charCapacityDCT = (intHeight * intWidth);
        //R, G, B bands of 8x8 squares (each band cen holds one bit of message)
        charCapacityDCT = charCapacityDCT * 3;
        //8 bit is needed fo storing one char of message
        charCapacityDCT = charCapacityDCT / 8;
        //one Byte is used for determining text length
        charCapacityDCT = charCapacityDCT - 1;
        //result is number of chars that can be inserted
        //for image 64x64px the result is 23

        if (isJpg) {
            return 1;
        } else {
            if (message.length() > charCapacityLSB) {
                return 100;
            } else if (message.length() > charCapacityDCT) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public int getDecodeMethod(Path path) {
        int decodeMethod;
        if (isFileJpg(path)) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean isFileJpg(Path path) {
        boolean isJpg;

        if (path.toString().endsWith(".jpg") || path.toString().endsWith(".jpeg")) {
            isJpg = true;
        } else {
            isJpg = false;
        }
        return isJpg;
    }
}

//TODO url
//URL url = new URL("https://www.biggmagg.cz/system/newsitems/perexes/000/007/443/article/DHsDCSMzQr2VeCg0KaNvxg.jpg?1561098480");
//URL url2 = new URL("https://nofilmschool.com/sites/default/files/styles/article_wide/public/once_upon_a_time_in_hollywood_margot_robbie.jpg?itok=r8TKHtRn");
