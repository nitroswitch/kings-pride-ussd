/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.dto;

import javax.json.JsonObject;

/**
 *
 * @author paycraftsystems-i
 */
public class QuestionInfo {
    
    public long questionNo;
    public String questionAnswer;
    public JsonObject jsonObject;

    public QuestionInfo() {
    }

    public QuestionInfo(long questionNo, String questionAnswer) {
        this.questionNo = questionNo;
        this.questionAnswer = questionAnswer;
    }
    
    public QuestionInfo(long questionNo, JsonObject jsonObject) {
        this.questionNo = questionNo;
        this.jsonObject = jsonObject;
    }

    @Override
    public String toString() {
        return "QuestionInfo{" + "questionNo=" + questionNo + ", questionAnswer=" + questionAnswer +", jsonObject ="+jsonObject+ '}';
    }
    
}
