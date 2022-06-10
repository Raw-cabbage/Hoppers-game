package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Window;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
/*
BY Shandon Mith
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, HoppersClientData> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";


    private HoppersModel model;
    // for demonstration purposes
    private final Image green_frog = new Image((getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png")));
    private final Image lily_pad = new Image((getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png")));
    private final Image red_frog = new Image((getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png")));
    private final Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));

    private  final static char Empty = '.';
    private final static char Invalid = '*';
    private final static char greenFrog = 'G';
    private final static char redFrog = 'R';

    Label message = new Label();
    BorderPane borderPane = new BorderPane();
    GridPane gridPane = new GridPane();
    Button load = new Button("Load");
    Button reset = new Button("Reset");
    Button hint = new Button("Hint");
    private Stage stage;



//  Initalize gui
    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
    }

//    show the gui
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        VBox vBox = new VBox();
        char[][] pond = model.getPond();
        message.setText("Loaded: " + model.getFilename().substring(13));
        message.setFont(new Font("Arial",  18));
        message.setTextFill(Paint.valueOf("Black"));
        vBox.getChildren().add(message);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setTop(vBox);
        for (int row=0; row < model.getRows(); ++row) {
            for (int col =0; col < model.getColumns(); ++col) {
                if (pond[row][col] == redFrog) {
                    ImageView view = new ImageView(red_frog);
                    view.setPreserveRatio(true);
                    view.setFitHeight(60);
                    view.setFitWidth(60);
                    view.setId("" + row + " " + col + "");
                    view.setOnMouseClicked(mouseEvent -> select(view.getId()));
                    gridPane.add(view, col, row);
                } else if (pond[row][col] == greenFrog) {
                    ImageView view = new ImageView(green_frog);
                    view.setPreserveRatio(true);
                    view.setFitHeight(60);
                    view.setFitWidth(60);
                    view.setId("" + row + " " + col + "");
                    view.setOnMouseClicked(mouseEvent -> select(view.getId()));
                    gridPane.add(view, col, row);
                } else if (pond[row][col] == Empty) {
                    ImageView view = new ImageView(lily_pad);
                    view.setPreserveRatio(true);
                    view.setFitHeight(60);
                    view.setFitWidth(60);
                    view.setId("" + row + " " + col + "");
                    view.setOnMouseClicked(mouseEvent -> select(view.getId()));
                    gridPane.add(view, col, row);
                } else if (pond[row][col] == Invalid) {
                    ImageView view = new ImageView(water);
                    view.setPreserveRatio(true);
                    view.setFitHeight(60);
                    view.setFitWidth(60);
                    view.setId("" + row + " " + col + "");
                    view.setOnMouseClicked(mouseEvent -> select(view.getId()));
                    gridPane.add(view, col, row);
                }

            }
        }
        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);
        HBox hBox = new HBox(region1, load, reset, hint, region2);
        borderPane.setBottom(hBox);
        load.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("data/hoppers"));
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                try {
                    this.model.load("data/hoppers/"+selectedFile.getName());
                } catch (IOException e) {
                    HoppersClientData hoppersClientData = new HoppersClientData("Failed to load: " + selectedFile.getName());
                    update(this.model, hoppersClientData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        reset.setOnAction(event -> {
            try {
                this.model.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        hint.setOnAction(event -> {
            if (model.getCurrentConfig().isSolution()) {
                HoppersClientData hoppersClientData = new HoppersClientData("Already Solved!");
                displayBoard(hoppersClientData, model.getRows(), model.getColumns(), model.getPond());
            } else{
                this.model.hint();
            }

        });
        Scene scene = new Scene(borderPane);
        this.stage.setScene(scene);
        this.stage.setTitle("Hoppers GUI");
        this.stage.show();

    }

//   Select the piece on a more and check if its a valid move
//    para: cordinates of the piece selected

    private void select(String cords){
        if (model.getCurrentConfig().isSolution()) {
            HoppersClientData hoppersClientData = new HoppersClientData("Already Solved!");
            displayBoard(hoppersClientData, model.getRows(), model.getColumns(), model.getPond());
        } else {
            ArrayList<Integer> cordsInt= new ArrayList<>();
            String[] cordString = cords.split("\\s");
            for (String s: cordString) {
                cordsInt.add(Integer.parseInt(s));
            }
            this.model.select(cordsInt);
        }
    }

//    makes a new board for any changes made
    private void displayBoard(HoppersClientData data, int rows, int columns, char[][] pond) {
        if (data != null) {
            message.setText(data.toString());
            if (data.toString().startsWith("Jumped") || data.toString().startsWith("Next")) {
                Media sound = new Media(new File("src/puzzles/hoppers/gui/resources/maro-jump-sound-effect_1.mp3").toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
            }
        }
        GridPane newGrid = new GridPane();
        for (int row=0; row < rows; ++row) {
            for (int col =0; col < columns; ++col) {
                if (pond[row][col] == redFrog) {
                    ImageView view = new ImageView(red_frog);
                    view.setPreserveRatio(true);
                    view.setFitHeight(60);
                    view.setFitWidth(60);
                    view.setId("" + row + " " + col + "");
                    view.setOnMouseClicked(mouseEvent -> select(view.getId()));
                    newGrid.add(view, col, row);
                } else if (pond[row][col] == greenFrog) {
                    ImageView view = new ImageView(green_frog);
                    view.setPreserveRatio(true);
                    view.setFitHeight(60);
                    view.setFitWidth(60);
                    view.setId("" + row + " " + col + "");
                    view.setOnMouseClicked(mouseEvent -> select(view.getId()));
                    newGrid.add(view, col, row);
                } else if (pond[row][col] == Empty) {
                    ImageView view = new ImageView(lily_pad);
                    view.setPreserveRatio(true);
                    view.setFitHeight(60);
                    view.setFitWidth(60);
                    view.setId("" + row + " " + col + "");
                    view.setOnMouseClicked(mouseEvent -> select(view.getId()));
                    newGrid.add(view, col, row);
                } else if (pond[row][col] == Invalid) {
                    ImageView view = new ImageView(water);
                    view.setPreserveRatio(true);
                    view.setFitHeight(60);
                    view.setFitWidth(60);
                    view.setId("" + row + " " + col + "");
                    view.setOnMouseClicked(mouseEvent -> select(view.getId()));
                    newGrid.add(view, col, row);
                }
            }
        }
        newGrid.setAlignment(Pos.CENTER);
        borderPane.setCenter(newGrid);
        stage.setHeight(60 * rows + 82);
        stage.setWidth(60 * columns + 18);
    }


//    update the board
    @Override
    public void update(HoppersModel hoppersModel, HoppersClientData hoppersClientData) {
        this.model = hoppersModel;
        displayBoard(hoppersClientData, model.getRows(), model.getColumns(), model.getPond());
    }

//    run program
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
