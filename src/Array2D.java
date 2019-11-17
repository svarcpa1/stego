/**
 *
 */
public class Array2D {
    private String color;
    private int[][] array;

    public Array2D(int[][] initialArray, String color){
        this.array = initialArray;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int[][] getArray() {
        return array;
    }

    public void setArray(int[][] array) {
        this.array = array;
    }
}
