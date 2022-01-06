/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.dto;

import com.paycraftsystems.kingstrivia.resources.ResourceHelper;
import static io.vertx.ext.web.codec.BodyCodec.jsonObject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author paycraftsystems-i
 */
public class EntryLog {
    
    public String sessionKey;// sessionid - msisdn
    public String sessionId;
    public String msisdn;
    public long questionNo;
    public long questionCategory;
    public String questionAnswer;
    public boolean status;
    private String lastInput;

    public EntryLog() {
    }

    public EntryLog(String sessionKey, String sessionId, String msisdn, long questionNo, String questionAnswer) {
        this.sessionKey = sessionKey;
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.questionNo = questionNo;
        this.questionAnswer = questionAnswer;
    }
    
    public EntryLog(String sessionKey, long questionNo, String questionAnswer) {
        this.sessionKey = sessionKey;
        this.questionNo = questionNo;
        this.questionAnswer = questionAnswer;
        this.lastInput = (this.lastInput == null || this.lastInput.trim().equals(""))? String.valueOf(questionNo).concat(questionAnswer):this.lastInput.trim()+"*"+String.valueOf(questionNo).concat(questionAnswer);
    }
    
    public EntryLog(String sessionKey, long questionNo) {
        this.sessionKey = sessionKey;
        this.questionNo = questionNo;
    }
    
    public JsonObject toJson() {
        ResourceHelper rh = new ResourceHelper();
        JsonObjectBuilder resp = Json.createObjectBuilder();
        try 
        {
            
            resp.add("sessionKey", this.msisdn+"-"+this.sessionId)
                .add("sessionId", rh.toDefault(this.sessionId))
                .add("msisdn", rh.toDefault(this.msisdn))
                .add("questionNo", this.questionNo)
                .add("questionCategory", this.questionCategory)
                .add("questionAnswer", rh.toDefault(this.questionAnswer));
            
        } catch (Exception e) {
        
        
        }
    
       return resp.build();
    }

    public String getLastInput() {
        
        //String dd = (String.valueOf(questionNo).concat("*"+questionAnswer))?;//(this.lastInput.trim().concat("X")+(String.valueOf(questionNo).concat("*"+questionAnswer))):"";
        
         //String dd = this.lastInput.trim().concat("X").concat(String.valueOf(questionNo).concat("*"+questionAnswer)?"":"";
        //    return (this.lastInput == null || this.lastInput.trim().equals(""))? (String.valueOf(questionNo).concat("*"+questionAnswer)):(this.lastInput.trim().concat("X")+(String.valueOf(questionNo).concat("*"+questionAnswer)));
        System.out.println(" ;; this = " + this.lastInput);
        
        return (this.lastInput == null || this.lastInput.trim().equals(""))? (String.valueOf(questionNo).concat("*"+questionAnswer)):(this.lastInput.trim().concat("#").concat(String.valueOf(questionNo).concat("*"+questionAnswer)));
    }
   

    @Override
    public String toString() {
        return "EntryLog{" + "sessionKey=" + sessionKey + ", sessionId=" + sessionId + ", msisdn=" + msisdn + ", questionNo=" + questionNo + ", questionCategory=" + questionCategory + ", questionAnswer=" + questionAnswer + ", status=" + status + '}';
    }

}
