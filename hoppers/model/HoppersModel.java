package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
/*
BY Shandon Mith
 */
public class HoppersModel {



    /** the collection of observers of this model */
    private List<Observer<HoppersModel, HoppersClientData>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;

    private ArrayList<ArrayList<Integer>> selectedCords = new ArrayList<>();

    private char[][] pond;

    private static int rows;

    private static int columns;

    private  final static char Empty = '.';
    private final static char Invalid = '*';
    private final static char greenFrog = 'G';
    private final static char redFrog = 'R';

    private String filename;

//  Creates a HopperModel constructor
    public HoppersModel(String filename) throws IOException {
        this.observers = new LinkedList<>();
        this.currentConfig = new HoppersConfig(filename);
        this.pond = currentConfig.getPond();
        rows = HoppersConfig.getRows();
        columns = HoppersConfig.getColumns();
        this.filename = filename;

    }

//    Returns current hoppersConfig
    public HoppersConfig getCurrentConfig() {
        return currentConfig;
    }

//    Returns filename being used
    public String getFilename(){ return filename;}

//    Returns current board
    public char[][] getPond() {
        return pond;
    }

//    Returns current number of Rows
    public int getRows() {
        return rows;
    }
//  Returns current number of columns
    public int getColumns() {
        return columns;
    }

//  Changes the current config to the next step in solving the puzzle
    public void hint() {
        Solver solve = new Solver();
        selectedCords = new ArrayList<>();
        try {
            Configuration newBoard = solve.solve(currentConfig).get(1);
            this.currentConfig = (HoppersConfig) newBoard;
            this.pond = currentConfig.getPond();
            HoppersClientData hoppersClientData = new HoppersClientData("Next Step!");
            alertObservers(hoppersClientData);
        } catch (Exception e) {
            HoppersClientData hoppersClientData = new HoppersClientData("No Solution!");
            alertObservers(hoppersClientData);
        }

    }

//    loads a give file and changes the current config
    public void load(String filename) throws IOException {
        selectedCords = new ArrayList<>();
        HoppersConfig hoppersConfig = new HoppersConfig(filename);
        this.filename = filename;
        this.currentConfig  = hoppersConfig;
        this.pond = hoppersConfig.getPond();
        rows = HoppersConfig.getRows();
        columns = HoppersConfig.getColumns();
        HoppersClientData hoppersClientData = new HoppersClientData("Loaded: " + filename);
        alertObservers(hoppersClientData);
    }

//    reloads the puzzle to its starting point
    public void reset() throws IOException {
        selectedCords = new ArrayList<>();
        this.currentConfig = new HoppersConfig(filename);
        this.pond = currentConfig.getPond();
        HoppersClientData hoppersClientData1 = new HoppersClientData("Loaded: " + filename);
        alertObservers(hoppersClientData1);
        HoppersClientData hoppersClientData2 = new HoppersClientData("Puzzle reset!");
        alertObservers(hoppersClientData2);
    }

//    Determins if the coordinate selected is valid
    private HoppersClientData isValid () {
        ArrayList<Integer> firstCord = this.selectedCords.get(0);
        int firstRow = firstCord.get(0);
        int firstCol = firstCord.get(1);
        if (this.pond[firstRow][firstCol] == redFrog || this.pond[firstRow][firstCol] == greenFrog) {
            return new HoppersClientData("Selected " + "(" + firstRow + ", " + firstCol + ")");
        }
        selectedCords = new ArrayList<>();
        return new HoppersClientData("Invalid selection " + "(" + firstRow + ", " + firstCol + ")" );
    }

//    Moves the redfrog or greenfrog to specified location
    private HoppersClientData makeMove() {
        ArrayList<Integer> firstCord = this.selectedCords.get(0);
        ArrayList<Integer> secondCord = this.selectedCords.get(1);
        int firstRow = firstCord.get(0);
        int firstCol = firstCord.get(1);
        int secondRow = secondCord.get(0);
        int secondCol = secondCord.get(1);
        while (true) {
            if (firstRow % 2 == 0 && firstCol % 2 == 0) {
                try {
                    if (this.pond[firstRow + 2][firstCol] == greenFrog && this.pond[firstRow + 4][firstCol] == Empty) {
                        if (secondRow == firstRow + 4 && secondCol == firstCol) {
                            if (this.pond[firstRow][firstCol] == redFrog) {
                                this.pond[secondRow][secondCol] = redFrog;
                            } else {
                                this.pond[secondRow][secondCol] = greenFrog;
                            }
                            this.pond[firstRow][firstCol] = Empty;
                            this.pond[firstRow + 2][firstCol] = Empty;
                            currentConfig.setNumGreenFrogs(currentConfig.getNumGreenFrogs() - 1);
                            selectedCords = new ArrayList<>();
                            return new HoppersClientData("Jumped from " + "(" + firstRow + ", " + firstCol + ")" + " to " + "( " + secondRow + ", " + secondCol + ")");
                        }
                    }
                } catch (Exception e) {
                }
                try {
                    if (this.pond[firstRow - 2][firstCol] == greenFrog && this.pond[firstRow - 4][firstCol] == Empty) {
                        if (secondRow == firstRow - 4 && secondCol == firstCol) {
                            if (this.pond[firstRow][firstCol] == redFrog) {
                                this.pond[secondRow][secondCol] = redFrog;
                            } else {
                                this.pond[secondRow][secondCol] = greenFrog;
                            }
                            this.pond[firstRow][firstCol] = Empty;
                            this.pond[firstRow - 2][firstCol] = Empty;
                            currentConfig.setNumGreenFrogs(currentConfig.getNumGreenFrogs() - 1);
                            selectedCords = new ArrayList<>();
                            return new HoppersClientData("Jumped from " + "(" + firstRow + ", " + firstCol + ")" + " to " + "( " + secondRow + ", " + secondCol + ")");
                        }

                    }
                } catch (Exception e) {
                }

                try {
                    if (this.pond[firstRow][firstCol + 2] == greenFrog && this.pond[firstRow][firstCol + 4] == Empty) {
                        if (secondRow == firstRow && secondCol == firstCol + 4) {
                            if (this.pond[firstRow][firstCol] == redFrog) {
                                this.pond[secondRow][secondCol] = redFrog;
                            } else {
                                this.pond[secondRow][secondCol] = greenFrog;
                            }
                            this.pond[firstRow][firstCol] = Empty;
                            this.pond[firstRow][firstCol + 2] = Empty;
                            currentConfig.setNumGreenFrogs(currentConfig.getNumGreenFrogs() - 1);
                            selectedCords = new ArrayList<>();
                            return new HoppersClientData("Jumped from " + "(" + firstRow + ", " + firstCol + ")" + " to " + "( " + secondRow + ", " + secondCol + ")");
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (this.pond[firstRow][firstCol - 2] == greenFrog && this.pond[firstRow][firstCol - 4] == Empty) {
                        if (secondRow == firstRow && secondCol == firstCol - 4) {
                            if (this.pond[firstRow][firstCol] == redFrog) {
                                this.pond[secondRow][secondCol] = redFrog;
                            } else {
                                this.pond[secondRow][secondCol] = greenFrog;
                            }
                            this.pond[firstRow][firstCol] = Empty;
                            this.pond[firstRow][firstCol - 2] = Empty;
                            currentConfig.setNumGreenFrogs(currentConfig.getNumGreenFrogs() - 1);
                            selectedCords = new ArrayList<>();
                            return new HoppersClientData("Jumped from " + "(" + firstRow + ", " + firstCol + ")" + " to " + "( " + secondRow + ", " + secondCol + ")");
                        }
                    }
                } catch (Exception e) {
                }
            }

            try {
                if (this.pond[firstRow - 1][firstCol - 1] == greenFrog && this.pond[firstRow - 2][firstCol - 2] == Empty) {
                    if (secondRow == firstRow - 2 && secondCol == firstCol - 2) {
                        if (this.pond[firstRow][firstCol] == redFrog) {
                            this.pond[secondRow][secondCol] = redFrog;
                        } else {
                            this.pond[secondRow][secondCol] = greenFrog;
                        }
                        this.pond[firstRow][firstCol] = Empty;
                        this.pond[firstRow - 1][firstCol - 1] = Empty;
                        currentConfig.setNumGreenFrogs(currentConfig.getNumGreenFrogs() - 1);
                        selectedCords = new ArrayList<>();
                        return new HoppersClientData("Jumped from " + "(" + firstRow + ", " + firstCol + ")" + " to " + "( " + secondRow + ", " + secondCol + ")");
                    }

                }
            } catch (Exception e) {
            }

            try {
                if (this.pond[firstRow - 1][firstCol + 1] == greenFrog && this.pond[firstRow - 2][firstCol + 2] == Empty) {
                    if (secondRow == firstRow - 2 && secondCol == firstCol + 2) {
                        if (this.pond[firstRow][firstCol] == redFrog) {
                            this.pond[secondRow][secondCol] = redFrog;
                        } else {
                            this.pond[secondRow][secondCol] = greenFrog;
                        }
                        this.pond[firstRow][firstCol] = Empty;
                        this.pond[firstRow - 1][firstCol + 1] = Empty;
                        currentConfig.setNumGreenFrogs(currentConfig.getNumGreenFrogs() - 1);
                        selectedCords = new ArrayList<>();
                        return new HoppersClientData("Jumped from " + "(" + firstRow + ", " + firstCol + ")" + " to " + "( " + secondRow + ", " + secondCol + ")");
                    }
                }
            } catch (Exception e) {
            }

            try {
                if (this.pond[firstRow + 1][firstCol + 1] == greenFrog && this.pond[firstRow + 2][firstCol + 2] == Empty) {
                    if (secondRow == firstRow + 2 && secondCol == firstCol + 2) {
                        if (this.pond[firstRow][firstCol] == redFrog) {
                            this.pond[secondRow][secondCol] = redFrog;
                        } else {
                            this.pond[secondRow][secondCol] = greenFrog;
                        }
                        this.pond[firstRow][firstCol] = Empty;
                        this.pond[firstRow + 1][firstCol + 1] = Empty;
                        currentConfig.setNumGreenFrogs(currentConfig.getNumGreenFrogs() - 1);
                        selectedCords = new ArrayList<>();
                        return new HoppersClientData("Jumped from " + "(" + firstRow + ", " + firstCol + ")" + " to " + "( " + secondRow + ", " + secondCol + ")");
                    }

                }
            } catch (Exception e) {
            }

            try {
                if (this.pond[firstRow + 1][firstCol - 1] == greenFrog && this.pond[firstRow + 2][firstCol - 2] == Empty) {
                    if (secondRow == firstRow + 2 && secondCol == firstCol - 2) {
                        if (this.pond[firstRow][firstCol] == redFrog) {
                            this.pond[secondRow][secondCol] = redFrog;
                        } else {
                            this.pond[secondRow][secondCol] = greenFrog;
                        }
                        this.pond[firstRow][firstCol] = Empty;
                        this.pond[firstRow + 1][firstCol - 1] = Empty;
                        currentConfig.setNumGreenFrogs(currentConfig.getNumGreenFrogs() - 1);
                        selectedCords = new ArrayList<>();
                        return new HoppersClientData("Jumped from " + "(" + firstRow + ", " + firstCol + ")" + " to " + "( " + secondRow + ", " + secondCol + ")");
                    }
                }
            } catch (Exception e) {
            }
            selectedCords = new ArrayList<>();
            break;
        }
        return new HoppersClientData("Can't jump from " +"(" + firstRow + ", " + firstCol + ")" + " to " + "( " + secondRow + ", " + secondCol + ")");
    }

//   gets selected cords and determines if a move can be made
    public void select(ArrayList<Integer> cords) {
        if ((0 <= cords.get(0) && cords.get(0) < rows) && (0 <= cords.get(1) && cords.get(1) < columns)) {
            HoppersClientData hoppersClientData = null;
            switch (selectedCords.size()) {
                case 0 -> {
                    selectedCords.add(cords);
                    hoppersClientData = isValid();
                }
                case 1 -> {
                    selectedCords.add(cords);
                    hoppersClientData = makeMove();
                }
                default -> throw
                        new RuntimeException("Internal Error: selectedCards too big.");
            }
            alertObservers(hoppersClientData);
        }
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */

//    add an observer
    public void addObserver(Observer<HoppersModel, HoppersClientData> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */

//    alerts observers
    private void alertObservers(HoppersClientData data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
