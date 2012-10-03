/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: NumbersInFile.java
Version: 1.1
****************************************************************************************/

package assignment3;

import java.util.Scanner;
import java.io.*;

public class NumbersInFile {

  public static void main(String[] args) throws FileNotFoundException {
    Scanner locationScan = new Scanner(System.in);
    System.out.println("Enter location of input file:");
    String location = locationScan.nextLine();
    Scanner fileScan = new Scanner(new File(location));

    int sumInt = 0, wordCount = 0;

    while (fileScan.hasNext()) {
      if (fileScan.hasNextInt()) {
        sumInt += fileScan.nextInt();
      } else {
        fileScan.next();
        wordCount++;
      }
    }

    System.out.println(sumInt + " " + wordCount);
   
  } //main
} //class