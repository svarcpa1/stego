package utils;

import java.util.Arrays;

public class UtilsGeneral {

    public String trimFront(String text, char character) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != character) {
                result = result+text.charAt(i);
            } else {
              return result;
            }
        }
        return result;
    }

    public byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
