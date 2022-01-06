/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.entities;

import com.paycraftsystems.kingstrivia.dto.StaticMenuInterface;
import com.paycraftsystems.kingstrivia.dto.USSDRequestObject;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.find;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Table(name="entries_log")
public class EntriesLog extends PanacheEntityBase implements Serializable{
    
    private static Logger LOGGER  = LoggerFactory.getLogger(EntriesLog.class);
    
    @Id
    @GeneratedValue
    @Column(name="tid")
    public long tid;
    
    @Column(name="session_id")
    public String sessionId;
    
    @Column(name="msisdn")
    public String msisdn;
    
    @Column(name="first_question")
    public long firstQuestion;
    
    @Column(name="second_question")
    public long secondQuestion;
    
    @Column(name="third_question")
    public long thirdQuestion;
    
    
    @Column(name="first_answer")
    public String firstAnswer;
    
    @Column(name="second_answer")
    public String secondAnswer;
    
    @Column(name="third_answer")
    public String thirdAnswer;
    
    @Column(name="entry_info")
    public String entryInfo;
    
     @Column(name="entry_qualification")
    public String entryQualification;
    
    
    @Column(name="entrie_date")
    public LocalDateTime entrieDate;
    
    @Column(name="last_answer_date")
    public LocalDateTime lastAnswerDate;
    
    
    
    @Column(name="status")
    public long status;
    
   
    @Transactional
    public static EntriesLog doLog(String msisdn, String sessionId, long question, String answer, int step) {
        EntriesLog doLookUp = null;
        try 
        {
            
             doLookUp = doLookUp(sessionId, msisdn);
             
             if(doLookUp == null)
             {
                 EntriesLog newEntry = new EntriesLog();
                 newEntry.msisdn = msisdn;
                 newEntry.sessionId = sessionId;
                 newEntry.firstAnswer = answer;
                 newEntry.firstQuestion = question;
                 newEntry.status = 0l;
                 newEntry.lastAnswerDate = LocalDateTime.now();
                
                 doLookUp = Panache.getEntityManager().merge(newEntry);
             }
             else
             {
                
                
                 if(step == 2)
                 {
                     doLookUp.thirdAnswer = answer;
                     doLookUp.thirdQuestion = question;
                     doLookUp.lastAnswerDate = LocalDateTime.now();
                 }
                 else  if(step == 3)
                 {
                     
                     doLookUp.thirdAnswer = answer;
                     doLookUp.thirdQuestion = question;
                     doLookUp.entrieDate = LocalDateTime.now();
                     
                 }
                 
                 doLookUp.status = 0l;
                
                 doLookUp = Panache.getEntityManager().merge(doLookUp);
                 
             }
            
        } 
        catch (Exception e) {
        
           // e.printStackTrace();
            
            
            LOGGER.error(" -- Exception -@ EntriesLog @ -doLog ",e);
        
        }
        
     return doLookUp;
    }
    
    @Transactional
    public static EntriesLog doLogEntry(String msisdn, String sessionId, String entryInfo, String qualified) {
        EntriesLog doLookUp = null;
        try 
        {
            
             doLookUp = doLookUp(sessionId, msisdn);
             
             if(doLookUp == null)
             {
                 EntriesLog newEntry = new EntriesLog();
                 newEntry.msisdn = msisdn;
                 newEntry.sessionId = sessionId;
                 newEntry.entryInfo = entryInfo;
                 newEntry.entryQualification = qualified;
                 newEntry.status = 0l;
                 newEntry.entrieDate = LocalDateTime.now();
                
                 doLookUp = Panache.getEntityManager().merge(newEntry);
                 
             }
            
        } 
        catch (Exception e) {
        
            LOGGER.error(" -- Exception -@ EntriesLog @ -doLog ",e);
        
        }
        
     return doLookUp;
    }
    
    @Transactional
    public static SessionsLog doSync(USSDRequestObject requestObj) {
        SessionsLog doLookUp = null;
        try 
        {
            
        } 
        catch (Exception e) {
        
            e.printStackTrace();
        
        }
       return doLookUp; 
    }
    
    public static EntriesLog doLookUp(String sessionId, String msisdn) {
        EntriesLog doLog = null;
        try 
        {
            doLog = find("sessionId = ?1 and msisdn = ?2",sessionId,msisdn).firstResult();
        } 
        catch (Exception e) {
        
            
            LOGGER.error("Exception ERROR @ EntriesLog doLookUp",e);
        
        }
        
      return doLog;
    }

   
}
