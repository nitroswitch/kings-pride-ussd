/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.resources;

/**
 *
 * @author paycraftsystems-i
 */
public enum CategorySelector {
    
    
    CURRENT_AFFAIRS(0), SPORTS(1), ENTERTAINMENT(2), ISLAMIC(3), BIBLE(4);
    
    
    private int categoryId;
    private String description;
    
  CategorySelector(int categoryId) {
    this.categoryId = categoryId;
  }
  
   CategorySelector(int categoryId, String description) {
    this.categoryId = categoryId;
    this.description = description;
  }

  public static CategorySelector getDescription(int value) {
    for (CategorySelector item : values()) {
      if (item.getCategoryId() == value) {
        return item;
      }
    }
    return CURRENT_AFFAIRS;
  }

    private CategorySelector() {
    }

    public int getCategoryId() {
        return categoryId;
    }


    public String getDescription() {
        return description;
    }

   

    @Override
    public String toString() {
        return "CategorySelector{" + "categoryId=" + categoryId + ", description=" + description + '}';
    }
   
}
