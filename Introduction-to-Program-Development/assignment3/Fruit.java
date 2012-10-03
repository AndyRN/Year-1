/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: Fruit.java
Version: 1.1
***************************************************************************************/

package assignment3;

import java.util.Scanner;

public class Fruit {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    System.out.println("A Apple");
    System.out.println("B Blueberry");
    System.out.println("C Currant");
    System.out.println("D Dragon Fruit");
    System.out.println("E Elderberry");
    System.out.println("F Finger Lime");
    System.out.println("Choose a fruit from menu above: ");

    String choice = scan.nextLine();
    Character ch = choice.charAt(0);

    switch (ch) {
      case ('a'): case('A'):
        System.out.println("You chose \"Apple\"");
        break;
      case ('b'): case('B'):
        System.out.println("You chose \"Blueberry\"");
        break;
      case ('c'): case('C'):
        System.out.println("You chose \"Currant\"");
        break;
      case ('d'): case('D'):
        System.out.println("You chose \"Dragon Fruit\"");
        break;
      case ('e'): case('E'):
        System.out.println("You chose \"Elderberry\"");
        break;
      case ('f'): case('F'):
        System.out.println("You chose \"Finger Lime\"");
        break;
      default:
        System.out.println("That fruit is not on the list");
    }

  } //main
} //class