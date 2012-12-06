/***************************************************************************************
* Author: Andy Nutt
* Student ID: 11004967
* File: Metal.java
* Version: 1.0
****************************************************************************************/

package assignment3;

public class Metal {

  String name;
  int costPerUnitWeight;

  public Metal(String name, int costPerUnitWeight) {
    this.name = name;
    this.costPerUnitWeight = costPerUnitWeight;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public int getCostPerUnitWeight() {
    return costPerUnitWeight;
  }

  public void setCostPerUnitWeight(int costPerUnitWeight) {
    this.costPerUnitWeight = costPerUnitWeight;
  }
  
} //class