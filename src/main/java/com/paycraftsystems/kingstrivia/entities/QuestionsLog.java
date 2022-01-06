/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.entities;

import com.paycraftsystems.kingstrivia.dto.StaticMenuInterface;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author paycraftsystems-i
 */

@Entity
@Table(name="questions_log")
public class QuestionsLog extends PanacheEntityBase implements Serializable{
    
    private static Logger LOGGER  = LoggerFactory.getLogger(QuestionsLog.class);
    
    @Id
    @GeneratedValue
    @Column(name="tid")
    public long tid;
    
    @Column(name="question_category")
    public long questionCategory;
    
    @Column(name="quest_language")
    public long questLanguage;
    
    @Column(name="question")
    public String question;
    
    
    @Column(name="created_date")
    public LocalDateTime createdDate;
    
    
    @Column(name="status")
    public long status;
    
    @Column(name="created_by")
    public String createdBy;
    
    
    @Column(name="valid_option")
    public String validOption;
    
   
    /*
    @Transactional
    public static QuestionsLog doLog(USSDRequestObject requestObj, long duration) {
        QuestionsLog doLookUp = null;
        try 
        {
            
             doLookUp = doLookUp(requestObj.sessionId, requestObj.msisdn);
             
             if(doLookUp == null)
             {
                 QuestionsLog newSessionid = new QuestionsLog();
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
    
    @Transactional
    public static QuestionsLog doSync(USSDRequestObject requestObj) {
        QuestionsLog doLookUp = null;
        try 
        {
            
        } 
        catch (Exception e) {
        
            e.printStackTrace();
        
        }
       return doLookUp; 
    }
    
    */
    public static QuestionsLog doLookUpByCategory(long questionCategory, String tid) {
        QuestionsLog doLog = null;
        try 
        {
            doLog = find("questionCategory = ?1 and tid = ?2",questionCategory,tid).firstResult();
        } 
        catch (Exception e) {
        
            
            LOGGER.error("Exception ERROR @ doLookUpByCategory",e);
        
        }
        
      return doLog;
    }
    
    
    public static String doLoadQuestionAndAnswers(long questionId) {
        QuestionsLog doLog = null;
        String questionAndAnswers = "";
        try 
        {  
            doLog = QuestionsLog.findById(questionId);
            
            if(doLog !=null)
            {
                questionAndAnswers = doLog.question+StaticMenuInterface.NEW_LINE+AnswersLog.doLookUpSelectedQuestionAnswers(questionId);
           
            
            
            }
            else
            {
                questionAndAnswers = StaticMenuInterface.EXIT_ON_ERROR;
            }
           
        
        } 
        catch (Exception e) {
        
            
            LOGGER.error("Exception ERROR @ doLoadQuestionAndAnswers",e);
        
        }
        
      return questionAndAnswers;
    }
    
    
    public static String doLoadQuestionAndAnswers(long questionId, long language) {
        QuestionsLog doLog = null;
        String questionAndAnswers = "";
        try 
        {  
            doLog =  find("tid = ?1 and questLanguage = ?2", questionId, language).firstResult();// QuestionsLog.findById(questionId);
            
            if(doLog !=null)
            {
                questionAndAnswers = doLog.question+StaticMenuInterface.NEW_LINE+AnswersLog.doLookUpSelectedQuestionAnswers(questionId);
           
            }
            else
            {
                questionAndAnswers = StaticMenuInterface.EXIT_ON_ERROR;
            }
           
        
        } 
        catch (Exception e) {
        
            
            LOGGER.error("Exception ERROR @ doLoadQuestionAndAnswers",e);
        
        }
        
      return questionAndAnswers;
    }
    
    
    public static List<QuestionsLog> doLoadQuestionAndValidAnswers() {
        List<QuestionsLog> doLog = new ArrayList<>();
        try 
        {  
            doLog = QuestionsLog.listAll();
            
            
            LOGGER.info(" --doLoadQuestionAndValidAnswers -- "+doLog.size());
            
           // doLog = find("questionCategory = ?1 and tid = ?2",questionCategory,tid).firstResult();
        
            
        
        } 
        catch (Exception e) {
        
            
            LOGGER.error("Exception ERROR @ doLoadQuestionAndValidAnswers",e);
        
        }
        
      return doLog;
    }
    
    
    public static  List<Long> doLookUpByCategory(long questionCategory, long language) {
        List<QuestionsLog> doLog = new ArrayList<>();
        List<Long> tids = new ArrayList<>();
        try 
        {
            doLog = find("questionCategory = ?1 and questLanguage = ?2 ",questionCategory,language).list();
        
            doLog.stream().forEach(a->tids.add(a.tid));
            
            LOGGER.info(" + --- QUESTIONS -- + "+tids);
        
        } 
        catch (Exception e) {
        
            
            LOGGER.error("Exception ERROR @ doLookUpByCategory []",e);
        
        }
        
      return tids;
    }

    @Override
    public String toString() {
        return "QuestionsLog{" + "tid=" + tid + ", questionCategory=" + questionCategory + ", questLanguage=" + questLanguage + ", question=" + question + ", createdDate=" + createdDate + ", status=" + status + ", createdBy=" + createdBy + ", validOption=" + validOption + '}';
    }

   
    
   
}
