package puzzles.hoppers.model;
/*
BY Shandon Mith
 */
// TODO: implement your HoppersConfig for the common solver

import puzzles.common.solver.Configuration;
import puzzles.hoppers.solver.Hoppers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class HoppersConfig implements Configuration {

    private  final static char Empty = '.';
    private final static char Invalid = '*';
    private final static char greenFrog = 'G';
    private final static char redFrog = 'R';

    private int numGreenFrogs;
    private static int columns;
    private static int rows;
    private final char[][] pond;

//    Creates HoppersConfig constuctor by reading filename
    public HoppersConfig(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String[] firstLine = in.readLine().split("\\s");
        rows = Integer.parseInt(firstLine[0]);
        columns= Integer.parseInt(firstLine[1]);
        this.pond = new char[rows][columns];
        for (int row=0; row < rows; ++row) {
            String[] pond = in.readLine().split("\\s");
            for (int col=0; col < columns; ++col) {
                this.pond[row][col] = pond[col].charAt(0);
            }
        }
        in.close();
        int frogs = 0;
        for (int row=0; row < rows; ++row) {
            for (int col=0; col < columns; ++col) {
                if (this.pond[row][col] == greenFrog) {
                    frogs += 1;
                }
            }
        }
        this.numGreenFrogs = frogs;
    }

//    Creates a copy of the HoppersConfig constructor
    public HoppersConfig(char[][] currentPond, int numGreenFrog) {
        this.pond = currentPond;
        this.numGreenFrogs=numGreenFrog;
    }

//    returns current board
    public char[][] getPond() {
        return pond;
    }

//    returns current number of rows
    public static int getRows() {
        return rows;
    }

//    returns current number of columns
    public static int getColumns() {
        return columns;
    }

//    returns number of green frogs on the board
    public int getNumGreenFrogs() {
        return numGreenFrogs;
    }
//  changes the number of green frogs on the board
    public void setNumGreenFrogs(int numGreenFrogs) {
        this.numGreenFrogs = numGreenFrogs;
    }
// presents hopperConfig as a string
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r=0; r < rows; ++r) {
            for (int c=0; c < columns; ++c) {
                sb.append(this.pond[r][c]);
                sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
// makes copy of the current board
    public char[][] copyOfPond(char[][] pond) {
        char [][] copy = new char[pond.length][];
        for(int i = 0; i < pond.length; i++) {
            copy[i] = pond[i].clone();
        }
        return copy;
    }

//    gets all possible moves for the current puzzle
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        for (int row=0; row < rows; ++row) {
            for (int col=0; col < columns; ++col) {
                if (this.pond[row][col] == greenFrog || this.pond[row][col] == redFrog) {
                    if (row%2==0 && col%2==0) {
                        try {
                            if (this.pond[row+2][col] == greenFrog && this.pond[row+4][col] == Empty) {
                                char[][] newPond = copyOfPond(pond);
                                int numGreenFrogs = 0;
                                newPond[row][col] = Empty;
                                newPond[row+2][col] = Empty;
                                numGreenFrogs = this.numGreenFrogs - 1;
                                if (this.pond[row][col] == redFrog) {
                                    newPond[row+4][col] = redFrog;
                                } else {
                                    newPond[row+4][col] = greenFrog;

                                }
                                HoppersConfig hoppers = new HoppersConfig(newPond, numGreenFrogs);
                                neighbors.add(hoppers);
                            }

                        } catch (Exception e) {
                        }

                        try {
                            if (this.pond[row-2][col] == greenFrog && this.pond[row-4][col] == Empty) {
                                char[][] newPond = copyOfPond(pond);
                                int numGreenFrogs = 0;
                                newPond[row][col] = Empty;
                                newPond[row-2][col] = Empty;
                                numGreenFrogs = this.numGreenFrogs - 1;
                                if (this.pond[row][col] == redFrog) {
                                    newPond[row-4][col] = redFrog;
                                } else {
                                    newPond[row-4][col] = greenFrog;
                                }
                                HoppersConfig hoppers = new HoppersConfig(newPond, numGreenFrogs);
                                neighbors.add(hoppers);
                            }
                        } catch (Exception e) {
                        }

                        try {
                            if (this.pond[row][col+2] == greenFrog && this.pond[row][col+4] == Empty) {
                                char[][] newPond = copyOfPond(pond);
                                int numGreenFrogs = 0;
                                newPond[row][col] = Empty;
                                newPond[row][col+2] = Empty;
                                numGreenFrogs = this.numGreenFrogs - 1;
                                if (this.pond[row][col] == redFrog) {
                                    newPond[row][col+4] = redFrog;
                                } else {
                                    newPond[row][col+4] = greenFrog;
                                }
                                HoppersConfig hoppers = new HoppersConfig(newPond, numGreenFrogs);
                                neighbors.add(hoppers);
                            }
                        } catch (Exception e) {
                        }

                        try {
                            if (this.pond[row][col-2] == greenFrog && this.pond[row][col-4] == Empty) {
                                char[][] newPond = copyOfPond(pond);
                                int numGreenFrogs = 0;
                                newPond[row][col] = Empty;
                                newPond[row][col-2] = Empty;
                                numGreenFrogs = this.numGreenFrogs - 1;
                                if (this.pond[row][col] == redFrog) {
                                    newPond[row][col-4] = redFrog;
                                } else {
                                    newPond[row][col-4] = greenFrog;
                                }
                                HoppersConfig hoppers = new HoppersConfig(newPond, numGreenFrogs);
                                neighbors.add(hoppers);
                            }
                        } catch (Exception e) {
                        }
                    }


                    try {
                        if (this.pond[row-1][col-1] == greenFrog && this.pond[row-2][col-2] == Empty) {
                            char[][] newPond = copyOfPond(pond);
                            int numGreenFrogs=0;
                            newPond[row][col] = Empty;
                            newPond[row-1][col-1] = Empty;
                            numGreenFrogs = this.numGreenFrogs - 1;
                            if (this.pond[row][col] == redFrog) {
                                newPond[row-2][col-2] = redFrog;
                            } else {
                                newPond[row-2][col-2] = greenFrog;
                            }
                            HoppersConfig hoppers = new HoppersConfig(newPond, numGreenFrogs);
                            neighbors.add(hoppers);
                        }
                    } catch (Exception e){
                    }

                    try {
                        if (this.pond[row-1][col+1] == greenFrog && this.pond[row-2][col+2] == Empty) {
                            char[][] newPond = copyOfPond(pond);
                            int numGreenFrogs=0;
                            newPond[row][col] = Empty;
                            newPond[row-1][col+1] = Empty;
                            numGreenFrogs = this.numGreenFrogs - 1;
                            if (this.pond[row][col] == redFrog) {
                                newPond[row-2][col+2] = redFrog;
                            } else {
                                newPond[row-2][col+2] = greenFrog;
                            }
                            HoppersConfig hoppers = new HoppersConfig(newPond, numGreenFrogs);
                            neighbors.add(hoppers);
                        }
                    } catch (Exception e){
                    }

                    try {
                        if (this.pond[row+1][col+1] == greenFrog && this.pond[row+2][col+2] == Empty) {
                            char[][] newPond = copyOfPond(pond);
                            int numGreenFrogs = 0;
                            newPond[row][col] = Empty;
                            newPond[row+1][col+1] = Empty;
                            numGreenFrogs = this.numGreenFrogs - 1;
                            if (this.pond[row][col] == redFrog) {
                                newPond[row+2][col+2] = redFrog;
                            } else {
                                newPond[row+2][col+2] = greenFrog;
                            }
                            HoppersConfig hoppers = new HoppersConfig(newPond, numGreenFrogs);
                            neighbors.add(hoppers);
                        }
                    } catch (Exception e) {
                    }

                    try {
                        if (this.pond[row+1][col-1] == greenFrog && this.pond[row+2][col-2] == Empty) {
                            char[][] newPond = copyOfPond(pond);
                            int numGreenFrogs = 0;
                            newPond[row][col] = Empty;
                            newPond[row+1][col-1] = Empty;
                            numGreenFrogs = this.numGreenFrogs - 1;
                            if (this.pond[row][col]==redFrog) {
                                newPond[row+2][col-2] = redFrog;
                            }
                            else{
                                newPond[row+2][col-2] = greenFrog;
                            }
                            HoppersConfig hoppers = new HoppersConfig(newPond, numGreenFrogs);
                            neighbors.add(hoppers);
                        }
                    } catch (Exception e){
                    }
                }
            }
        }
        return neighbors;
    }

//    compares two 2D array objects
    @Override
    public boolean equals(Object obj) {
        char[][] pond1 = new char[rows][columns];
        char[][] pond2 = new char[rows][columns];
        if (obj instanceof HoppersConfig) {
            pond1 = this.pond;
            pond2 = ((HoppersConfig) obj).pond;
            return Arrays.deepEquals(pond1,pond2);
        }
        return false;
    }

//    creats a hashcode for a 2d array
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(pond);
    }


//  Determines if current config is the solution
    @Override
    public boolean isSolution() {
        return numGreenFrogs == 0;
    }
}
