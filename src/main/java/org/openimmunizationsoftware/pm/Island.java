package org.openimmunizationsoftware.pm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openimmunizationsoftware.pm.model.Creature;
import org.openimmunizationsoftware.pm.model.MatchItem;
import org.openimmunizationsoftware.pm.model.Scorer;
import org.openimmunizationsoftware.pm.model.World;

/**
 * This is the command line entry point for running the optimization process.
 * 
 * @author Nathan Bunker
 * 
 */
public class Island
{
  /**
   * To run this via the command line, follow these steps:
   * <p>
   * 1. Startup local CentralServlet or obtain URL to CentralServlet you will
   * connect to.
   * <p>
   * 2. Identify source file for you tests. The default tests are currently
   * checked into the project.
   * <p>
   * 3. Modify and use this command line from the root of the project:
   * <p>
   * <code>mvn exec:java -Dexec.mainClass="org.openimmunizationsoftware.pm.Island" -Dexec.args="http://localhost:8286/CentralServlet src/main/java/org/openimmunizationsoftware/pm/servlet/MIIS-F1.txt"</code>
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err
          .println("usage: java org.openimmunizationsoftware.pm.Island [central url] [test case filename] [world size] [weights]");
      // java -classpath
      // "classes;C:/Documents and Settings/nathan/.m2/repository/com/wcohen/ss/20060615/ss-20060615.jar"
      // org.openimmunizationsoftware.pm.Island
      // http://localhost:8286/CentralServlet MIIS-C.txt
      //
      // java -classpath
      // "classes;C:/Users/Nathan Bunker/.m2/repository/com/wcohen/ss/20060615/ss-20060615.jar"
      // org.openimmunizationsoftware.pm.Island
      // http://localhost:8286/CentralServlet MIIS-E2.txt
      //
      // java -classpath
      // "classes;C:/Users/Nathan Bunker/.m2/repository/com/wcohen/ss/20060615/ss-20060615.jar"
      // org.openimmunizationsoftware.pm.Island
      // http://localhost:8286/CentralServlet MIIS-E2.txt 400
      // 20,0,-5,-20,10,0,-40,-10,10
    }
    String centralUrlString = args[0];
    String testCasesFilename = args[1];
    String worldSizeString = null;
    String weightsString = null;
    if (args.length > 2) {
      worldSizeString = args[2];
      if (args.length > 3) {
        weightsString = args[3];
      }
    }

    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(testCasesFilename)));
    List<MatchItem> matchItemList = readSourceFile(in);

    System.out.println("Starting patient match optimization island.");
    System.out.println("Central repository location: " + centralUrlString);
    System.out.println("Test Case Filename: " + testCasesFilename);
    System.out.println("  + test case count: " + matchItemList.size());
    String worldName = readInput("World Name");
    if (worldName == null || worldName.equals("")) {
      System.out.println("Not starting world name.");
      System.exit(0);
    }
    String islandName = readInput("Island Name");
    if (islandName == null || islandName.equals("")) {
      System.out.println("Not starting world without island name.");
      System.exit(0);
    }
    if (worldSizeString == null) {
      worldSizeString = readInput("World Size");
    }
    if (worldSizeString == null || worldSizeString.equals("") || worldSizeString.equals("0")) {
      System.out.println("Not starting world without world size");
      System.exit(0);
    }

    int worldSize = 0;
    try {
      worldSize = Integer.parseInt(worldSizeString);
    } catch (NumberFormatException nfe) {
      System.out.println("Invalid world size, must be number");
    }
    if (worldSize <= 10) {
      System.out.println("World size too small");
      System.exit(0);
    }

    if (weightsString != null) {
      String[] ws = weightsString.split("\\,");
      int i = 0;
      int j = 0;
      int p = 0;
      int[][] w = Scorer.getWeights();
      while (p < ws.length && i < 3) {
        w[i][j] = Integer.parseInt(ws[p]);
        j++;
        if (j == 3) {
          i++;
          j = 0;
        }
        p++;
      }
      System.out.println();
      System.out.println("  +--------------+-------+-------+-------+");
      System.out.println("  | Weights      | Match | Poss  | Not M |");
      System.out.println("  +--------------+-------+-------+-------+");
      System.out.println("  | Should Match |" + pad(w[0][0]) + " |" + pad(w[0][1]) + " |" + pad(w[0][2]) + " |");
      System.out.println("  | Should Poss  |" + pad(w[1][0]) + " |" + pad(w[1][1]) + " |" + pad(w[1][2]) + " |");
      System.out.println("  | Should Not M |" + pad(w[2][0]) + " |" + pad(w[2][1]) + " |" + pad(w[2][2]) + " |");
      System.out.println("  +--------------+-------+-------+-------+");
    }

    URL centralUrl = new URL(centralUrlString);

    System.out.println("Asking central server for base case");
    String baseCase = null;
    try {
      baseCase = IslandSync.requestStartScript(worldName, centralUrl);
    } catch (Exception e) {
      System.out.println("Unable to query central server");
      e.printStackTrace(System.err);
    }

    System.out.println("Creating world");
    World world = new World(worldSize, worldName, islandName, baseCase);

    world.setMatchItemList(matchItemList);

    System.out.println("Syncing with central server");
    IslandSync islandSync = new IslandSync(world, centralUrl);
    try {
      islandSync.sendQuery();
    } catch (Exception e) {
      System.out.println("Unable to query central server");
      e.printStackTrace(System.err);
    }

    islandSync.start();
    System.out.println("Regular island sync started");

    world.start();
    System.out.println("World started on island.");

    while (true) {
      System.out.println();
      System.out.println("  +------------------+------------------+-------+-------+");
      System.out.println("  |      World       |      Island      |  Pop  |  Gen  | ");
      System.out.println("  +------------------+------------------+-------+-------+");
      System.out.println("  | " + pad(world.getWorldName(), 16) + " | " + pad(world.getIslandName(), 16) + " |"
          + pad(worldSize) + " |" + pad(world.getGeneration()) + " | ");
      System.out.println("  +------------------+------------------+-------+-------+");
      System.out.println("   Generating status: " + world.getLastMessage());
      System.out.println("   Score rate (c/s):  " + world.getScoreRate());
      System.out.println("   Sync status:       " + islandSync.getLastMessage());
      Creature[] creatures = world.getCreaturesCopy();
      if (creatures != null) {
        System.out.println();
        System.out.println("  +-------+-------+");
        System.out.println("  |  Gen  | Score |");
        System.out.println("  +-------+-------+");
        for (int i = 0; i < 10 && i < creatures.length; i++) {

          System.out.println("  |" + pad(creatures[i].getGeneration()) + " |"
              + pad((int) (creatures[i].getScore() * 100.0 + 0.5)) + " |");
        }
        System.out.println("  +-------+-------+");
        if (creatures.length > 0) {
          System.out.println();
          System.out.println("  +--------------+-------+-------+-------+");
          System.out.println("  |              | Match | Poss  | Not M |");
          System.out.println("  +--------------+-------+-------+-------+");
          Scorer scorer = creatures[0].getScorer();
          int[][] c = scorer.getCountTable();
          System.out.println("  | Should Match |" + pad(c[0][0]) + " |" + pad(c[0][1]) + " |" + pad(c[0][2]) + " |");
          System.out.println("  | Should Poss  |" + pad(c[1][0]) + " |" + pad(c[1][1]) + " |" + pad(c[1][2]) + " |");
          System.out.println("  | Should Not M |" + pad(c[2][0]) + " |" + pad(c[2][1]) + " |" + pad(c[2][2]) + " |");
          System.out.println("  +--------------+-------+-------+-------+");
        }
      }
      System.out.println();
      String command = readInput("Command");
      if (command != null && !command.equals("")) {
        if (command.equalsIgnoreCase("exit")) {
          System.out.println("Stopping world");
          world.setKeepRunning(false);
          islandSync.setKeepRunning(false);
          islandSync.interrupt();
          break;
        } else if (command.equalsIgnoreCase("sync")) {
          System.out.println("Syncing now");
          islandSync.interrupt();
        } else if (command.equalsIgnoreCase("script")) {
          if (creatures != null) {
            System.out.println(creatures[0].makeScript());
          }
        } else {
          System.out.println("The following commands are available: ");
          System.out.println(" + exit   - Stop processing and exit");
          System.out.println(" + sync   - Synchronize data to central server");
          System.out.println(" + script - Print out script for all creatures");
        }
      }
    }

  }

  private static String readInput(String question) {
    System.out.print(question + ": ");
    // open up standard input
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // read the username from the command-line; need to use try/catch with the
    // readLine() method
    try {
      return br.readLine();
    } catch (IOException ioe) {
      System.out.println("IO error trying to read your name!");
      System.exit(1);
      return null;
    }
  }

  // This is a simplistic way to space out the output
  private static final String PAD = "                    ";

  private static String pad(int i) {
    int length = 6;
    return pad(i, length);
  }

  private static String pad(int i, int length) {
    String num = "" + i;
    String padding = PAD + num;
    if (num.length() > length) {
      return num;
    }
    return padding.substring(padding.length() - length);
  }

  private static String pad(String value, int length) {
    if (value.length() > length) {
      return value.substring(length);
    }
    String padding = value + PAD;
    return padding.substring(0, length);
  }

  /**
   * Reads lines a buffered reader and creates a set of MatchTestCases. This may
   * be used to display to the user or to start and optimization run.
   * 
   * @param in
   *          open buffered reader
   * @return a list of MatchTestCase ojects
   * @throws IOException
   */
  public static List<MatchItem> readSourceFile(BufferedReader in) throws IOException {
    List<MatchItem> matchItemList = new ArrayList<MatchItem>();
    MatchItem matchItem = null;
    String line = "";
    while ((line = in.readLine()) != null) {
      if (line.startsWith("TEST:")) {
        if (matchItem != null) {
          matchItemList.add(matchItem);
        }
        matchItem = new MatchItem();
        matchItem.setLabel(readValue(line));
      } else if (matchItem != null) {
        if (line.startsWith("EXPECT:")) {
          matchItem.setExpectStatus(readValue(line));
        } else if (line.startsWith("PATIENT A:")) {
          matchItem.setPatientDataA(readValue(line));
        } else if (line.startsWith("PATIENT B:")) {
          matchItem.setPatientDataB(readValue(line));
        } else if (line.startsWith("DESCRIPTION:")) {
          matchItem.setDescription(readValue(line));
        }
      }
    }
    if (matchItem != null) {
      matchItemList.add(matchItem);
    }
    return matchItemList;
  }

  private static String readValue(String s) {
    int pos = s.indexOf(":");
    if (pos == -1) {
      return "";
    } else {
      return s.substring(pos + 1).trim();
    }
  }
}
