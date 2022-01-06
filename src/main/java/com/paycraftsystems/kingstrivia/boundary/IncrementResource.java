/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.boundary;

import com.paycraftsystems.kingstrivia.connectors.RedisConnectors;
import com.paycraftsystems.kingstrivia.dto.EntryLog;
import com.paycraftsystems.kingstrivia.dto.Increment;
import com.paycraftsystems.kingstrivia.dto.QuestionInfo;
import com.paycraftsystems.kingstrivia.dto.QuizEntry;
import io.smallrye.mutiny.Uni;
import java.util.List;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author paycraftsystems-i
 */
@Path("/increments")
public class IncrementResource {

    @Inject
    RedisConnectors service;

    @GET
    public Uni<List<String>> keys() {
        return service.keys();
    }

    @POST
    public JsonObject create(String key, JsonObject jsonInfo) {
        service.doSave(key, jsonInfo);
        return jsonInfo;
    }
    
    
    @GET
    @Path("question/{key}")
    public QuestionInfo getQuestionInfo(@PathParam("key") String key) {
        return new QuestionInfo(Long.parseLong(key.trim()), service.getJsonObject(key));
    }
    
    @POST
    @Path("save-entryinfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doStoreEntryInfo(JsonObject json) {
        
        try 
        {
                 EntryLog entry =  JsonbBuilder.create().fromJson(json.toString(),EntryLog.class);
                 System.out.println("entry = " + entry);
                 
                 service.doSaveGameEntry(entry.sessionKey,entry.getLastInput());
        } 
        catch (Exception e) {
        
            
            e.printStackTrace();
        
        }
       
       return Response.ok().build();// QuestionInfo(Long.parseLong(key.trim()), service.getJsonObject(key));
    }
    
    @GET
    @Path("lastentry/{key}")
    public Response getLastEntry(@PathParam("key") String key) {
        
        try 
        {
            String get = service.getGameEntryInfo(key);
            
            System.out.println("---key "+key+" get = " + get);
            
            return Response.ok().entity(get).build();
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
       return null;// service.get(key);
    }
    
    
    
    
    @POST
    @Path("save-last-question")
    public Response doStoreLastQuestion(JsonObject json) {
        
        try 
        {
                 QuizEntry entry =  JsonbBuilder.create().fromJson(json.toString(),QuizEntry.class);
                 System.out.println("entry = " + entry);
                 service.doSave(entry.sessionKey, String.valueOf(entry.questionNo));
        } 
        catch (Exception e) {
        
            
            e.printStackTrace();
        
        }
       
       return Response.ok().build();// QuestionInfo(Long.parseLong(key.trim()), service.getJsonObject(key));
    }
    
    @GET
    @Path("qinfo/{key}")
    public Response getQInfo(@PathParam("key") String key) {
        
        try 
        {
            String get = service.getQuestionNumber(key);
            
            System.out.println("---key "+key+" get = " + get);
            
            return Response.ok().entity(get).build();
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
       return null;// service.get(key);
    }
    
    /*
    @POST
    @Path("")
    public Increment create(Increment increment) {
        service.set(increment.key, increment.value);
        return increment;
    }

    @GET
    @Path("/{key}")
    public Increment get(@PathParam("key") String key) {
        return new Increment(key, Integer.valueOf(service.get(key)));
    }
   
    @PUT
    @Path("/{key}")
    public void increment(@PathParam("key") String key, Integer value) {
        service.increment(key, value);
    }
    */
    @DELETE
    @Path("/{key}")
    public Uni<Void> delete(@PathParam("key") String key) {
        return service.del(key);
    }
}
