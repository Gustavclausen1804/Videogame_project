import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.HostServices;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

class MapGeneration {
    // Group mapRoot = new Group();

    static int boxSize = 40;
    static int buildColorsAmount = 4; // Different colors of houses
    static int buildAmount = 4; // amount of pictures pr color

    // Creates two arraylists which are used for the generation of houses
    ArrayList<int[]> housesStart = new ArrayList<int[]>();
    static ArrayList<ArrayList<int[]>> houses = new ArrayList<>();
    Image[][] houseImageArray = new Image[buildColorsAmount][buildAmount];
    Image houseImage;

    MapGeneration() {

        // Generere tilfældige tal til kortet.
        Random ran = new Random();
        int max_random_house = (App.width / boxSize); // Max houses on the map.

        int width_of_house = 0; // width of the house.
        int x_coordinate_in_loop = 0;
        for (int i = 0; i < max_random_house; i++) {
            // generating integer
            x_coordinate_in_loop = x_coordinate_in_loop + width_of_house; // top left X_coordinate of the house.
            int y_coordinate = ran.nextInt((App.height / boxSize) - ((App.height / boxSize) / 5)); // top left
                                                                                                   // y_coordnat.
                                                                                                   // Integer Given in
                                                                                                   // Blocks
                                                                                                   // from
                                                                                                   // the bottom
                                                                                                   // of the
                                                                                                   // stage.

            int minHeight = (App.height / boxSize) / 2; // Integer given in blocks from the top of the stage.
            if (y_coordinate < minHeight) {
                y_coordinate = minHeight + ran.nextInt(minHeight - 2);
            }

            int maxWidth = 5;
            int minWidth = 3;
            int random = ran.nextInt(maxWidth);
            if (random < minWidth) {
                random = minWidth;
            }
            width_of_house = random;
            int colorID = ran.nextInt(buildColorsAmount);
            housesStart.add(new int[4]); // adds an array with 4 spaces to the houses start arraylist
            housesStart.get(i)[0] = x_coordinate_in_loop; // represents the x coordinate for the top left of the house
            housesStart.get(i)[1] = y_coordinate; // represents the y coordinate for the top left of the house
            housesStart.get(i)[2] = width_of_house; // represents the width of the house (number of blocks)
            housesStart.get(i)[3] = colorID;
            int antalBlocks = 0;
            for (int j = 0; j < housesStart.size(); j++) {
                antalBlocks += housesStart.get(j)[2];
            }
            if (antalBlocks >= max_random_house) {
                i = max_random_house + 1;
            }
        }

        GenerateHouse();
    }

    // Metode som generer de specifikek Blokke som Husene skal består af,
    // baseret på x-koordinat, y-koordinat, ønsket bredde og farve
    public void GenerateHouse() {

        Random ran = new Random();
        for (int k = 0; k < housesStart.size(); k++) {
            // creates variables so it is easier to keep track of code
            int x_coordinate = housesStart.get(k)[0];
            int y_coordinate = housesStart.get(k)[1];
            int width = housesStart.get(k)[2];
            int colorID = housesStart.get(k)[3];

            int max_height_of_house = (App.height / boxSize);

            for (int i = 0; i < width; i++) {
                houses.add(new ArrayList<>()); // adds an arraylist to the which represents each column of the house
                for (int j = 0; j < max_height_of_house; j++) {
                    int imageID = ran.nextInt(buildAmount);

                    // Adds an array with 4 spaces which holds the x, y, colorID and imageID of each
                    // block.
                    houses.get(i).add(
                            new int[] { (x_coordinate + i) * boxSize, (y_coordinate + j) * boxSize, colorID, imageID });
                }
            }
        }
        for (int i = 0; i < buildColorsAmount; i++) {
            for (int j = 0; j < buildAmount; j++) {
                houseImageArray[i][j] = new Image("/buildings/" + i + "" + j + ".png");
            }
        }
    }

    public void drawMap(GraphicsContext gc) {

        gc.drawImage(App.backGroundImage, 0, 0);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);

        for (int i = 0; i < houses.size(); i++) {

            for (int j = 0; j < houses.get(i).size(); j++) {
                houseImage = houseImageArray[houses.get(i).get(j)[2]][houses.get(i).get(j)[3]];

                gc.drawImage(houseImage, houses.get(i).get(j)[0], houses.get(i).get(j)[1]);
                gc.strokeRect(houses.get(i).get(j)[0], houses.get(i).get(j)[1], boxSize, boxSize);

            }
        }
        mapMove();
    }

    void mapMove() {
        if (App.frameCount % 20 == 0) {
            for (int i = 0; i < houses.size(); i++) {
                for (int j = 0; j < houses.get(i).size(); j++) {
                    if (houses.get(i).get(j)[1] + boxSize < App.height) {
                        if (houses.get(i).get(j)[1] + boxSize != houses.get(i).get(j + 1)[1]) {
                            houses.get(i).get(j)[1] += boxSize;
                        }
                    }
                }
            }
        }
    }

}
