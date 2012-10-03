/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: Longer.java
Version: 1.2
****************************************************************************************/

package assignment3;

import java.util.Scanner;

public class Longer {

  public static void main(String[] args) {
    Scanner wordScan = new Scanner(System.in);
    System.out.println("Enter 2 words: ");
    String word1 = wordScan.next();
    String word2 = wordScan.next();

    System.out.println("Longest is " + longest(word1, word2));
  } //main

  public static String longest(String word1, String word2) {
    String lword = "";
    if (word2.length() > word1.length()) {
      lword = word2;
    } else {
      lword = word1;
    }
    return lword;
        
  } // longest
} //class

