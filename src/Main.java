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
        int sourceMode = 1;
        int cryptoMode; // 0 -> LSB; 1 -> DCT
        boolean isPossibleJpeg= true;

        Path pathSource = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\stripes.bmp");
        Path pathDecode = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\stripes_output.bmp");

        URL url = new URL("https://www.biggmagg.cz/system/newsitems/perexes/000/007/443/article" +
                "/DHsDCSMzQr2VeCg0KaNvxg.jpg?1561098480");

        String textToHide = "12345678912345678912345";

        Image image = new Image();
        BufferedImage sourceImage = image.readImageFile(pathSource.toString());

        int charCapacityLSB = (sourceImage.getHeight()*sourceImage.getWidth())/8-4;
        int charCapacityDCT = ((((sourceImage.getHeight()/8)*(sourceImage.getWidth()/8))*3)/8)-1;

        if (isPossibleJpeg) {
            if (charCapacityDCT<textToHide.length()) {
                cryptoMode=0;
            } else {
                cryptoMode=1;
            }
        } else {
            cryptoMode =0;
        }

        if (cryptoMode==0) {
            System.out.println("LSB performed");

            cryptoLSB.code(pathSource, sourceImage ,textToHide, sourceMode);
            //cryptoLSB.codeURL(url, sourceImage, textToHide, sourceMode);
            System.out.println("Retrieved message: ");
            System.out.println(cryptoLSB.decode(pathDecode));

        } else if (cryptoMode==1) {
            System.out.println("DCT performed");

            FileOutputStream fileOutputStream = new FileOutputStream(new File("aaa.jpg"));
            char endCharDCT = '°';

            //jpg creation
            CryptoDCT cryptoDCT = new CryptoDCT();
            jpegEncoder = new JpegEncoder(sourceImage, 80, fileOutputStream);
            jpegEncoder.Compress(textToHide+endCharDCT);
            fileOutputStream.close();

            //jpg decode
            File file = new File("aaa.jpg");
            FileInputStream fileInputStream = new FileInputStream(file);
            int [] coefficients = cryptoDCT.extractCoefficients(fileInputStream, (int) file.length());
            int [] bitArray = cryptoDCT.extractLSBFromCoefficientsMessage(coefficients);
            byte [] byteArray = cryptoDCT.getByteArrayDCT(bitArray);
            String a = cryptoDCT.decodeDCT(byteArray);
            String b = utilsGeneral.trimFront(a, endCharDCT);
            System.out.println("Retrieved message: ");
            System.out.println(b);
        }
    }
}
