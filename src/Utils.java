import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {

    public void bitsToFile (ArrayList<Integer> bits, String fileName) throws IOException {
        // .png starts with: 137, 80, 78, 71, 13, 10, 26, 10 ->
        // 10001001, 1010000, 1001110, 1000111, 1101, 1010, 11010, 1010
        FileWriter writer = new FileWriter(fileName + ".txt");
        for(Integer b: bits) {
            try {
                writer.write(b.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer[] bitsToBytes (ArrayList<Integer> bits) throws IOException {

        String actualByteString = "";
        int actualByte;
        ArrayList<Integer> byteArray = new ArrayList<>();
        int j = 0;

        for (int i = 0; i < bits.size(); i++){
            j++;
            actualByteString = actualByteString + bits.get(i).toString();
            if(j % 8 == 0){
                //makes byte (value) from String
                actualByte = Integer.parseInt(actualByteString,2);
                //adding byte to byte array
                byteArray.add(actualByte);
                //del tmp actualByte
                actualByteString = "";
            }
        }

        Integer[] arr = byteArray.toArray(new Integer[byteArray.size()]);
        return arr;
    }

    public byte[] integerToByte (Integer[] integer) {
        byte[] bytes = new byte[integer.length];

        for (int i = 0; i< integer.length; i++){
            bytes[i] = integer[i].byteValue();
        }

        return bytes;
    }

    public BufferedImage getImageFrombyteArray(byte[] inputData) throws Exception{
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputData);
        try {
            return ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}