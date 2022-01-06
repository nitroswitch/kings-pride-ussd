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
public class QuizEntry {
    
    public String sessionKey;// sessionid - msisdn
    public String sessionId;
    public String msisdn;
    public long questionNo;
    public long questionIndex;
    public String questionAnswer;
    public JsonObject jsonObject;

    public QuizEntry() {
    }

    public QuizEntry(String sessionKey, String sessionId, String msisdn, long questionNo, String questionAnswer) {
        this.sessionKey = sessionKey;
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.questionNo = questionNo;
        this.questionAnswer = questionAnswer;
    }
    
    public QuizEntry(String sessionKey, JsonObject jsonObject) {
        this.sessionKey = sessionKey;
        this.jsonObject = jsonObject;
    }
    
    public QuizEntry(String sessionKey, long questionNo) {
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
                .add("questionIndex", this.questionIndex)
                .add("questionAnswer", rh.toDefault(this.questionAnswer));
            
        } catch (Exception e) {
        
        
        }
    
       return resp.build();
    }

    @Override
    public String toString() {
        return "QuizEntry{" + "sessionKey=" + sessionKey + ", sessionId=" + sessionId + ", msisdn=" + msisdn + ", questionNo=" + questionNo + ", questionIndex=" + questionIndex + ", questionAnswer=" + questionAnswer + ", jsonObject=" + jsonObject + '}';
    }
    
    
    
}
