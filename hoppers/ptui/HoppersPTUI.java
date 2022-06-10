package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HoppersPTUI implements Observer<HoppersModel, HoppersClientData> {
    private HoppersModel model;
    private boolean firstLoad = true;

//    Initialize ptui
    public HoppersPTUI(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        initializeView(filename);
    }

//    Run the ptui
    private void run() throws IOException {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> ");
            String line = in.nextLine();
            String[] words = line.split("\\s");
            if (words.length > 0) {

                if ( words[0].startsWith("h")) {
                    if(model.getCurrentConfig().isSolution()) {
                        HoppersClientData hoppersClientData = new HoppersClientData("Already Solved!");
                        displayBoard(hoppersClientData, model.getRows(), model.getColumns(), model.getPond());
                    } else {
                        this.model.hint();
                    }
                } else if ( words[0].startsWith("l")) {
                    try {
                        this.model.load(words[1]);
                    } catch (IOException e) {
                        HoppersClientData hoppersClientData = new HoppersClientData("Failed to load: " + words[1]);
                        update(this.model, hoppersClientData);
                    }
                } else if ( words[0].startsWith("s")) {
                    if(model.getCurrentConfig().isSolution()) {
                        HoppersClientData hoppersClientData = new HoppersClientData("Already Solved!");
                        displayBoard(hoppersClientData, model.getRows(), model.getColumns(), model.getPond());
                    } else {
                        ArrayList<Integer> cords = new ArrayList<>();
                        cords.add(Integer.parseInt(words[1]));
                        cords.add(Integer.parseInt(words[2]));
                        this.model.select(cords);
                    }
                } else if ( words[0].startsWith("q")) {
                    break;
                } else if (words[0].startsWith("r")) {
                    this.model.reset();
                } else {
                    displayHelp();
                }
            }
        }
    }

//    Initialize view
    public void initializeView(String filename) {
        this.model.addObserver(this);
        HoppersClientData hoppersClientData = new HoppersClientData("Loaded: " + filename);
        update(this.model, hoppersClientData);
    }

//    diaplay board
    private void displayBoard(HoppersClientData data, int rows, int columns, char[][] pond) {
        if (data != null){
            System.out.println(data);
        }
        System.out.print("  ");
        for (int i=0; i < columns; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        System.out.print("  ");
        for (int i = 0; i < (columns + rows); ++i) {
            System.out.print("-");
        }
        System.out.println();
        for (int row=0; row < rows; ++row) {
            System.out.print(row + "|");
            for (int col=0; col < columns; ++col) {
                System.out.print(" "+ pond[row][col]);
            }
            System.out.println();
        }
        System.out.println();
        if (this.firstLoad) {
            displayHelp();
        }
        this.firstLoad = false;

    }


//    diaplays help commands
    private static void displayHelp() {
        System.out.println("h(int)    -- hint next move");
        System.out.println("l(oad)    -- load new puzzle file");
        System.out.println("s(elect)  -- select cell at r, c");
        System.out.println("q(uit)    -- quit the game");
        System.out.println("r(eset)   -- reset the current game");
    }

//    updates board and displays it
    @Override
    public void update(HoppersModel model, HoppersClientData data) {
        displayBoard(data, model.getRows(), model.getColumns(), model.getPond());
    }

//    runs program
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            HoppersPTUI ptui = new HoppersPTUI(args[0]);
            ptui.run();
        }

    }
}
