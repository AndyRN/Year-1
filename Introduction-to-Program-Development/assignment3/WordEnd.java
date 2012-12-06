/***************************************************************************************
* Author: Andy Nutt
* Student ID: 11004967
* File: WordEnd.java
* Version: 1.0
****************************************************************************************/

package assignment3;

import java.util.Scanner;

public class WordEnd {

  public static void main(String[] args) {
    Scanner wordScan = new Scanner(System.in);
    System.out.println("Enter a word: ");
    String word = wordScan.next();
    int length = word.length();

    for (int i = length - 1; i > -1; i--) {
      System.out.print(word.substring(i) + ",");
    }
    
    System.out.println();
    
  } //main
} //class