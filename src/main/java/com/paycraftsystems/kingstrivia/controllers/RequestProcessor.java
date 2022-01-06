/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.controllers;

import com.paycraftsystems.kingstrivia.connectors.RedisConnectors;
import com.paycraftsystems.kingstrivia.dto.QuestionInfo;
import com.paycraftsystems.kingstrivia.dto.StaticMenuInterface;
import com.paycraftsystems.kingstrivia.dto.USSDRequestObject;
import com.paycraftsystems.kingstrivia.entities.AnswersLog;
import com.paycraftsystems.kingstrivia.entities.EntriesLog;
import com.paycraftsystems.kingstrivia.entities.QuestionsLog;
import com.paycraftsystems.kingstrivia.entities.SessionsLog;
import com.paycraftsystems.kingstrivia.resources.CommunicationCommands;
import com.paycraftsystems.kingstrivia.resources.ResourceHelper;
import io.quarkus.runtime.Startup;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author paycraftsystems-i
 */

@Startup
@ApplicationScoped
public class RequestProcessor {
    
    private  static Logger LOGGER =  LoggerFactory.getLogger(RequestProcessor.class);
  
    
    @Inject
    RedisConnectors redis;
    
    ResourceHelper rh = new ResourceHelper();
    
    
    @PostConstruct
    public void doInit() {
        LOGGER.info("-- Called doInit ");
        String keypad = "";
        try 
        {
            
            
            
            List<QuestionsLog> doLoadQuestionAndValidAnswers = QuestionsLog.doLoadQuestionAndValidAnswers();
            
             for (QuestionsLog doLoadQuestionAnswer : doLoadQuestionAndValidAnswers) {
                 
                 keypad = String.valueOf(doLoadQuestionAnswer.questLanguage)+""+String.valueOf(doLoadQuestionAnswer.tid);
                 LOGGER.info(" ==  ADDING ==   KEYPAD == "+keypad+"   id "+doLoadQuestionAnswer.tid+" doLoadQuestionAnswer "+doLoadQuestionAnswer.validOption);
                // redis.doSaveQuestionAndAnswer(String.valueOf(doLoadQuestionAnswer.tid), doLoadQuestionAnswer.validOption);
                
                 redis.doSaveQuestionAndAnswerByLangauge(keypad, doLoadQuestionAnswer.validOption);
             
             
            }
           
            
            LOGGER.info("-- Called doInit  all good");
            
           
        } 
        catch (Exception e) {
        
            LOGGER.error("Exception - doInit()- ",e);
        }
        
    }
    
   
    private String doProcess(String control, String msisdn, String sessionId, String msg) {
        long option = -1;
        long category = -1;
        long langauge = -1;
        String resp = "";
        int step = 0;
        try 
        {
                LOGGER.info("  -doProcess - "+control+" --len -- "+control.trim().length());
                
                //  landing page
                if(control.trim().length() == 6 && control.equals("*3020#"))
                {
                    resp = StaticMenuInterface.WELCOME;   
                }
                
                if(control.trim().length() > 6)
                { 
                    langauge = ResourceHelper.doExtractLanguage(control); 
                }
                //main menu
                if(control.trim().length() == 8)
                {
                    resp = doLoadMainByLanguage(control);
                }
                
                // display selected category info with option to start
                if(control.trim().length() == 10)
                { 
                   resp = doLoadSelectMenuInfo(control); 
                  
                }
                
                // opt in or out  Q1
                if(control.trim().length() == 12)
                {
                   category = ResourceHelper.doExtractCategory(control); 
                   step = 1;
                   resp = doLoadFirstQuestionOrExit(control,  category, langauge, step,  msisdn,  sessionId,  msg);
                   
                   LOGGER.info(" @@@ s>>>>  "+ResourceHelper.doLanguageDesc(Integer.parseInt(""+langauge)).toUpperCase()+" category check -->>> = " +  ResourceHelper.doCaetoryDesc(Integer.parseInt(""+category)).toUpperCase());
                
                   
                   
                }
                // answer Q1
                if(control.trim().length() == 14)
                {
                    
                   option = ResourceHelper.doExtractAnswer(control, 5);
                   step = 1;
                   resp = doProcessAnswers(langauge, step,  msisdn,  sessionId,  msg,  ""+option);
                    
                }
                
                //  page switch option and load question 2 or exit
                if(control.trim().length() == 16)
                {
                    
                   category = ResourceHelper.doExtractCategory(control);
                   option = ResourceHelper.doExtractAnswer(control,6);
                   step = 2;
                   resp = doProcessPageSwitch(langauge, step,  msisdn,  sessionId,  msg,  ""+option, category);
               
                }
                
                 // answer question Q 2
                if(control.trim().length() == 18)
                {
                    
                   option = ResourceHelper.doExtractAnswer(control, 7);
                   step = 2;
                   resp = doProcessAnswers(langauge,step,  msisdn,  sessionId,  msg,  ""+option);
                }
                
                 // load question 3 page load decision
                if(control.trim().length() == 20)
                {
                   
                   category = ResourceHelper.doExtractCategory(control);
                   option = ResourceHelper.doExtractAnswer(control,8);
                   step = 3;
                   resp = doProcessPageSwitch(langauge, step,  msisdn,  sessionId,  msg,  ""+option, category);
                  
                }
                
                 // answer question 3
                if(control.trim().length() == 22)
                {
                    
                   option = ResourceHelper.doExtractAnswer(control, 9); // ResourceHelper.doExtractAnswer(control, 8); 
                   step = 3;
                   resp = doProcessAnswers(langauge, step,  msisdn,  sessionId,  msg,  ""+option);
                }
                
                //sLOGGER.info(" >>>> category check -->>> = " +  ResourceHelper.doCaetoryDesc(Integer.parseInt(""+ResourceHelper.doExtractCategory(control))).toUpperCase());
                
            
        } 
        catch (Exception e) {
        
            
            resp = CommunicationCommands.END+"Error encountered, please try again";
        
        }
        
        
        return resp;
    }
    
    
    public String doProcessRequest(@Context UriInfo info) {
        String resp = "";
        try 
        {
            String msisdn = info.getQueryParameters().getFirst("msisdn");
            String shortcode = info.getQueryParameters().getFirst("shortcode");
            String message = info.getQueryParameters().getFirst("message");
            String sessionId = info.getQueryParameters().getFirst("sessionid");
            String opcode = info.getQueryParameters().getFirst("opcode");
            long doLanguage = -1;
            /*
            LOGGER.info("msisdn = " + msisdn);
            LOGGER.info("shortcode = " + shortcode);
            LOGGER.info("message = " + message);
            LOGGER.info("sessionid = " + sessionId);
            LOGGER.info("opcode = " + opcode);
            */
            
            // USSDRequestObject(String msisdn, String message, String sessionId,  String optype, String servicecode, String opcode)
            USSDRequestObject ussdRequestObject = new USSDRequestObject(msisdn,  message,  sessionId,  "NA", "NA", opcode);
            //LOGGER.info(" @-- ussdRequestObject --  "+ussdRequestObject);
            if(ussdRequestObject !=null)
            {
                
                SessionsLog doLog = SessionsLog.doLog(ussdRequestObject, 0);
                
                LOGGER.info(" @--SessionsLog doLog -- " + doLog);
                
                if(doLog !=null)
                {
                    //if last command is not null and is not an exit command
                    if(doLog.lastCommand !=null && !doLog.lastCommand.endsWith("*00#"))
                    {  
                         resp = doProcess(doLog.lastCommand, msisdn,sessionId,message);
                    }  //if last command is not null and is an exit command
                    else if(doLog.lastCommand !=null && doLog.lastCommand.endsWith("*00#"))
                    {
                         doLanguage = ResourceHelper.doExtractLanguage(doLog.lastCommand);
                         //public static String WELCOME = CommunicationCommands.CON+"Welcome to Kings Trivial Game"+NEW_LINE+""+NEW_LINE+"Please select prefered language"+NEW_LINE+"1.English"+NEW_LINE+"2.Yoruba"+NEW_LINE+"3.Hausa"+NEW_LINE+"4.Igbo"+NEW_LINE+"5.Exit";
                         LOGGER.info(" -- LANGUAGE -- "+doLanguage);
                         if(doLanguage == 1)//english
                         {
                             resp = StaticMenuInterface.YOU_CHOOSE_EXIT;
                         }
                         else if(doLanguage == 2) // yoruba
                         {
                             resp = StaticMenuInterface.YOU_CHOOSE_EXIT_YORUBA;
                         }
                         else if(doLanguage == 3) // hausa
                         {
                              resp = StaticMenuInterface.YOU_CHOOSE_EXIT_HAUSA;
                         }
                         else if(doLanguage == 4) // ibo
                         {
                             
                         }
                         else
                         {
                             // error exit in enqlish
                             resp = StaticMenuInterface.YOU_CHOOSE_EXIT;
                         }
                    }
                    else   //else return errors
                    {
                        resp = StaticMenuInterface.EXIT_ON_ERROR;
                    }
                   
                }
                else
                {
                    resp = StaticMenuInterface.EXIT_ON_ERROR;
                }
            
            
            }
            else
            {
                resp = StaticMenuInterface.EXIT_ON_ERROR;
            }
        
        
        } 
        catch (Exception e) {
        
             LOGGER.error("ERROR --Exception @  doProcessRequest -- ",e);
             resp = StaticMenuInterface.EXIT_ON_ERROR;
        }
       
     return resp;
    }
    
    
    /*
    public String doLoadQuestionAndAnswers(long questionCategory, int step, String msisdn, String sessionId, String message) {
        LOGGER.info(" doLoadQuestionAndAnswers to Load "+questionCategory+" -- step -- "+step);
        String questionAndAnswers  = "";
        try 
        {
            long question =  doAutoSelectQuestion(questionCategory);
            
             //LOGGER.info(" Question to Load "+question);
             
             questionAndAnswers = QuestionsLog.doLoadQuestionAndAnswers(question);
           
             LOGGER.info(" Question "+step+" -- Session KEY -- "+msisdn+"-"+sessionId+"-"+step);
               
            // save question info
            
             redis.doSave(msisdn+"-"+sessionId+"-"+step, String.valueOf(question));
           
        } 
        catch (Exception e) {
        
             LOGGER.info(" Exception @ doLoadQuestAndAnswers ",e);
        
        }
        
       return questionAndAnswers;
    }*/
    
    public String doLoadQuestionAndAnswers(long questionCategory, long languageId, int step, String msisdn, String sessionId, String message) {
        LOGGER.info(" -- doLoadQuestionAndAnswers to Load "+questionCategory+" -- step -- "+step+" -- languageId --- "+languageId);
        String questionAndAnswers  = "";
        try 
        {
            long question =  doAutoSelectQuestion(questionCategory,languageId);
            
             //LOGGER.info(" Question to Load "+question);
             
             questionAndAnswers = QuestionsLog.doLoadQuestionAndAnswers(question, languageId);
           
             LOGGER.info(" Question "+step+" -- Session KEY -- "+msisdn+"-"+sessionId+"-"+step);
               
            // save question info
            
             redis.doSave(msisdn+"-"+sessionId+"-"+step, String.valueOf(question));
          
        } 
        catch (Exception e) {
        
             LOGGER.info(" Exception @ doLoadQuestAndAnswers ",e);
        
        }
        
       return questionAndAnswers;
    }
    
    
    public String doProcessAnswers(long languageId, int step, String msisdn, String sessionId, String message, String playerOption) {
        LOGGER.info(" @@@-- doProcessAnswers  langugeId("+languageId+") to Load Question "+step+" = msisdn = "+msisdn+" --sessionId --  "+sessionId+" playerOption -- "+playerOption);
        String answer  = "";
        String resp = "";
        String sessionKey = "";
        String entrySessionKey = "";
        String questionAnswerInfo = "";
        try 
        {
             entrySessionKey = msisdn+"-"+sessionId;
             sessionKey = msisdn+"-"+sessionId+"-"+step;
             //LOGGER.info(" = doProcessAnswers= Question "+step+" sessionKey == "+sessionKey);
            
             String question =  redis.getQuestionNumber(sessionKey);
            
             LOGGER.info(" retrieved Question # "+question+" to Load ");
             
             questionAnswerInfo +=question;
             
             question = String.valueOf(languageId)+question;
             
             //LOGGER.info(" @@-- padded @@@s -->>>> "+question);
             
             answer = redis.getQuestionsValidAnswer(question);// QuestionsLog.doLoadQuestionAndAnswers(question);
             
             LOGGER.info(" @@-- answerJson @@@s -- "+answer);
             
              questionAnswerInfo +="*"+answer;
            
             if(step == 1 || step ==2 ) // || step ==3
             {
                 resp =  rh.doAnswerNotification(languageId,playerOption, answer);
                 
                 if(resp !=null && (resp.trim().startsWith("Yay!") ||  resp.trim().startsWith("O kare!") || resp.trim().startsWith("Ma sha Allah!")))  
                 {
                      questionAnswerInfo +="*1+";
                 }
                 else
                 {
                      questionAnswerInfo +="*0+";
                 }
                 
                  LOGGER.info(" -- @@-- "+entrySessionKey+" questionAnswerInfo @@@s -- "+questionAnswerInfo);
                 
                 redis.doSaveGameEntry(entrySessionKey, questionAnswerInfo);
                
             }
             else
             {
                 resp =  rh.doAnswerNotification(languageId,playerOption, answer);
                 
                 if(resp !=null && (resp.trim().startsWith("Yay!") ||  resp.trim().startsWith("O kare!") || resp.trim().startsWith("Ma sha Allah!")))
                 {
                      questionAnswerInfo +="*1";
                 }
                 else
                 {
                      questionAnswerInfo +="*0";
                 }
                 
                
                 LOGGER.info("--- entrySessionKey = " + entrySessionKey+" questionAnswerInfo "+questionAnswerInfo);
                 redis.doSaveGameEntry(entrySessionKey, questionAnswerInfo);
                 //System.out.println("--- entrySessionKey = " + entrySessionKey);
                 String gameEntryInfo = redis.getGameEntryInfo(entrySessionKey);
                 
                 //System.out.println("gameEntryInfo = " + gameEntryInfo);
                 
                 resp = doMarkEntry(languageId, gameEntryInfo);
                 
                 
                 //doLogEntry(String msisdn, String sessionId, String entryInfo, String qualified)
                 EntriesLog.doLogEntry(msisdn, sessionId, gameEntryInfo, isQualified(gameEntryInfo));
                 
                  
             }
            
           
             
            
        } 
        catch (Exception e) {
        
             LOGGER.error(" Exception @ doProcessAnswers ",e);
             
             resp = StaticMenuInterface.EXIT_ON_ERROR;
        
        }
        
       return resp;
    }
    
    public String doMarkEntry(long languageId, String entryInfo)
    {
        String  resp = "";
        try 
        {
             String[] split = entryInfo.split("\\+");
             
             if(split.length == 3)
             {
                 if(split[0].endsWith("*1") && split[1].endsWith("*1") && split[2].endsWith("*1"))
                 {
                     resp = StaticMenuInterface.ENTRY_CONFIRMATION_QUALIFY;//  "Yay!, You are correct "+StaticMenuInterface.NEW_LINE+"1. To proceed to the next question "+StaticMenuInterface.NEW_LINE+"00. To Quit";
           
                 }
                 else
                 {
                    
                   if(languageId == 1l) //english
                   {
                       resp = StaticMenuInterface.ENTRY_CONFIRMATION_DID_NOT_QUALIFY;//"Oooh No!, You did not qualify "+StaticMenuInterface.NEW_LINE+"1. To proceed to the next question"+StaticMenuInterface.NEW_LINE;
                   
                   }
                   else  if(languageId == 2l) //yoruba
                   {
                         // nextDisplay = "O kare!, O gbaa "+StaticMenuInterface.NEW_LINE+"1. Lati tesiwaju si ibeere tokan "+StaticMenuInterface.NEW_LINE+"00. Lati Japa";
           
                         resp = StaticMenuInterface.ENTRY_CONFIRMATION_DID_NOT_QUALIFY_YORUBA;
                        
                   }
                   else  if(languageId ==3l) //hausa
                   {
                        
                        resp = StaticMenuInterface.ENTRY_CONFIRMATION_DID_NOT_QUALIFY_HAUSA;
                       // nextDisplay = "Ma sha Allah!, Wannan haka yake."+StaticMenuInterface.NEW_LINE+"1. Domin amsa wani tambayan"+StaticMenuInterface.NEW_LINE+"00. Domin fita";
           
                   }
                   else  if(languageId ==4l) //ibo
                   {
                      
                   }
                 }
             }
        } 
        catch (Exception e) {
        
        }
      return resp;
    }
    
    public String isQualified(String entryInfo)
    {
        String  resp = "";
        try 
        {
             String[] split = entryInfo.split("\\+");
             
             if(split.length == 3)
             {
                 if(split[0].endsWith("*1") && split[1].endsWith("*1") && split[2].endsWith("*1"))
                 {
                     resp = "1";// StaticMenuInterface.ENTRY_CONFIRMATION_QUALIFY;//  "Yay!, You are correct "+StaticMenuInterface.NEW_LINE+"1. To proceed to the next question "+StaticMenuInterface.NEW_LINE+"00. To Quit";
           
                 }
                 else
                 {
                     resp = "0";// StaticMenuInterface.ENTRY_CONFIRMATION_DID_NOT_QUALIFY;//"Oooh No!, You did not qualify "+StaticMenuInterface.NEW_LINE+"1. To proceed to the next question"+StaticMenuInterface.NEW_LINE;
                   
                 }
             }
        } 
        catch (Exception e) {
        
        }
      return resp;
    }
    
    public String doProcessAnswersBk(long langugeId, int step, String msisdn, String sessionId, String message, String playerOption) {
        LOGGER.info(" doProcessAnswers to Load step "+step+" = msisdn = "+msisdn+" --sessionId --  "+sessionId+" playerOption -- "+playerOption);
        String answer  = "";
        String resp = "";
        String sessionKey = "";
        
        try 
        {
             sessionKey = msisdn+"-"+sessionId+"-"+step;
             LOGGER.info(" =doProcessAnswers= sessionKey == "+sessionKey);
            
             String question =  redis.getQuestionNumber(sessionKey);
            
             LOGGER.info(" Question to Load "+question);
             
             JsonObject answerJson = redis.getJsonObject(question);// QuestionsLog.doLoadQuestionAndAnswers(question);
             
             LOGGER.info(" -- answerJson -- "+answerJson);
             
             AnswersLog toAnswerObect = AnswersLog.toAnswerObect(answerJson);
             
             LOGGER.info(" -- toAnswerObect answerJson -- step  "+step);
             
             if(step == 1 || step ==2)
             {
                 resp =  rh.doAnswerNotification(langugeId,playerOption, toAnswerObect.answerOption, false);
             }
             else
             {
                  resp =  rh.doAnswerNotification(langugeId,playerOption, toAnswerObect.answerOption, true);
             }
           
             
            
        } 
        catch (Exception e) {
        
             LOGGER.info(" Exception @ doProcessAnswers ",e);
             
             resp = StaticMenuInterface.EXIT_ON_ERROR;
        
        }
        
       return resp;
    }
    
    
    public String doProcessPageSwitch(long languageId, int step, String msisdn, String sessionId, String message, String playerOption, long category) {
        LOGGER.info(" doProcessPageSwitch to Load step "+step+" = msisdn = "+msisdn+" --sessionId --  "+sessionId+" playerOption -- "+playerOption+" category = "+category+" languageId - "+languageId);
        String answer  = "";
        String resp = "";
        String sessionKey = "";
        
        try 
        {
            
            if("1".equals(playerOption.trim()))
            {
                resp =  doLoadQuestionAndAnswers(category,languageId, step,  msisdn,  sessionId,  message);
               
            }
            else if("00".equals(playerOption.trim()))
            {
                resp = StaticMenuInterface.YOU_CHOOSE_EXIT;
            }
           
        } 
        catch (Exception e) {
        
             LOGGER.info(" Exception @ doProcessPageSwitch ",e);
             
             resp = StaticMenuInterface.EXIT_ON_ERROR;
        
        }
        
       return resp;
    }
    
    
    
    public long doAutoSelectQuestion(long questionCategory, long langaugeId) {
        List<Long> doLookUpByCategory = new ArrayList<>();
         long[] toArray = null;
        try 
        {
            doLookUpByCategory = QuestionsLog.doLookUpByCategory(questionCategory,langaugeId);
            
            toArray = doLookUpByCategory.stream().mapToLong(x->x).toArray();
        } 
        catch (Exception e) {
        
            LOGGER.error(" Exception in doAutoSelectQuestion -- ",e);
        
        }
    //doLookUpByCategory
        return ResourceHelper.getRandom(toArray);
    }
    
     public QuestionInfo getQuestionInfo(@PathParam("key") String key) {
        return new QuestionInfo(Long.parseLong(key.trim()), redis.getJsonObject(key));
    }
     
     private String doLoadMainByLanguage(String control)
    {
        String resp = "";
        try 
        {
               switch(control)
                {
                   
                  
                    //page 1
                    case "*3020*1#": //landing page english
                    resp = StaticMenuInterface.ENGLISH_MENU;
                    break;
                    case "*3020*2#": //landing page yoruba
                    resp = StaticMenuInterface.YORUBA_MENU;
                    break;
                    case "*3020*3#": //landing page hausa
                    resp = StaticMenuInterface.HAUSA_MENU;
                    break;
                    case "*3020*4#": //landing page ibo
                    resp = StaticMenuInterface.IBO_MENU;
                    break;
                    default:
                    resp = StaticMenuInterface.WELCOME;
                    break;
                        
               }
            
        } catch (Exception e) {
        
            e.printStackTrace();
        }
      return resp;
    }
    
    private String doLoadSelectMenuInfo(String control)
    {
        String resp = "";
        try 
        {
               switch(control)
                {
                   
                   //*3020*2*1#
                    //page 1
                    case "*3020*1*1#": //landing page english
                    resp = StaticMenuInterface.ENGLISH_MENU_CURRENT_AFFAIRS;
                    break;
                    case "*3020*1*2#": //landing page yoruba
                    resp = StaticMenuInterface.ENGLISH_MENU_SPORTS;
                    break;
                    case "*3020*1*3#": //landing page hausa
                    resp = StaticMenuInterface.ENGLISH_MENU_ENTERTAINMENT;
                    break;
                    case "*3020*1*4#": //landing page ibo
                    resp = StaticMenuInterface.ENGLISH_MENU_ISLAMIC;
                    break;
                    case "*3020*1*5#": //landing page ibo
                    resp = StaticMenuInterface.ENGLISH_MENU_BIBLE;
                    break;
                    
                    //page 1 Yoruba
                    case "*3020*2*1#": //landing page english
                    resp = StaticMenuInterface.YORUBA_MENU_CURRENT_AFFAIRS;
                    break;
                    case "*3020*2*2#": //landing page yoruba
                    resp = StaticMenuInterface.YORUBA_MENU_SPORTS;
                    break;
                    case "*3020*2*3#": //landing page hausa
                    resp = StaticMenuInterface.YORUBA_MENU_ENTERTAINMENT;
                    break;
                    case "*3020*2*4#": //landing page ibo
                    resp = StaticMenuInterface.YORUBA_MENU_ISLAMIC;
                    break;
                    case "*3020*2*5#": //landing page ibo
                    resp = StaticMenuInterface.YORUBA_MENU_BIBLE;
                    break;
                    
                     //page 1 hausa
                    case "*3020*3*1#": //landing page english
                    resp = StaticMenuInterface.HAUSA_MENU_CURRENT_AFFAIRS;
                    break;
                    case "*3020*3*2#": //landing page yoruba
                    resp = StaticMenuInterface.HAUSA_MENU_SPORTS;
                    break;
                    case "*3020*3*3#": //landing page hausa
                    resp = StaticMenuInterface.HAUSA_MENU_ENTERTAINMENT;
                    break;
                    case "*3020*3*4#": //landing page ibo
                    resp = StaticMenuInterface.HAUSA_MENU_ISLAMIC;
                    break;
                    case "*3020*3*5#": //landing page ibo
                    resp = StaticMenuInterface.HAUSA_MENU_BIBLE;
                    break;
                    
                     //page 1 ibo
                    case "*3020*4*1#": //landing page english
                    resp = StaticMenuInterface.IBO_MENU_CURRENT_AFFAIRS;
                    break;
                    case "*3020*4*2#": //landing page yoruba
                    resp = StaticMenuInterface.IBO_MENU_SPORTS;
                    break;
                    case "*3020*4*3#": //landing page hausa
                    resp = StaticMenuInterface.IBO_MENU_ENTERTAINMENT;
                    break;
                    case "*3020*4*4#": //landing page ibo
                    resp = StaticMenuInterface.IBO_MENU_ISLAMIC;
                    break;
                    case "*3020*4*5#": //landing page ibo
                    resp = StaticMenuInterface.IBO_MENU_BIBLE;
                    break;
                    
                    default: //back to home? or display error
                    resp = StaticMenuInterface.WELCOME;
                    break;
                    
                        
               }
            
        } catch (Exception e) {
        
            e.printStackTrace();
        }
      return resp;
    }
    
    private String doLoadFirstQuestionOrExit(String control,  long option, long languageId, int step, String msisdn, String sessionId,String  msg)
    {
        String resp = "";
        try 
        {
               switch(control)
                {
                   
                     //*3020*2*1*1#
                    //page 1
                    case "*3020*1*1*1#": //landing page english
                    resp = doLoadQuestionAndAnswers(option,languageId, step, msisdn, sessionId, msg);
                    break; //yoruba
                    case "*3020*2*1*1#": //landing page english
                    resp = doLoadQuestionAndAnswers(option, languageId, step, msisdn, sessionId, msg);
                    break; 
                     case "*3020*2*2*1#": //landing page english
                    resp = doLoadQuestionAndAnswers(option, languageId, step, msisdn, sessionId, msg);
                    break; 
                    case "*3020*1*2*1#": //landing page english
                    resp = doLoadQuestionAndAnswers(option, languageId, step, msisdn, sessionId, msg);
                    break; 
                    case "3020*1*3*1*1": //landing page english
                    resp = doLoadQuestionAndAnswers(option, languageId, step, msisdn, sessionId, msg);
                    break; 
                     case "3020*1*4*1*1": //landing page english
                    resp = doLoadQuestionAndAnswers(option, languageId, step, msisdn, sessionId, msg);
                    break;
                    //hausa
                    case "*3020*3*1*1#": //landing page english
                    resp = doLoadQuestionAndAnswers(option, languageId, step, msisdn, sessionId, msg);
                    break; //ibo
                    case "*3020*4*1*1#": //landing page english
                    resp = doLoadQuestionAndAnswers(option, languageId, step, msisdn, sessionId, msg);
                    break;
                    case "*3020*1*1*00#": //landing page english
                    resp = StaticMenuInterface.YOU_CHOOSE_EXIT;
                    
                    
                   
                    default: //back to home? or display error
                    resp = StaticMenuInterface.WELCOME;
                    break;
                    
                        
               }
            
        } catch (Exception e) {
        
            e.printStackTrace();
        }
      return resp;
    }
    
    
}
