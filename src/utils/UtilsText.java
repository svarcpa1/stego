package utils;

public class UtilsText {

    public byte[] getBytesFromText(String text) {
        return text.getBytes();
    }

    public byte[] getTextLength(int textLength) {
        byte byte0 = (byte) ((textLength & 0x000000FF));
        byte byte1 = (byte) ((textLength & 0x0000FF00) >>> 8);
        byte byte2 = (byte) ((textLength & 0x00FF0000) >>> 16);
        byte byte3 = (byte) ((textLength & 0xFF000000) >>> 24);

        return (new byte[]{byte3, byte2, byte1, byte0});
    }
}
