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
        int sourceMode = 1;
        UtilsGeneral utilsGeneral = new UtilsGeneral();

        //squares
        Path pathSource = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\stripes.bmp");
        Path pathDecode = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\lenna_output.png");

        URL url = new URL("https://www.biggmagg.cz/system/newsitems/perexes/000/007/443/article" +
                "/DHsDCSMzQr2VeCg0KaNvxg.jpg?1561098480");

        String textToHide = "aaa";
        char endCharDCT = '°';

        //cryptoLSB.code(pathSource, textToHide, sourceMode);
        //crypto.codeURL(url, textToHide, sourceMode);
        //System.out.println(cryptoLSB.decode(pathDecode));

        Image image = new Image();
        BufferedImage sourceImage = image.readImageFile(pathSource.toString());
        FileOutputStream fileOutputStream = new FileOutputStream(new File("aaa.jpg"));

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
        System.out.println(b);
    }
}
