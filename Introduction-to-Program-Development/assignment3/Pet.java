/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: Pet.java
Version: 1.0
****************************************************************************************/

package assignment3;

public class Pet {

  String name;
  String species;
  int age;

  public Pet(String name, String species, int age) {
    this.name = name;
    this.species = species;
    this.age = age;
  }

  @Override
  public String toString() {
    return (this.name + " (" + this.species + ") aged " + this.age);
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSpecies() {
    return species;
  }

  public void setSpecies(String species) {
    this.species = species;
  }
} //class