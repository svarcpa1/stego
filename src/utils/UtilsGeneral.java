package utils;

public class UtilsGeneral {

    public String trimFront(String text, char character) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != character) {
                result = result + text.charAt(i);
            } else {
                return result;
            }
        }
        return result;
    }

    public boolean isImageLoadedFromURL(String pathString) {
        if (pathString.contains("https://") || pathString.contains("http://")) {
            return true;
        } else {
            return false;
        }
    }
}
