import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;

public class CryptoDCT {

    private Image image = new Image();
    private Text text = new Text();
    private BufferedImage sourceImage;
    private BufferedImage cryptoImage;
    private BufferedImage[] blocksOfImage;
    private static int m = 8;
    private static int n = 8;
    private static double pi = 3.142857;
    private int messageBitCounter = 7;


    public CryptoDCT() {
    }

    public void codeDCT (Path path) throws IOException {
        sourceImage = image.readImageFile(path.toString());
        //duplicates source image
        //TODO has to be created new???
        cryptoImage = image.getNewImage(sourceImage);
        blocksOfImage = image.getBlocksOfImage(cryptoImage);
        double[][] DTCTransformMatrixR, DTCTransformMatrixG, DTCTransformMatrixB;
        int[][] quantiseMatrixR, quantiseMatrixG, quantiseMatrixB;
        int[][] recountQuantiseMatrixR, recountQuantiseMatrixG, recountQuantiseMatrixB;
        ArrayList<int[][]> quantiseMatrixRGB = new ArrayList<>();

        for (BufferedImage bi : blocksOfImage) {

            ArrayList<Array2D> matrixPixelsBands = image.getMatrixPixelsBands(bi);
            DTCTransformMatrixR = getDTCTransformMatrix(matrixPixelsBands.get(0).getArray());
            quantiseMatrixR = getQuantiseCoefficients(DTCTransformMatrixR);
            DTCTransformMatrixG = getDTCTransformMatrix(matrixPixelsBands.get(1).getArray());
            quantiseMatrixG = getQuantiseCoefficients(DTCTransformMatrixG);
            DTCTransformMatrixB = getDTCTransformMatrix(matrixPixelsBands.get(2).getArray());
            quantiseMatrixB = getQuantiseCoefficients(DTCTransformMatrixB);

            quantiseMatrixRGB.add(quantiseMatrixR);
            quantiseMatrixRGB.add(quantiseMatrixG);
            quantiseMatrixRGB.add(quantiseMatrixB);

        }
        quantiseMatrixRGB = addMessageFirst(quantiseMatrixRGB, "Hello");

    }

    public double[][] getDTCTransformMatrix(int[][] imageMatrix){

        double[][] dctTransformMatrix = new double[m][n];
        double ci, cj, tmpDCTValue, tmpSum;

        //TODO simplify 4x for --> BAD
        //TODO consulting
        for(int i = 0; i<m; i++) {
            for(int j = 0; j<n; j++) {

                if (i == 0){
                    ci = 1 / Math.sqrt(m);
                } else {
                    ci = Math.sqrt(2) / Math.sqrt(m);
                }
                if (j == 0){
                    cj = 1 / Math.sqrt(n);
                } else {
                    cj = Math.sqrt(2) / Math.sqrt(n);
                }

                tmpSum = 0;
                for (int i_image = 0; i_image < m; i_image++) {
                    for (int j_image = 0; j_image < n; j_image++) {

                        tmpDCTValue = imageMatrix[i_image][j_image] *
                                Math.cos((2 * i_image + 1) * i * pi / (2 * m)) *
                                Math.cos((2 * j_image + 1) * j * pi / (2 * n));
                        tmpSum = tmpSum + tmpDCTValue;
                    }
                }
                dctTransformMatrix[i][j] = ci * cj * tmpSum;
            }
        }
        /*for ( int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
                System.out.print("%f\t" + dctTransformMatrix[i][j]);
            System.out.println();
        }*/
        return dctTransformMatrix;
    }

    public int[][] getQuantiseCoefficients(double[][] DTCTransformMatrix) {
        int[][] quantiseResult = new int[DTCTransformMatrix.length][DTCTransformMatrix[1].length];
        int[][] quantiseMatrix =   {{16, 11, 10, 16, 24, 40, 51, 61},
                                    {12, 12, 14, 19, 26, 58, 60, 55},
                                    {14, 13, 16, 24, 40, 57, 69, 56},
                                    {14, 17, 22, 29, 51, 87, 80, 62},
                                    {18, 22, 37, 56, 68, 109, 103, 77},
                                    {24, 35, 55, 64, 81, 104, 113, 92},
                                    {49, 64, 78, 87, 103, 121, 120, 101},
                                    {72, 92, 95, 98, 112, 100, 103, 99}};

        //TODO delete 8
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                quantiseResult[i][j] = (int)Math.round(DTCTransformMatrix[i][j]/quantiseMatrix[i][j]);
            }
        }

        /*for ( int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
                System.out.print(DTCTransformMatrix[i][j]+ " ");
            System.out.println();
        }
        System.out.println();
        for ( int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
                System.out.print(quantiseMatrix[i][j]+ " ");
            System.out.println();
        }
        System.out.println();
        for ( int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
                System.out.print(quantiseResult[i][j]+ " ");
            System.out.println();
        }*/

        return quantiseResult;
    }

    public ArrayList<int[][]> addMessageFirst(ArrayList<int[][]> quantiseMatrixRGB, String message) {
        Utils utils = new Utils();
        int bitTextShift = 7;
        int byteTextShift = 0;
        byte[] textByteArray = text.getBytesFromText(message);

        for (int i = 0; i < textByteArray.length*8; i++) {
            byte[] firstValueByte = utils.integerToByte(quantiseMatrixRGB.get(i)[0][0]);

            firstValueByte[3] = (byte) ((firstValueByte[3] & 0xFE) | ((int) textByteArray[byteTextShift] >>> bitTextShift) & 1);
            if (bitTextShift <= 0) {
                bitTextShift = 7;
                byteTextShift++;
            } else {
                bitTextShift--;
            }
        }
        return quantiseMatrixRGB;
    }
}
