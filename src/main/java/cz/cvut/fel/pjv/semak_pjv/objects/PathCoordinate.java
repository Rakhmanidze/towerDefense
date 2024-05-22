package cz.cvut.fel.pjv.semak_pjv.objects;

/**
 * Represents a coordinate in the path.
 */
public class PathCoordinate {
    private int xCoord, yCoord;

    /**
     * Constructs a PathCoordinate object with the specified coordinates.
     *
     * @param xCoord  the x-coordinate of the path coordinate
     * @param yCoord  the y-coordinate of the path coordinate
     */
    public PathCoordinate(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

//    Getters and setters
    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }
}
