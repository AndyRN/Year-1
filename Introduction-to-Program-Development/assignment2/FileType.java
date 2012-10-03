/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: FileType.java
Version: 1.1
***************************************************************************************/

package assignment2;

import java.util.Scanner;

public class FileType {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    System.out.println("Enter file names (blank line to end): ");
    String line = "";
    boolean blank = false;

    while (blank == false) {
      line = scan.nextLine();
      if ("".equals(line)) {
        blank = true;
      } else {
        if (line.endsWith(".txt")) {
          System.out.println("Text File: " + line);
        } else if (line.contains("del")) {
          System.out.println("Delete: " + line);
        }// if
      }// else
    }// while
    
  }// main()
  
}// FileType