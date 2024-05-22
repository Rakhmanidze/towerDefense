package cz.cvut.fel.pjv.semak_pjv.helperMethods;

import cz.cvut.fel.pjv.semak_pjv.objects.PathCoordinate;
import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages downloading, saving, and accessing game resources and level data.
 */
public class DownloadSave {
    private static final Logger LOGGER = Logger.getLogger(DownloadSave.class.getName());

    /**
     * Retrieves an image resource.
     *
     * @param imageName The name of the image file.
     * @return The image resource.
     */
    public static Image getImage(String imageName) {
        Image image = null;
        InputStream imagePath = DownloadSave.class.getClassLoader().getResourceAsStream(imageName);

        if (imagePath == null) {
            LOGGER.log(Level.SEVERE, "Image not found: " + imageName);
            return null;
        }
        try {
            image = new Image(imagePath);
            LOGGER.info("Successfully loaded image: " + imageName);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Error loading image: " + imageName, e);
        }
        return image;
    }

    /**
     * Creates a new level file with the specified file name and array of IDs.
     *
     * @param fileName   The name of the level file.
     * @param arrayOfId  The array of IDs representing the level.
     */
    public static void createLevel(String fileName, int[] arrayOfId) {
        File newLevel = new File("src/main/resources/" + fileName + ".txt");

        if (newLevel.exists()) {
            LOGGER.info("File " + fileName + " exists");
        } else {
            try {
                boolean fileCreated = newLevel.createNewFile();
                if (fileCreated) {
                    writeToFile(newLevel, arrayOfId, new PathCoordinate(0, 0), new PathCoordinate(0, 0));
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to create the file: " + fileName);
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error creating file: " + fileName, e);
            }
        }
    }

    /**
     * Writes data to a file, including IDs, start and end path coordinates.
     *
     * @param file   The file to write data to.
     * @param arrayOfId  The array of IDs.
     * @param start  The starting path coordinate.
     * @param end    The ending path coordinate.
     */
    private static void writeToFile(File file, int[] arrayOfId, PathCoordinate start, PathCoordinate end) {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (int i : arrayOfId)
                printWriter.println(i);

            printWriter.println(start.getxCoord());
            printWriter.println(start.getyCoord());
            printWriter.println(end.getxCoord());
            printWriter.println(end.getyCoord());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to file: " + file.getAbsolutePath(), e);
        }
    }

    /**
     * Saves a level with the specified file name, 2D array of IDs, and start and end path coordinates.
     *
     * @param fileName      The name of the level file.
     * @param arrayOfId     The 2D array of IDs representing the level.
     * @param start         The starting path coordinate.
     * @param end           The ending path coordinate.
     */
    public static void saveLevel(String fileName, int[][] arrayOfId, PathCoordinate start, PathCoordinate end) {
        File newLevel = new File("src/main/resources/" + fileName + ".txt");
        if (newLevel.exists())
            writeToFile(newLevel, LevelNavigator.convert2DarrayTo1Darray(arrayOfId), start, end);
        else
            LOGGER.info("File " + fileName + " doesn't exist");
    }

    /**
     * Reads data from a file and returns an ArrayList of integers.
     *
     * @param levelfile The file to read data from.
     * @return An ArrayList of integers.
     */
    private static ArrayList<Integer> readFromFile(File levelfile) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(levelfile);
            while (scanner.hasNextLine())
                arrayList.add(Integer.parseInt(scanner.nextLine()));

            scanner.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading from file: " + levelfile.getAbsolutePath(), e);
        }

        return arrayList;
    }


    /**
     * Retrieves the start and end path coordinates from a level file.
     *
     * @param filename The name of the level file.
     * @return An ArrayList of path coordinates.
     */
    public static ArrayList<PathCoordinate> getLevelPathCoordinate(String filename) {
        File levelFile = new File("src/main/resources/" + filename + ".txt");

        if (levelFile.exists()) {
            ArrayList<Integer> arrayList = readFromFile(levelFile);
            ArrayList<PathCoordinate> coordinates = new ArrayList<>();
            coordinates.add(new PathCoordinate(arrayList.get(400), arrayList.get(401)));
            coordinates.add(new PathCoordinate(arrayList.get(402), arrayList.get(403)));
            return coordinates;
        } else {
            LOGGER.info("File " + filename + " doesn't exist");
            return null;
        }
    }

    /**
     * Retrieves level data from a file and returns a 2D array of integers.
     *
     * @param filename The name of the level file.
     * @return A 2D array of integers representing level data.
     */
    public static int[][] getLevelData(String filename) {
        File levelFile = new File("src/main/resources/" + filename + ".txt");

        if (levelFile.exists()) {
            ArrayList<Integer> arrayList = readFromFile(levelFile);
            return LevelNavigator.convertArrayListTo2DIntArray(arrayList, 20, 20);
        } else {
            LOGGER.info("File " + filename + " doesn't exist");
            return null;
        }

    }
}
