package com.ccomp.model;

import java.io.Serializable;
import java.util.Vector;

public class Item implements Serializable {
	
	
    private String[] flowers = new String[]{"flower1.jpg", "flower2.jpg", 
            "flower3.jpg", "flower4.jpg", "flower5.jpg", "flower6.jpg"};
  private int price;
  private Vector shopcart = new Vector();
  
  public String[] getFlowers() {
      return flowers;
  }
  
  public int getPrice(String flower) {
      if (flower.equals("flower1.jpg")) {
          return 130;
      }
      if (flower.equals("flower2.jpg")) {
          return 230;
      }
      if (flower.equals("flower3.jpg")) {
          return 123;
      }
      if (flower.equals("flower4.jpg")) {
          return 111;
      }
      if (flower.equals("flower5.jpg")) {
          return 250;
      }
      if (flower.equals("flower6.jpg")) {
          return 110;
      }
      return 0;
  }

  public Object[] getShopcart() {
      if (shopcart != null) {
          if (!shopcart.isEmpty()) {
              return shopcart.toArray();
          }
      }
      return new String[]{};
  }

  public void addToShopcart(String item) {
      shopcart.add(item);
  }

  public void remToShopcart(String item) {
      if (shopcart != null) {
          if (shopcart.contains(item)) {
              shopcart.remove(item);
          }
      }
  }
  public void emptyShopcart() {
      if (shopcart != null) {
          shopcart.removeAll(shopcart);
      }
  }
}
