/***************************************************************************************
* Author: Andy Nutt
* Student ID: 11004967
* File: MyPets.java
* Version: 1.2
****************************************************************************************/

package assignment3;

import java.util.ArrayList;

public class MyPets {

  ArrayList<Pet> pets = new ArrayList<Pet>();

  public MyPets(ArrayList<Pet> pets) {
    this.pets = pets;
  }

  public Pet oldest() {
    Pet petOld = null, petCurrent;
    for (int i = 0; i <= pets.size(); i++) {
      petCurrent = pets.get(i);
      if (petCurrent.age > petOld.age) {
        petOld = petCurrent;
      }
    }
    return petOld;
  }

  public int numberOfPets() {
    return pets.size();
  }

  public void replacePet(Pet oldPet, Pet newPet) {
    int pos = pets.indexOf(oldPet);
    pets.remove(oldPet);
    pets.add(pos, newPet);
  }

  public void addPet(Pet newPet) {
    pets.add(newPet);
  }

  public boolean removePet(String name) {
    if (pets.contains(name)) {
      return false;
    } else {
      return true;
    }

  }
} //class