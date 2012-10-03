/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: BigWord.java
Version: 1.2
****************************************************************************************/
package assignment2;

import java.util.Scanner;

public class BigWord {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    System.out.print("Please enter 3 words: ");
    String word, biggestword = "";

    int bwlength = -1, length;
    for (int i = 0; i < 3; i++) {
      word = scan.next();
      length = word.length();
      if (length > bwlength) {
        biggestword = word;
        bwlength = length;
      }
    }

    System.out.println(biggestword);
  }
}
