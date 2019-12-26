import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
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

    public CryptoDCT() {
    }

    public void codeDCT (Path path) throws IOException {
        sourceImage = image.readImageFile(path.toString());
        //duplicates source image
        //TODO has to be created new???
        cryptoImage = image.getNewImage(sourceImage);
        blocksOfImage = image.getBlocksOfImage(cryptoImage);
        double[][] DCTransformMatrixR, DCTransformMatrixG, DCTransformMatrixB;
        int[][] quantiseMatrixR, quantiseMatrixG, quantiseMatrixB;
        ArrayList<int[][]> quantiseMatrixRGB = new ArrayList<>();

        //TODO go only thought necessary blocks - count how many blocks is needed
        for (BufferedImage bi : blocksOfImage) {

            ArrayList<Array2D> matrixPixelsBands = image.getMatrixPixelsBands(bi);
            //red
            DCTransformMatrixR = getDCTransformMatrix(matrixPixelsBands.get(0).getArray());
            quantiseMatrixR = getQuantiseCoefficients(DCTransformMatrixR);
            //green
            DCTransformMatrixG = getDCTransformMatrix(matrixPixelsBands.get(1).getArray());
            quantiseMatrixG = getQuantiseCoefficients(DCTransformMatrixG);
            //blue
            DCTransformMatrixB = getDCTransformMatrix(matrixPixelsBands.get(2).getArray());
            quantiseMatrixB = getQuantiseCoefficients(DCTransformMatrixB);

            quantiseMatrixRGB.add(quantiseMatrixR);
            quantiseMatrixRGB.add(quantiseMatrixG);
            quantiseMatrixRGB.add(quantiseMatrixB);

        }
        //order: R, G, B, R, G, B, R.....
        quantiseMatrixRGB = addMessageFirst(quantiseMatrixRGB, "Hello");
        System.out.println("stop");

        for (int[][] bi2 : quantiseMatrixRGB) {
            int [][] revertQCoef = revertGetQuantiseCoefficients(bi2);
            int [][] revertDCT = revertGetDCTransformMatrix(revertQCoef);
            System.out.println("stop");
        }


    }

    public double[][] getDCTransformMatrix(int[][] imageMatrix){

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

        //TODO delete static 8
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                //Bij = round(Gij/Qij)
                quantiseResult[i][j] = (int)Math.round(DTCTransformMatrix[i][j]/quantiseMatrix[i][j]);
            }
        }
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
        //LSB edited according to text
        System.out.println("stop");
        return quantiseMatrixRGB;
    }


//----------------------------------------------------------------------------------------------------------------------
    //geeks
    public int[] getZigZag (int[][] matrix, int m, int n) {
        int row = 0, col = 0;
        String zigZagResultString="";
        int [] zigZagResult = new int[64];

        // Boolean variable that will true if need to increment 'row' value otherwise false
        boolean row_inc = false;

        // Print matrix of lower half zig-zag pattern
        int min = Math.min(m, n);
        for (int len = 1; len <= min; ++len) {
            for (int i = 0; i < len; ++i) {
                zigZagResultString = zigZagResultString+(matrix[row][col]);

                if (i + 1 == len) {
                    break;
                }
                if (!row_inc) {
                    --row;
                    ++col;
                } else {
                    ++row;
                    --col;
                }
            }

            if (len == min) {
                break;
            }
            if (!row_inc) {
                ++col;
                row_inc = true;
            } else {
                ++row;
                row_inc = false;
            }
        }

        if (row == 0) {
            if (col == m - 1) {
                ++row;
            } else {
                ++col;
            }
            row_inc = true;
        } else {
            if (row == n - 1) {
                ++col;
            } else {
                ++row;
            }
            row_inc = false;
        }

        int MAX = Math.max(m, n) - 1;
        for (int len, diag = MAX; diag > 0; --diag) {

            if (diag > min) {
                len = min;
            } else {
                len = diag;
            }

            for (int i = 0; i < len; ++i) {
                zigZagResultString = zigZagResultString+(matrix[row][col]);

                if (i + 1 == len) {
                    break;
                }
                if (row_inc) {
                    ++row;
                    --col;
                } else {
                    ++col;
                    --row;
                }
            }

            if (row == 0 || col == m - 1) {
                if (col == m - 1) {
                    ++row;
                } else {
                    ++col;
                }

                row_inc = true;
            }

            else if (col == 0 || row == n - 1) {
                if (row == n - 1) {
                    ++col;
                } else {
                    ++row;
                }
                row_inc = false;
            }
        }

        String[] nmbStringArr = zigZagResultString.split("");
        for(int i = 0;i < nmbStringArr.length;i++)
        {
            zigZagResult[i] = Integer.parseInt(nmbStringArr[i]);
        }

        return zigZagResult;
    }









// ----------------------------------------------------------------------------------------------------------------------
    public int[][] revertGetQuantiseCoefficients (int[][] quantiseMatrixRGB) {
        int[][] quantiseMatrix =   {{16, 11, 10, 16, 24, 40, 51, 61},
                                    {12, 12, 14, 19, 26, 58, 60, 55},
                                    {14, 13, 16, 24, 40, 57, 69, 56},
                                    {14, 17, 22, 29, 51, 87, 80, 62},
                                    {18, 22, 37, 56, 68, 109, 103, 77},
                                    {24, 35, 55, 64, 81, 104, 113, 92},
                                    {49, 64, 78, 87, 103, 121, 120, 101},
                                    {72, 92, 95, 98, 112, 100, 103, 99}};
        for (int j = 0; j<8; j++) {
            for (int k = 0; k<8; k++) {
                quantiseMatrixRGB[j][k]=quantiseMatrixRGB[j][k]*quantiseMatrix[j][k];
            }
        }

        return quantiseMatrixRGB;
    }

    public int[][] revertGetDCTransformMatrix(int[][] quantiseMatrixRGB) {
        double[][] dctTransformMatrix = new double[m][n];
        double ck, cl, tmpDCTValue, tmpSum;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    tmpSum = 0;
                    for (int i_image = 0; i_image < m; i_image++) {
                        for (int j_image = 0; j_image < n; j_image++) {

                            if (i_image == 0) {
                                ck = 1 / Math.sqrt(m);
                            } else {
                                ck = Math.sqrt(2) / Math.sqrt(m);
                            }
                            if (j_image == 0) {
                                cl = 1 / Math.sqrt(n);
                            } else {
                                cl = Math.sqrt(2) / Math.sqrt(n);
                            }

                            tmpDCTValue = quantiseMatrixRGB[i_image][j_image] *
                                    Math.cos((2 * i_image + 1) * i * pi / (2 * m)) *
                                    Math.cos((2 * j_image + 1) * j * pi / (2 * n));
                            tmpSum = tmpSum + ck*cl*tmpDCTValue;
                        }
                    }
                    dctTransformMatrix[i][j] =  tmpSum;
                }
            }
        return quantiseMatrixRGB;
    }

    public int[][] getMergeRGB (int[][] r, int[][] g, int[][] b) {
        int[][] mergeRGB = new int[r.length][r[1].length];
        for (int i = 0; i<8; i++) {
            for(int j = 0; j<8; j++) {
                mergeRGB[i][j] = r[i][j];
                mergeRGB[i][j] = (mergeRGB[i][j]<<8) + g[i][j];
                mergeRGB[i][j] = (mergeRGB[i][j]<<8) + b[i][j];
            }
        }
        return mergeRGB;
    }

    public BufferedImage getPixelBlock (int[][] mergeRGB) {
        BufferedImage bi = new BufferedImage( 8, 8, BufferedImage.TYPE_INT_RGB );
        final int[] a = ( (DataBufferInt) bi.getRaster().getDataBuffer() ).getData();
        System.arraycopy(mergeRGB, 0, a, 0, mergeRGB.length);
        return bi;
    }

    public BufferedImage joinImages (BufferedImage subImage) {

        
        return null;
    }
}
