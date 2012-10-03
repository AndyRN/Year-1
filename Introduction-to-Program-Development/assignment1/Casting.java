/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: Casting.java
Version: 1.1
***************************************************************************************/

package assignment1;

import java.util.Scanner;

public class Casting {

  public static void main(String[] args) {
    double dnumber1, dnumber2, danswer;
    int inumber1, inumber2, ianswer;

    Scanner scan = new Scanner(System.in);
    
    System.out.print("Enter number 1: ");
    dnumber1 = scan.nextDouble();
    System.out.print("Enter number 2: ");
    dnumber2 = scan.nextDouble();
    
    inumber1 = (int) dnumber1;
    inumber2 = (int) dnumber2;
    
    ianswer = inumber2/inumber1;
    
    System.out.println("Integer result = " + ianswer);
        
    danswer = dnumber2/dnumber1;
    
    System.out.println("Double result = " + danswer);
  }
}
