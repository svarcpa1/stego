import java.util.ArrayList;

public class Text {

    public byte[] getBytesFromText(String text){
        //TODO simplify
        byte[] bytesFromString;
        bytesFromString = text.getBytes();
        return bytesFromString;
    }

/*    public byte[] getTextLengh(int BytesFromTextLength){
        //TODO simplify
        byte byte0 = (byte)((BytesFromTextLength & 0x000000FF));
        byte byte1 = (byte)((BytesFromTextLength & 0x0000FF00) >>> 8);
        byte byte2 = (byte)((BytesFromTextLength & 0x00FF0000) >>> 16);
        byte byte3 = (byte)((BytesFromTextLength & 0xFF000000) >>> 24);

        return(new byte[]{byte3,byte2,byte1,byte0});
    }*/
}
