/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.entities;

import com.paycraftsystems.kingstrivia.dto.StaticMenuInterface;
import com.paycraftsystems.kingstrivia.dto.USSDRequestObject;
import com.paycraftsystems.kingstrivia.resources.ResourceHelper;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.JsonbBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author paycraftsystems-i
 */

@Entity
@Table(name="answers_log")
public class AnswersLog extends PanacheEntityBase implements Serializable{
    
    private static Logger LOGGER  = LoggerFactory.getLogger(AnswersLog.class);
    
    @Id
    @GeneratedValue
    @Column(name="tid")
    public long tid;
    
    @Column(name="question_id")
    public long questionId;
    
    @Column(name="answer_option")
    public String answerOption;
    
    @Column(name="created_date")
    public LocalDateTime createdDate;
    
    
    @Column(name="status")
    public long status;
    
    @Column(name="created_by")
    public String createdBy;
    
    @Column(name="answer_index")
    public int answerIndex;

    public int getAnswerIndex() {
        return answerIndex;
    }
    
   
    
    /*
    @Transactional
    public static AnswersLog doLog(USSDRequestObject requestObj, long duration) {
        AnswersLog doLookUp = null;
        try 
        {
            
             doLookUp = doLookUp(requestObj.sessionId, requestObj.msisdn);
             
             if(doLookUp == null)
             {
                 AnswersLog newSessionid = new AnswersLog();
                 newSessionid.lastCommand = requestObj.message;
                 newSessionid.message = requestObj.message;
                 newSessionid.msisdn = requestObj.msisdn;
                 newSessionid.sessionDate = LocalDateTime.now();
                 newSessionid.sessionDurationMillis = duration;
                 newSessionid.sessionId = requestObj.sessionId;
                 newSessionid.status = 0l;
                
                 doLookUp = Panache.getEntityManager().merge(newSessionid);
             }
             else
             {
                
                 doLookUp.lastCommand = doLookUp.lastCommand.replaceAll("#", "")+"*"+requestObj.message.replaceAll("#", "").replaceAll("\\*", "")+"#";
                 //doLookUp.msisdn = requestObj.msisdn;
                 doLookUp.lastSessionDate = LocalDateTime.now();
                 doLookUp.sessionDurationMillis = 0l;
                // doLookUp.sessionId = requestObj.sessionId;
                 doLookUp.status = 0l;
                
                 doLookUp = Panache.getEntityManager().merge(doLookUp);
                 
             }
            
        } 
        catch (Exception e) {
        
           // e.printStackTrace();
            
            
            LOGGER.error(" -- Exception -@ SessionsLog @ -doLog ",e);
        
        }
        
     return doLookUp;
    }
    */
    @Transactional
    public static AnswersLog doSync(USSDRequestObject requestObj) {
        AnswersLog doLookUp = null;
        try 
        {
            
        } 
        catch (Exception e) {
        
            e.printStackTrace();
        
        }
       return doLookUp; 
    }
    
    public static AnswersLog doLookUp(String sessionId, String msisdn) {
        AnswersLog doLog = null;
        try 
        {
            doLog = find("sessionId = ?1 and msisdn = ?2",sessionId,msisdn).firstResult();
        } 
        catch (Exception e) {
        
            
            LOGGER.error("Exception ERROR @ doLookUp",e);
        
        }
        
      return doLog;
    }
    
    
    public static String doLookUpSelectedQuestionAnswers(long questionId) {
        List<AnswersLog> doLog = new ArrayList<>();
        String answers = "";
        int counta = 0;
        try 
        {
            doLog = find("questionId = ?1   order by tid asc ",questionId).list();
            
            if(!doLog.isEmpty())
            {
                   //doLog.stream().forEach(a->a.);
                
                  List<AnswersLog> sorted =  doLog.stream().sorted(Comparator.comparingInt(AnswersLog::getAnswerIndex)).collect(Collectors.toList());
                 // doLog =  doLog.stream().sorted(Comparator.comparingInt(AnswersLog::getAnswerIndex())).collect(Collectors.toList());
                   //LOGGER.info(" -- sorted -- "+sorted);
                   for (AnswersLog answersLog : sorted) {
                  
                       //counta +=1;
                       answers += answersLog.answerIndex+". "+answersLog.answerOption+StaticMenuInterface.NEW_LINE;
                   } 
                   
                   /*
                    for (AnswersLog answersLog : doLog) {
                  
                       counta +=1;
                       answers += counta+". "+answersLog.answerOption+StaticMenuInterface.NEW_LINE;
                   } 
                   */
                 
            }
        } 
        catch (Exception e) {
        
            
            LOGGER.error("Exception ERROR @ doLookUp",e);
        
        }
        
      return answers;
    }
    
    public static List<AnswersLog> doLoadQuestionAnswers() {
        List<AnswersLog> doLog = new ArrayList<>();
        String answers = "";
        int counta = 0;
        try 
        {
            doLog = find("isValid = ?1",true).list();
            
        } 
        catch (Exception e) {
        
            
            LOGGER.error("Exception ERROR @ doLoadQuestionAnswers",e);
        
        }
        
      return doLog;
    }
    
    
    public JsonObject toJson() {
        
        ResourceHelper rh = new ResourceHelper();
        JsonObjectBuilder job = Json.createObjectBuilder();
        try 
        {
            job.add("tid", this.tid)
               .add("questionId", this.questionId)
               .add("answerOption", rh.toDefault(this.answerOption))
               .add("status", this.status);
             
            
        } catch (Exception e) {
        }
       
        
     return job.build();
    }
    
    
    public static AnswersLog toAnswerObect(JsonObject json) {
        
        AnswersLog  obj = null;
        try 
        {
             obj = JsonbBuilder.create().fromJson(json.toString(), AnswersLog.class);
             
            
        } 
        catch (Exception e) {
        
            LOGGER.info(" Exception Error @ toAnswerObect ",e);
        }
       
        
     return obj;
    }
    
    

    @Override
    public String toString() {
        return "AnswersLog{" + "tid=" + tid + ", questionId=" + questionId + ", answerOption=" + answerOption + ", createdDate=" + createdDate + ", status=" + status + ", createdBy=" + createdBy + '}';
    }

}
