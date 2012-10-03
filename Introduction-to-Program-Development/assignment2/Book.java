/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: Book.java
Version: 1.0
***************************************************************************************/

package assignment2;

public class Book {

  private String name;
  private int price;
  private int number;
  
  public String getName() {
    return name;
  }

  public int getNumber() {
    return number;
  }

  public int getPrice() {
    return price;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public Book(String name) {
    this.name = name;
  }
  
  public int calcTotalValue() {
    return price * number;
  }  
  
}
