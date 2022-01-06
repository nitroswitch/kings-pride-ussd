/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.resources;

import com.paycraftsystems.kingstrivia.dto.StaticMenuInterface;
import java.util.Random;

/**
 *
 * @author paycraftsystems-i
 */
public class ResourceHelper {
    
    
     public int toDefault(Integer value) {
        
        return (value == null )?0:value.intValue();
    }
     
    public  String toDefault(String value) {
        
        return (value == null || value.isEmpty())?"NA":value.trim();
    }
    
    public  String toDefault(String value, String defaultValue) {
        
        return (value == null || value.isEmpty())?defaultValue:value.trim();
    }
    
    public boolean isValidCommand(String msg) {
        boolean isCommand = false;
        try 
        {
            
            Integer.parseInt(msg);
            isCommand = true;
            
        } catch (Exception e) {
        }
     return isCommand; 
    }
    
    
    public static long doExtractCategory(String rx) {
        //String rx = "*3020*1*2#";
        long option = -1;
        try 
        {
            String[] split = rx.split("\\*");
            option = Long.parseLong(split[3].replaceAll("#", ""));
          
        } 
        catch (Exception e) {
        
            e.printStackTrace();
        
        }
      return option;
    }
    
    
     public static long doExtractLanguage(String rx) {
        //String rx = "*3020*1*2#";
        long option = -1;
        try 
        {
            String[] split = rx.split("\\*");
            option = Long.parseLong(split[2].replaceAll("#", ""));
           
        } 
        catch (Exception e) {
        
            e.printStackTrace();
        
        }
      return option;
    }
    
    /*
       for  first question step = 5
    */
    public static long doExtractAnswer(String rx, int step) {
        //String rx = "*3020*1*2#";
        long option = -1;
        try 
        {
            String[] split = rx.split("\\*");
            option = Long.parseLong(split[step].replaceAll("#", ""));
           
        } 
        catch (Exception e) {
        
            e.printStackTrace();
        
        }
      return option;
    }
    
    public String answersSwitch(String selectedIndex) {
        
        String systemValue = "";
        try 
        {
                 switch(selectedIndex)
                 {
                     case "1":
                     systemValue = "A";
                     break;
                     case "2":
                     systemValue = "B";
                     break;
                     case "3":
                     systemValue = "C";
                     break;
                     default:
                     systemValue = "X";
                     break;
                     
                 }
        
        
        } 
        catch (Exception e) {
       
        }
        
     return systemValue;
    }
    
    public static String doCaetoryDesc(int catid) {
        
        String systemValue = "";
        try 
        {
                 switch(catid)
                 {
                     case  1:
                     systemValue = "Current Affairs";
                     break;
                     case 2:
                     systemValue = "Sports";
                     break;
                     case 3:
                     systemValue = "Entertainment";
                     break;
                     case 4:
                     systemValue = "Islamic Knowledge";
                     break;
                     case 5:
                     systemValue = "Bible Knowledge";
                     break;
                     default:
                     systemValue = "X";
                     break;
                     
                     //public static String ENGLISH_MENU = "Please select option"+NEW_LINE+""+NEW_LINE+"1.Current Affairs"+NEW_LINE+"2.Sports"+NEW_LINE+"3.Entertainment"+NEW_LINE+"4.Islamic Knowledge"+NEW_LINE+"5.Bible Knowledge"+NEW_LINE+"6.Exit";
    
                     
                 }
        
        
        } 
        catch (Exception e) {
       
        }
        
     return systemValue;
    }
    
    public static String doLanguageDesc(int catid) {
        
        String systemValue = "";
        try 
        {
                 switch(catid)
                 {
                     case  1:
                     systemValue = "English";
                     break;
                     case 2:
                     systemValue = "Yoruba";
                     break;
                     case 3:
                     systemValue = "Hausa";
                     break;
                     case 4:
                     systemValue = "Ibo";
                     break;
                     default:
                     systemValue = "Wahala";
                     break;
                    
                 }
        
        
        } 
        catch (Exception e) {
       
        }
        
     return systemValue;
    }
    
    
    public  String doAnswerNotification(long languageId, String answerOption, String correctValue, boolean endplay) {
        //String rx = "*3020*1*2#";
        String nextDisplay = "";
        try 
        {
            if(answerOption.equals(correctValue)) // if(answersSwitch(answerOption).equals(correctValue))
            {
                if(!endplay)
                {
                   if(languageId == 1l) //english
                   {
                       nextDisplay = "Yay!, You are correct "+StaticMenuInterface.NEW_LINE+"1. To proceed to the next question "+StaticMenuInterface.NEW_LINE+"00. To Quit";
           
                   }
                   else  if(languageId == 2l) //yoruba
                   {
                        nextDisplay = "O kare!, O gbaa "+StaticMenuInterface.NEW_LINE+"1. Lati tesiwaju si ibeere tokan "+StaticMenuInterface.NEW_LINE+"00. Lati Japa";
           
                   }
                   else  if(languageId ==3l) //hausa
                   {
                        nextDisplay = "Ma sha Allah!, Wannan haka yake."+StaticMenuInterface.NEW_LINE+"1. Domin amsa wani tambayan"+StaticMenuInterface.NEW_LINE+"00. Domin fita";
           
                   }
                   else  if(languageId ==4l) //ibo
                   {
                      
                   }
                }
                else
                {
                   nextDisplay = "";// StaticMenuInterface.ENTRY_CONFIRMATION;
                }
                
            }
            else
            {
                if(!endplay)
                {
                  if(languageId == 1l) //english
                  {
                      nextDisplay = "Oooops!, That was a wrong answer "+StaticMenuInterface.NEW_LINE+"1. To proceed to the next question"+StaticMenuInterface.NEW_LINE+"00. To Quit";
                   
                  }
                  else  if(languageId == 2l) //yoruba
                  {
                       nextDisplay = "Oooops!, That was a wrong answer "+StaticMenuInterface.NEW_LINE+"1. Lati tesiwaju si ibeere tokan"+StaticMenuInterface.NEW_LINE+"00. Lati Japa";
                   
                  }
                  else  if(languageId ==3l) //hausa
                  {
                       nextDisplay = "Kash!, Ka fadi "+StaticMenuInterface.NEW_LINE+"1. Domin amsa wani tambayan"+StaticMenuInterface.NEW_LINE+"00. Domin fita";
                   
                  }
                  else  if(languageId ==4l) //ibo
                  {
                      
                  }
               
                  
                }
                else
                {
                   nextDisplay ="";// StaticMenuInterface.ENTRY_CONFIRMATION;
                }
            }
           
        } 
        catch (Exception e) {
        
            e.printStackTrace();
            
             nextDisplay = StaticMenuInterface.EXIT_ON_ERROR;
        
        }
      return nextDisplay;
    }
    
    
      public  String doAnswerNotification(long languageId, String answerOption, String correctValue) {
        //String rx = "*3020*1*2#";
        String nextDisplay = "";
        try 
        {
            if(answerOption.equals(correctValue)) // if(answersSwitch(answerOption).equals(correctValue))
            {
               
                  // nextDisplay = "Yay!, You are correct "+StaticMenuInterface.NEW_LINE+"1. To proceed to the next question "+StaticMenuInterface.NEW_LINE+"00. To Quit";
           
                   if(languageId == 1l) //english
                   {
                       nextDisplay = "Yay!, You are correct "+StaticMenuInterface.NEW_LINE+"1. To proceed to the next question "+StaticMenuInterface.NEW_LINE+"00. To Quit";
           
                   }
                   else  if(languageId == 2l) //yoruba
                   {
                        nextDisplay = "O kare!, O gbaa "+StaticMenuInterface.NEW_LINE+"1. Lati tesiwaju si ibeere tokan "+StaticMenuInterface.NEW_LINE+"00. Lati Japa";
           
                   }
                   else  if(languageId ==3l) //hausa
                   {
                        nextDisplay = "Ma sha Allah!, Wannan haka yake."+StaticMenuInterface.NEW_LINE+"1. Domin amsa wani tambayan"+StaticMenuInterface.NEW_LINE+"00. Domin fita";
           
                   }
                   else  if(languageId ==4l) //ibo
                   {
                      
                   }
            
            
            }
            else
            {
              
                  // nextDisplay = "Oooops!, That was a wrong answer "+StaticMenuInterface.NEW_LINE+"1. To proceed to the next question"+StaticMenuInterface.NEW_LINE+"00. To Quit";
               
                  if(languageId == 1l) //english
                  {
                      nextDisplay = "Oooops!, That was a wrong answer "+StaticMenuInterface.NEW_LINE+"1. To proceed to the next question"+StaticMenuInterface.NEW_LINE+"00. To Quit";
                   
                  }
                  else  if(languageId == 2l) //yoruba
                  {
                       nextDisplay = "Oooops!, That was a wrong answer "+StaticMenuInterface.NEW_LINE+"1. Lati tesiwaju si ibeere tokan"+StaticMenuInterface.NEW_LINE+"00. Lati Japa";
                   
                  }
                  else  if(languageId ==3l) //hausa
                  {
                       nextDisplay = "Kash!, Ka fadi "+StaticMenuInterface.NEW_LINE+"1. Domin amsa wani tambayan"+StaticMenuInterface.NEW_LINE+"00. Domin fita";
                   
                  }
                  else  if(languageId ==4l) //ibo
                  {
                      
                  }
            
            
            }
           
        } 
        catch (Exception e) {
        
            e.printStackTrace();
            
             nextDisplay = StaticMenuInterface.EXIT_ON_ERROR;
        
        }
      return nextDisplay;
    }
    
    
    public static int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
    }
    
    public static int getRandom(int[] array) {
     int rnd = new Random().nextInt(array.length);
     return array[rnd];
    }
    
    public static long getRandom(long[] array) {
     int rnd = new Random().nextInt(array.length);
     return array[rnd];
    }
    
}
