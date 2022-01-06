/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.resources;

/**
 *
 * @author paycraftsystems-i
 */
public class ErrorCodes {
    
    public static  int MESSAGE_NOT_FOUND = 800;
    
    
     public static String doErroDesc(int code) {
        String desc = "";
        switch(code)
        {
            case 200:
                desc = "SUCCESSFUL";
                break;
            case 201:
                desc = "CREATED";
                 break;
            case 401:
                desc = "UNAUTHORIZED";
                 break;
            case 202:
                desc = "ACCEPTED";
                 break;
             case 800:
                desc = "MESSAGE_NOT_FOUND";
                 break;
            case 811:
                desc = "INVALID SCHEME";
                 break;
          
                
               
              default:
                desc = "UNKOWN ERROR "+code;
                 break;
                 
            /*   
             case 939:
                desc = "LIMIT EXCEEDED";  
                 break;
            default:
                desc = "UNKOWN ERROR "+code;
                 break;
             */
                
             
              
        }
        
     return desc.replaceAll("_", " ");
    }
    
}
