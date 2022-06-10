package puzzles.hoppers.solver;

//import puzzles.common.solver.Solver;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.common.solver.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *  (
 *  )\ )                            )      )
 * (()/( (         (     (       ( /(   ( /(
 *  /(_)))(    (   )\   ))\  (   )\())  )(_))
 * (_)) (()\   )\ ((_) /((_) )\ (_))/  ((_)
 * | _ \ ((_) ((_)  ! (_))  ((_)| |_   |_  )
 * |  _)| '_|/ _ \ | |/ -_)/ _| |  _|   / /
 * |(|/(|_|  \___/_/ |\___|\__|  \__|  /___|
 *  )\())        |__/          (   (
 * ((_)\   (   `  )   `  )    ))\  )(   (
 *  _((_)  )\  /(/(   /(/(   /((_)(()\  )\
 * | || | ((_)((_)_\ ((_)_\ (_))   ((_)((_)
 * | __ |/ _ \| '_ \)| '_ \)/ -_) | '_|(_-<
 * |_||_|\___/| .__/ | .__/ \___| |_|  /__/
 *            |_|    |_|
 */
/*
BY Shandon Mith
 */


// Initialize hoppers puzzzle and prints solutions
public class Hoppers {
    public static void main(String[] args) throws IOException {
        Solver solve = new Solver();
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        } else {
            HoppersConfig hoppersConfig = new HoppersConfig(args[0]);
            System.out.println("File: " +args[0]);
            System.out.println(hoppersConfig);
            ArrayList<Configuration> path = solve.solve(hoppersConfig);
            System.out.println("Total configs: " + solve.getTotalConfigs());
            System.out.println("Unique configs: " + solve.getUniqueConfigs());
            if (path.isEmpty()) {
                System.out.println("No solution");
            } else {
                int step = 0;
                for (Configuration i : path) {
                    System.out.println("Step " + step + ":");
                    System.out.println(i.toString());
                    step += 1;
                }
            }
        }
    }
}
