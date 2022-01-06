/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.dto;

/**
 *
 * @author paycraftsystems-i
 */
public class Increment {
    
   public String key;
   public int value; 

    public Increment() {
    }

    public Increment(String key, int value) {
        this.key = key;
        this.value = value;
    }

   

    @Override
    public String toString() {
        return "Increment{" + "key=" + key + ", value=" + value + '}';
    }
   
   
}
