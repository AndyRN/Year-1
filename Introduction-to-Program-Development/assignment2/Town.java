/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: Town.java
Version: 1.0
***************************************************************************************/

package assignment2;

public class Town {

  private String name;
  private int population;

  public String getName() {
    return name;
  }

  public int getPopulation() {
    return population;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPopulation(int population) {
    this.population = population;
  }

  public Town() {
    population = 10000;
    name = "AnyTown";
  }

  public Town(String name, int population) {
    this.name = name;
    this.population = population;
  }

  public Town calcBiggest(Town town) {
    if (town.getPopulation() > this.population) {
      return town;
    }
    return this;
  }
}
