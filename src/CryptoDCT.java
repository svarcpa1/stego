import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class CryptoDCT {

    private Image image = new Image();
    private Text text = new Text();
    private BufferedImage sourceImage;
    private BufferedImage cryptoImage;
    private BufferedImage[] blocksOfImage;
    public static int m = 8;
    public static int n = 8;
    public static double pi = 3.142857;


    public CryptoDCT() {
    }

    public void codeDCT (Path path) throws IOException {
        sourceImage = image.readImageFile(path.toString());
        //duplicates source image
        //TODO has to be created new???
        cryptoImage = image.getNewImage(sourceImage);
        blocksOfImage = image.getBlocksOfImage(cryptoImage);
        double[][] DTCTransformMatrixR, DTCTransformMatrixG, DTCTransformMatrixB;

        for (BufferedImage bi : blocksOfImage) {

            ArrayList<Array2D> matrixPixelsBands = image.getMatrixPixelsBands(bi);
            DTCTransformMatrixR = getDTCTransformMatrix(matrixPixelsBands.get(0).getArray());
            System.out.println("R");
            DTCTransformMatrixG = getDTCTransformMatrix(matrixPixelsBands.get(1).getArray());
            System.out.println("G");
            DTCTransformMatrixB = getDTCTransformMatrix(matrixPixelsBands.get(2).getArray());
            System.out.println("B");

        }
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

        for ( int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
                System.out.printf("%f\t", dctTransformMatrix[i][j]);
            System.out.println();
        }
        return dctTransformMatrix;
    }

    public void addMessage(double[][] DTCTransformMatrix) {

    }
}
