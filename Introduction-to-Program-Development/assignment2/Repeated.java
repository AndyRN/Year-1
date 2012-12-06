/***************************************************************************************
* Author: Andy Nutt
* Student ID: 11004967
* File: Repeated.java
* Version: 1.0
***************************************************************************************/

package assignment2;

import java.util.Scanner;

public class Repeated {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    System.out.println("Please enter lines of text:");
    String line = "", pline = "";
    boolean blank = false;

    while (blank == false) {
      line = scan.nextLine();
      if ("".equals(line)) {
        blank = true;
        } else {
          if (pline.equals(line)) {
            System.out.println("Repeated: " + line);
          }
          pline = line;
        }
    }
    
    System.out.println("Farewell");
  }
  
}
