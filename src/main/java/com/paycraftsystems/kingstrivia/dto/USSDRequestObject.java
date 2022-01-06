/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.dto;

import com.paycraftsystems.kingstrivia.resources.ResourceHelper;
import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author paycraftsystems-i
 */
public class USSDRequestObject {
    
   public String msisdn;
   public String message;
   public String sessionId;
   public String ussdversion;
   public String optype;
   public String servicecode;
   public String opcode;

   public USSDRequestObject() {
    
   }

    public USSDRequestObject(String msisdn, String message, String sessionId,  String ussdversion, String optype, String servicecode, String opcode) {
        this.msisdn = msisdn;
        this.message = message;
        this.sessionId = sessionId;
        this.ussdversion = ussdversion;
        this.optype = optype;
        this.servicecode = servicecode;
        this.opcode = opcode;
    }
    
    
    public USSDRequestObject(String msisdn, String message, String sessionId,  String optype, String servicecode, String opcode) {
        this.msisdn = msisdn;
        this.message = message;
        this.sessionId = sessionId;
        this.optype = optype;
        this.servicecode = servicecode;
        this.opcode = opcode;
    }
   
   
    
    public JsonObject toJson() {
       ResourceHelper rh = new ResourceHelper();
       JsonObjectBuilder job = Json.createObjectBuilder();
        try 
        {
            
            job.add("msisdn", rh.toDefault(this.msisdn))
               .add("message", rh.toDefault(this.message))
               .add("sessionId", rh.toDefault(this.sessionId))
               //.add("senderid", rh.toDefault(this.senderid))
              // .add("receiverid", rh.toDefault(this.receiverid))
               .add("ussdversion", rh.toDefault(this.ussdversion))
               .add("optype", rh.toDefault(this.optype))
               .add("servicecode", rh.toDefault(this.servicecode))
               .add("opcode", rh.toDefault(this.opcode));
           
            
        } catch (Exception e) {
        }
     
        return job.build();
    }

    @Override
    public String toString() {
        return "USSDRequestObject{" + "msisdn=" + msisdn + ", message=" + message + ", sessionId=" + sessionId + ", ussdversion=" + ussdversion + ", optype=" + optype + ", servicecode=" + servicecode + ", opcode=" + opcode + '}';
    }
    
   
}
