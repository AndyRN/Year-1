/***************************************************************************************
* Author: Andy Nutt
* Student ID: 11004967
* File: Product.java
* Version: 1.0
****************************************************************************************/

package assignment3;

public class Product {

  String type;
  Metal metal;
  int weight;

  public Product(String type, Metal metal, int weight) {
    this.type = type;
    this.metal = metal;
    this.weight = weight;
  }

  public Metal getMetal() {
    return metal;
  }

  public void setMetal(Metal metal) {
    this.metal = metal;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public int calcMetalCost() {
    return metal.costPerUnitWeight * this.weight;
  }
  
} //class