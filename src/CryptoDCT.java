import jpegDecoder.HuffmanDecode;
import java.io.IOException;
import java.io.InputStream;

public class CryptoDCT {
    private static byte[] deZigZag = {
            0, 1, 5, 6, 14, 15, 27, 28,
            2, 4, 7, 13, 16, 26, 29, 42,
            3, 8, 12, 17, 25, 30, 41, 43,
            9, 11, 18, 24, 31, 40, 44, 53,
            10, 19, 23, 32, 39, 45, 52, 54,
            20, 22, 33, 38, 46, 51, 55, 60,
            21, 34, 37, 47, 50, 56, 59, 61,
            35, 36, 48, 49, 57, 58, 62, 63 };

    public int[] extractCoefficients(InputStream inputStream, int flength) throws IOException {
        byte[] carrier = new byte[flength];
        inputStream.read(carrier);
        HuffmanDecode huffmanDecode = new HuffmanDecode(carrier);
        int[] coefficients = huffmanDecode.decode();
        return coefficients;
    }

    public int[] extractLSBFromCoefficientsMessage (int[] coefficients) {
        int[] bitArray = new int[coefficients.length/64];
        int index = 0;

        for (int i = deZigZag[23]; i < coefficients.length; i=i+64) {
            if (coefficients[i] % 2 == 0) {
                bitArray[index] = 0;
            } else {
                bitArray[index] = 1;
            }
            index++;
        }

        return bitArray;
    }

    public byte [] getByteArrayDCT (int[] lsbValues) {
        byte [] byteArrayChar = new byte[lsbValues.length/8];
        int index2 = 0;
        for (int i = 0; i < lsbValues.length; i=i+8) {
            String byteCharString = Integer.toString(lsbValues[i]);
            byteCharString += Integer.toString(lsbValues[i+1]);
            byteCharString += Integer.toString(lsbValues[i+2]);
            byteCharString += Integer.toString(lsbValues[i+3]);
            byteCharString += Integer.toString(lsbValues[i+4]);
            byteCharString += Integer.toString(lsbValues[i+5]);
            byteCharString += Integer.toString(lsbValues[i+6]);
            byteCharString += Integer.toString(lsbValues[i+7]);
            byte byteChar = (byte)Integer.parseInt(byteCharString, 2);

            byteArrayChar[index2] = byteChar;
            index2++;
        }
        return byteArrayChar;
    }

    public String decodeDCT (byte[] messageBytes) throws IOException {
        String message = new String(messageBytes);
        return message;
    }
}