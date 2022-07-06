package org.immregistries.pm;

import com.wcohen.ss.JaroWinkler;


/**
 * Verification test of the JaroWinkler library.
 */
public class JaroWinklerTest {

  private static int max(int i, int j) {
    return i > j ? i : j;
  }

  private static int min(int i, int j) {
    return i < j ? i : j;
  }

  private static int min(int i, int j, int k) {
    return min(min(i, j), k);
  }

  public static String getCommonCharacters(String string1, String string2, int allowedDistance) {

    int str1_len = string1.length();
    int str2_len = string2.length();
    String temp_string2 = string2;

    String commonCharacters = "";

    for (int i = 0; i < str1_len; i++) {
      boolean noMatch = true;

      // compare if char does match inside given allowedDistance
      // and if it does add it to commonCharacters
      for (int j = max(0, i - allowedDistance); noMatch && j < min(i + allowedDistance + 1, str2_len); j++) {
        if (charAt(temp_string2, j) == charAt(string1, i)) {
          noMatch = false;
          commonCharacters += charAt(string1, i);
          temp_string2 = temp_string2.substring(0, j) + temp_string2.substring(j + 1);
        }
      }
    }

    return commonCharacters;
  }

  public static double jaroScore(String string1, String string2) {

    int str1_len = string1.length();
    int str2_len = string2.length();

    // theoretical distance
    int distance = min(str1_len, str2_len) / 2;

    // get common characters
    String commons1 = getCommonCharacters(string1, string2, distance);
    String commons2 = getCommonCharacters(string2, string1, distance);

    int commons1_len = commons1.length();
    int commons2_len = commons2.length();
    if (commons1_len == 0)
      return 0;
    if (commons2_len == 0)
      return 0;

    // calculate transpositions
    double transpositions = 0;
    int upperBound = min(commons1_len, commons2_len);
    for (int i = 0; i < upperBound; i++) {
      if (charAt(commons1, i) != charAt(commons2, i)) {
        transpositions++;
      }
    }
    transpositions /= 2.0;

    // return the Jaro distance
    return (((double) commons1_len) / str1_len + ((double) commons2_len) / (str2_len) + ((double) (commons1_len - transpositions)) / (commons1_len)) / 3.0;

  }

  private static char charAt(String s, int pos) {
    if (pos >= s.length()) {
      return ' ';
    }
    return s.charAt(pos);
  }

  public static int getPrefixLength(String string1, String string2) {
    return getPrefixLength(string1, string2, 4);
  }

  public static int getPrefixLength(String string1, String string2, int MINPREFIXLENGTH) {

    int n = min(MINPREFIXLENGTH, string1.length(), string2.length());

    for (int i = 0; i < n; i++) {
      if (i >= string1.length() || i >= string2.length()) {
        // reached end of at least one string
        return i;
      } else if (string1.charAt(i) != string2.charAt(i)) {
        return i;
      }
    }

    // first n characters are the same
    return n;
  }

  public static double score(String string1, String string2) {
    return score(string1, string2, 0.1);
  }

  public static double score(String string1, String string2, double PREFIXSCALE) {

    double JaroDistance = jaroScore(string1, string2);

    int prefixLength = getPrefixLength(string1, string2);

    return JaroDistance + prefixLength * PREFIXSCALE * (1.0 - JaroDistance);
  }

  
  public static void main(String[] args) {
    JaroWinkler jw = new JaroWinkler();
    String[][] compares = new String[][] { 
    	{ "Pablo20091016Alkira49555",
    	  "Veit20071216Tomasz48084" 
    	}};
    for (int i = 0; i < compares.length; i++) {
      //System.out.println(" + " + compares[i][0].substring(0, 20) + " ~ " + compares[i][1].substring(0, 20) + " = " + score(compares[i][0], compares[i][1]));
      System.out.println(" * " + compares[i][0].substring(0, 20) + " ~ " + compares[i][1].substring(0, 20) + " = " + jw.score(compares[i][0], compares[i][1]));
    }
  }

}
