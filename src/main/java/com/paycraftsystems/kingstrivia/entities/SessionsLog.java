/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.entities;

import com.paycraftsystems.kingstrivia.dto.USSDRequestObject;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Table(name="session_log")
public class SessionsLog extends PanacheEntityBase implements Serializable{
    
    private static Logger LOGGER  = LoggerFactory.getLogger(SessionsLog.class);
    
    @Id
    @GeneratedValue
    @Column(name="tid")
    public long tid;
    
    @Column(name="session_id")
    public String sessionId;
    
    @Column(name="msisdn")
    public String msisdn;
    
    @Column(name="last_command")
    public String lastCommand;
    
    @Column(name="message")
    public String message;
    
    @Column(name="session_date")
    public LocalDateTime sessionDate;
    
    @Column(name="last_session_date")
    public LocalDateTime lastSessionDate;
    
    @Column(name="status")
    public long status;
    
    @Column(name="session_duration_millis")
    public long sessionDurationMillis;
    
   
    @Transactional
    public static SessionsLog doLog(USSDRequestObject requestObj, long duration) {
        SessionsLog doLookUp = null;
        try 
        {
            
             doLookUp = doLookUp(requestObj.sessionId, requestObj.msisdn);
             
             if(doLookUp == null)
             {
                 SessionsLog newSessionid = new SessionsLog();
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
                
                 doLookUp.lastCommand = (doLookUp.lastCommand.replaceAll("#", "")+"*"+requestObj.message.replaceAll("#", "").replaceAll("\\*", "")+"#").replaceAll("\\*#", "");
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
    
    public static SessionsLog doLookUp(String sessionId, String msisdn) {
        SessionsLog doLog = null;
        try 
        {
            doLog = find("sessionId = ?1 and msisdn = ?2",sessionId,msisdn).firstResult();
        } 
        catch (Exception e) {
        
            
            LOGGER.error("Exception ERROR @ doLookUp",e);
        
        }
        
      return doLog;
    }

    @Override
    public String toString() {
        return "SessionsLog{" + "tid=" + tid + ", sessionId=" + sessionId + ", msisdn=" + msisdn + ", lastCommand=" + lastCommand + ", message=" + message + ", sessionDate=" + sessionDate + ", status=" + status + ", sessionDurationMillis=" + sessionDurationMillis + '}';
    }
   
}
