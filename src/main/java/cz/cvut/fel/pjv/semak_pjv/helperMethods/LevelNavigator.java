package cz.cvut.fel.pjv.semak_pjv.helperMethods;

import java.util.ArrayList;

/**
 * Utility class for navigating levels and performing array conversions.
 * It provides methods for converting between ArrayLists and 2D integer arrays,
 * as well as calculating distances between points.
 */
public class LevelNavigator {
    /**
     * Converts an ArrayList to a 2D integer array with specified row and column counts.
     *
     * @param arrayList   The ArrayList to convert.
     * @param rowCount    The number of rows in the resulting 2D array.
     * @param columnCount The number of columns in the resulting 2D array.
     * @return The converted 2D integer array.
     */
    public static int[][] convertArrayListTo2DIntArray (ArrayList<Integer> arrayList, int rowCount, int columnCount) {
        int[][] newArrayList = new int[rowCount][columnCount];

        for (int i = 0; i < newArrayList.length; i++)
            for (int j = 0; j < newArrayList[i].length; j++) {
                int index = i * rowCount + j;
                newArrayList[i][j] = arrayList.get(index);
            }

        return newArrayList;
    }

    /**
     * Converts a 2D integer array to a 1D integer array.
     *
     * @param twoDarray The 2D array to convert.
     * @return The converted 1D integer array.
     */
    public static int[] convert2DarrayTo1Darray(int[][] twoDarray) {
        int[] oneDarray = new int[twoDarray.length * twoDarray[0].length];

        for (int i = 0; i < twoDarray.length; i++)
            for (int j = 0; j < twoDarray[i].length; j++) {
                int index = i *  twoDarray.length + j;
                oneDarray[index] = twoDarray[i][j];
            }

        return oneDarray;
    }

    /**
     * Calculates the distance between two points.
     *
     * @param x1 The x-coordinate of the first point.
     * @param y1 The y-coordinate of the first point.
     * @param x2 The x-coordinate of the second point.
     * @param y2 The y-coordinate of the second point.
     * @return The distance between the two points.
     */
    public static int getDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        float xDiff = Math.abs(x1 - x2); // Math.abs gives only positive diff between numbers
        float yDiff = Math.abs(y1 - y2);

        return (int) Math.hypot(xDiff, yDiff); //hypotenuse
    }
}
