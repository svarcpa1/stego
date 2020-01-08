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
    private UtilsImage utilsImage = new UtilsImage();
    private char endCharDCT = '°';
    private String startChar = "|";

    public void code(int cryptoMode, String pathSource, String message) throws Exception {
        JpegEncoder jpegEncoder;
        UtilsImage utilsImage = new UtilsImage();

        if (utilsGeneral.isImageLoadedFromURL(pathSource)) {
            if (cryptoMode == 0) {
                System.out.println("LSB code performed - URL");
                BufferedImage sourceImage = utilsImage.readImageURL(pathSource);
                cryptoLSB.code(pathSource, sourceImage, startChar + message);
            } else  if (cryptoMode == 1) {
                System.out.println("DCT code performed - URL");
                FileOutputStream fileOutputStream = new FileOutputStream(new File("output.jpg"));
                BufferedImage sourceImage = utilsImage.readImageURL(pathSource);
                //jpg creation
                jpegEncoder = new JpegEncoder(sourceImage, 100, fileOutputStream);
                jpegEncoder.Compress(startChar + message + endCharDCT);
                fileOutputStream.close();
            } else if (cryptoMode == 100) {
                System.out.println("Text is too long for this image");

            } else {
                System.out.println("Unexpected error");
            }

        } else {
            if (cryptoMode == 0) {
                System.out.println("LSB code performed");
                BufferedImage sourceImage = utilsImage.readImageFile(pathSource);
                cryptoLSB.code(pathSource, sourceImage, startChar + message);

            } else if (cryptoMode == 1) {
                System.out.println("DCT code performed");
                FileOutputStream fileOutputStream = new FileOutputStream(new File("output.jpg"));
                BufferedImage sourceImage = utilsImage.readImageFile(pathSource);
                //jpg creation
                jpegEncoder = new JpegEncoder(sourceImage, 100, fileOutputStream);
                jpegEncoder.Compress(startChar + message + endCharDCT);
                fileOutputStream.close();

            } else if (cryptoMode == 100) {
                System.out.println("Text is too long for this image");

            } else {
                System.out.println("Unexpected error");
            }
        }
    }

    public String decode(String path) throws IOException {

        int decodeMethod = decodeMethod(path);

        if (decodeMethod == 0) {
            System.out.println("LSB decode performed");
            return cryptoLSB.decode(path).substring(1);

        } else if (decodeMethod == 1) {
            System.out.println("DCT decode performed");
            File file = new File(String.valueOf(path));
            FileInputStream fileInputStream = new FileInputStream(file);
            int[] coefficients = cryptoDCT.extractCoefficients(fileInputStream, (int) file.length());
            int[] bitArray = cryptoDCT.extractLSBFromCoefficientsMessage(coefficients);
            byte[] byteArray = cryptoDCT.getByteArrayDCT(bitArray);
            String a = cryptoDCT.decodeDCT(byteArray);
            String b = utilsGeneral.trimFront(a, endCharDCT).substring(1);
            return b;

        } else {
            return "Unexpected error";
        }
    }

    public int codeMethod(BufferedImage sourceImage, String message) {
        int charCapacityLSB, charCapacityDCT;

        charCapacityLSB = sourceImage.getHeight() * sourceImage.getWidth();
        charCapacityLSB = charCapacityLSB / 8;
        charCapacityLSB = charCapacityLSB - 4;
        charCapacityLSB = charCapacityLSB - 1;

        double height = sourceImage.getHeight();
        int intHeight = (int) Math.round(height / 8);
        double width = sourceImage.getWidth();
        int intWidth = (int) Math.round(width / 8);
        charCapacityDCT = (intHeight * intWidth);
        charCapacityDCT = charCapacityDCT * 3;
        charCapacityDCT = charCapacityDCT / 8;
        charCapacityDCT = charCapacityDCT - 2;

        if (message.length() > charCapacityLSB) {
            return 100;
        } else if (message.length() > charCapacityDCT) {
            return 0;
        } else {
            return 1;
        }
    }

    public int decodeMethod(String path) {
        if (utilsGeneral.isImageLoadedFromURL(path)) {
            System.out.println("URL file");
            if (utilsImage.getImageTypeURL(path)==1) {
                //dct
                return 1;
            } else {
                //lsb
                return 0;
            }
        } else {
            System.out.println("File file");
            if (isFileJpg(path)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public boolean isFileJpg(String path) {
        boolean isJpg;

        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            isJpg = true;
        } else {
            isJpg = false;
        }
        return isJpg;
    }

    public String decideCodeOrDecode(String pathString) throws IOException {
        int decodeMethod = decodeMethod(pathString);
        String firstChar = "";

        if (decodeMethod == 0) {
            //get first char using LSB
            //if char match with defined -> decode
            firstChar = cryptoLSB.decodeFirstChar(pathString);

        } else {
            //get first char using DCT
            //if char match with defined -> decode
            File file = new File(String.valueOf(pathString));
            FileInputStream fileInputStream = new FileInputStream(file);
            int[] coefficients = cryptoDCT.extractCoefficients(fileInputStream, (int) file.length());
            int[] bitArray = cryptoDCT.extractLSBFromCoefficientsMessageFirstChar(coefficients);
            byte[] byteArray = cryptoDCT.getByteArrayDCT(bitArray);
            firstChar = cryptoDCT.decodeDCT(byteArray);
        }


        if (firstChar.equals(startChar)) {
            return "decode";
        } else {
            return "code";
        }
    }
}

