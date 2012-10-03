/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: Oddy.java
Version: 1.3
***************************************************************************************/

package assignment2;

import java.util.Scanner;

public class Oddy {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    System.out.print("Please enter two integers: ");
    int n1 = 0, n2 = 0;

    n1 = scan.nextInt();
    n2 = scan.nextInt();

    int sum = 0;
    for (int i = Math.min(n1, n2); i <= Math.max(n1, n2); i++) {
      if ((i % 2) == 1) {
        sum += i;
      }
    }
    System.out.println(sum);

  }
  
}