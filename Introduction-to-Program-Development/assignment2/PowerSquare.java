/***************************************************************************************
* Author: Andy Nutt
* Student ID: 11004967
* File: PowerSquare.java
* Version: 1.2
***************************************************************************************/

package assignment2;

import java.util.Scanner;
import java.text.DecimalFormat;

public class PowerSquare {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    DecimalFormat fmt = new DecimalFormat("0.00");
    System.out.print("Enter 2 numbers: ");
    double n1 = 0.0, n2 = 0.0;
    
    n1 = scan.nextDouble();
    n2 = scan.nextDouble();
    
    double cuberesult = Math.pow(n1, 3);
    double sqrtresult = Math.sqrt(Math.abs(n2));
    double largestresult = Math.max(cuberesult, sqrtresult);
    System.out.println(fmt.format(largestresult));
  }
  
}
