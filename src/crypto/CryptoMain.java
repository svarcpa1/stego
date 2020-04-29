package crypto;

import jpegEncoder.JpegEncoder;
import utils.UtilsGeneral;
import utils.UtilsImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CryptoMain {
    private CryptoLSB cryptoLSB = new CryptoLSB();
    private CryptoLSB2 cryptoLSB2 = new CryptoLSB2();
    private CryptoDCT cryptoDCT = new CryptoDCT();
    private UtilsGeneral utilsGeneral = new UtilsGeneral();
    private UtilsImage utilsImage = new UtilsImage();
    private char endCharDCT = '°';
    private String startChar = "|";
    private boolean lsb2 = false;
    private String messageLog = "";
    private int messageLength;

    public int getMessageLength() {
        return messageLength;
    }

    public String getMessageLog() {
        return messageLog;
    }

    public void code(int cryptoMode, String pathSource, BufferedImage placeholder, String message) throws Exception {
        JpegEncoder jpegEncoder;
        UtilsImage utilsImage = new UtilsImage();
        BufferedImage sourceImage;

        if (utilsGeneral.isImageLoadedFromURL(pathSource)) {
            if (cryptoMode == 0) {

                if (placeholder == null) {
                    sourceImage = utilsImage.readImageURL(pathSource);
                } else {
                    sourceImage = placeholder;
                }

                long startTime = System.nanoTime();
                cryptoLSB.code(pathSource, sourceImage, startChar + message);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime)/1000000;
                messageLog = "LSB code performed - URL (in "+ duration +")";
                System.out.println(messageLog);

            } else  if (cryptoMode == 1) {
                FileOutputStream fileOutputStream = new FileOutputStream(new File("output.jpg"));

                if (placeholder == null) {
                    sourceImage = utilsImage.readImageURL(pathSource);
                } else {
                    sourceImage = placeholder;
                }

                //jpg creation
                long startTime = System.nanoTime();
                jpegEncoder = new JpegEncoder(sourceImage, 100, fileOutputStream);
                jpegEncoder.Compress(startChar + message + endCharDCT);
                fileOutputStream.close();
                long endTime = System.nanoTime();
                long duration = (endTime - startTime)/1000000;
                messageLog = "DCT code performed - URL (in "+ duration +")";
                System.out.println(messageLog);

            } else if (cryptoMode == 100) {
                System.out.println("Text is too long for this image");

            } else if (cryptoMode == 3) {
                if (placeholder == null) {
                    sourceImage = utilsImage.readImageURL(pathSource);
                } else {
                    sourceImage = placeholder;
                }

                long startTime = System.nanoTime();
                cryptoLSB2.code(pathSource, sourceImage, startChar + message);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime)/1000000;
                messageLog = "LSB2 code performed - URL (in "+ duration +")";
                System.out.println(messageLog);

            } else {
                System.out.println("Unexpected error");
            }

        } else {
            if (placeholder == null) {
                sourceImage = utilsImage.readImageFile(pathSource);
            } else {
                sourceImage = placeholder;
            }

            if (cryptoMode == 0) {
                long startTime = System.nanoTime();
                cryptoLSB.code(pathSource, sourceImage, startChar + message);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime)/1000000;
                messageLog = "LSB code performed (in "+ duration +")";
                System.out.println(messageLog);

            } else if (cryptoMode == 1) {
                FileOutputStream fileOutputStream = new FileOutputStream(new File("output.jpg"));

                //jpg creation
                long startTime = System.nanoTime();
                jpegEncoder = new JpegEncoder(sourceImage, 100, fileOutputStream);
                jpegEncoder.Compress(startChar + message + endCharDCT);
                fileOutputStream.close();
                long endTime = System.nanoTime();
                long duration = (endTime - startTime)/1000000;
                messageLog = "DCT code performed (in "+ duration +")";
                System.out.println(messageLog);

            } else if (cryptoMode == 100) {
                System.out.println("Text is too long for this image");

            } else if (cryptoMode == 3){
                long startTime = System.nanoTime();
                cryptoLSB2.code(pathSource, sourceImage, startChar + message);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime)/1000000;
                messageLog = "LSB2 code performed (in "+ duration +")";
                System.out.println(messageLog);

            } else {
                System.out.println("Unexpected error");
            }
        }
    }

    public String decode(String path) throws IOException {
        int decodeMethod = decodeMethod(path);
        String hiddenMessage;
        if (decodeMethod == 0) {
            if(lsb2){
                long startTime = System.nanoTime();
                hiddenMessage = cryptoLSB2.decode(path).substring(1);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime)/1000000;
                messageLog = "LSB2 decode performed (in "+ duration +")";
                System.out.println(messageLog);
                return hiddenMessage;

            } else {
                long startTime = System.nanoTime();
                hiddenMessage =  cryptoLSB.decode(path).substring(1);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime)/1000000;
                messageLog = "LSB decode performed (in "+ duration +")";
                System.out.println(messageLog);
                return hiddenMessage;
            }

        } else if (decodeMethod == 1) {
            long startTime = System.nanoTime();
            File file = new File(String.valueOf(path));
            FileInputStream fileInputStream = new FileInputStream(file);
            int[] coefficients = cryptoDCT.extractCoefficients(fileInputStream, (int) file.length());
            int[] bitArray = cryptoDCT.extractLSBFromCoefficientsMessage(coefficients);
            byte[] byteArray = cryptoDCT.getByteArrayDCT(bitArray);
            String messageFull = cryptoDCT.decodeDCT(byteArray);
            hiddenMessage = utilsGeneral.trimFront(messageFull, endCharDCT).substring(1);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000000;
            messageLog = "DCT decode performed (in "+ duration +")";
            System.out.println(messageLog);
            return hiddenMessage;

        } else {
            return "Unexpected error";
        }
    }

    public int codeMethod(BufferedImage sourceImage, String message) {
        int charCapacityLSB, charCapacityDCT, charCapacityLSB2;

        //pixels
        charCapacityLSB = sourceImage.getHeight() * sourceImage.getWidth();
        //each pixel can hold 3 bit (8 pixels hold 1 char)
        charCapacityLSB = (charCapacityLSB / 8) *3;
        //32 Bytes used for length (length coded in 4 Bytes (32 bits) => 32 Bytes for storage in image => 4 char)
        charCapacityLSB = charCapacityLSB - 4;
        //one char used as a prefix (to identify which method use)
        charCapacityLSB = charCapacityLSB - 1;

        charCapacityLSB2 = sourceImage.getHeight() * sourceImage.getWidth();
        //each pixel can hold 6 bit (4 pixels hold 1 char)
        charCapacityLSB2 = (charCapacityLSB2 / 4) * 3;
        //16 Bytes used for length (length coded in 4 Bytes (32 bits) => 16 Bytes for storage in image => 4 char)
        charCapacityLSB2 = charCapacityLSB2 - 4;
        //one char used as a prefix (to identify which method use)
        charCapacityLSB2 = charCapacityLSB2 - 1;

        double height = sourceImage.getHeight();
        int intHeight = (int) Math.round(height / 8);
        double width = sourceImage.getWidth();
        int intWidth = (int) Math.round(width / 8);
        charCapacityDCT = (intHeight * intWidth);
        charCapacityDCT = charCapacityDCT * 3;
        charCapacityDCT = charCapacityDCT / 8;
        //one char used as a prefix (to identify which method use)
        //one char used as a suffix (to identify when end)
        charCapacityDCT = charCapacityDCT - 2;

        messageLength = message.length();
        if (messageLength > charCapacityLSB2) {
            return 100;
        } else if (messageLength > charCapacityLSB) {
            //use LSB2
            return 3;
        } else if (messageLength > charCapacityDCT) {
            //use LSB
            return 0;
        } else {
            //use DCT
            return 1;
        }
    }

    public int decodeMethod(String path) {
        if (utilsGeneral.isImageLoadedFromURL(path)) {
            if (utilsImage.getImageTypeURL(path)==1) {
                //dct
                return 1;
            } else {
                //lsb or lsb2
                return 0;
            }
        } else {
            if (isFileJpg(path)) {
                //dct
                return 1;
            } else {
                //lsb or lsb2
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

        if (utilsGeneral.isImageLoadedFromURL(pathString) || pathString.isEmpty()) {
            return "code";
        }

        if (decodeMethod == 0) {
            //get first char using LSB
            //if char match with defined -> decode
            firstChar = cryptoLSB.decodeFirstChar(pathString);
            if (!firstChar.equals(startChar)) {
                firstChar = cryptoLSB2.decodeFirstChar(pathString);
                if (firstChar.equals(startChar)) {
                    lsb2 = true;
                }
            }

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

        if (firstChar.equals(startChar) ) {
            return "decode";
        } else {
            return "code";
        }
    }
}

