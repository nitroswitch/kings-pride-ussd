/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paycraftsystems.kingstrivia.connectors;

import com.paycraftsystems.kingstrivia.controllers.RequestProcessor;
import io.quarkus.redis.client.RedisClient;
import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Uni;

import io.vertx.mutiny.redis.client.Response;
//import io.vertx.redis.client.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.JsonbBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author paycraftsystems-i
 */

@Singleton
public class RedisConnectors {
    
     private  static Logger LOGGER =  LoggerFactory.getLogger(RedisConnectors.class);
  
    
    
    @Inject
    RedisClient redisClient;
    
     
    @Inject
    ReactiveRedisClient reactiveRedisClient; 
    
    
    public String get(String key) {
        io.vertx.redis.client.Response resp = redisClient.get(key);
        //LOGGER.info("--##- resp = " + resp.toString());
       
        return resp ==null?"NA":resp.get(key).toString();//redisClient.get(key).toString();
    }
    
    public String getQuestionNumber(String key) throws Exception{
        io.vertx.redis.client.Response resp = redisClient.get(key);
        //LOGGER.info("--@@- resp = " + (resp !=null?resp.toString():"-2"));
        return (resp !=null?resp.toString():"-2");
    }
    
    public JsonObject getJsonObject(String key) {
        io.vertx.redis.client.Response resp = redisClient.get(key);
        //LOGGER.info("---!! resp = " + (resp !=null?resp.toString():"-2"));
        
        JsonObject jsonObject = resp !=null?JsonbBuilder.create().fromJson(resp.toString(), JsonObject.class):Json.createObjectBuilder().build();
        LOGGER.info("--**- resp jsonObject = " + jsonObject);
        
        return jsonObject;// resp ==null?"NA":resp.get(key).toString();//redisClient.get(key).toString();
    }
    
    
    public String getQuestionsValidAnswer(String key) {
        io.vertx.redis.client.Response resp = redisClient.get(key);
        //LOGGER.info("--- resp = " + (resp !=null?resp.toString():"-2"));
        
        return  (resp !=null?resp.toString():"-2");// resp ==null?"NA":resp.get(key).toString();//redisClient.get(key).toString();
    }

    public void doSave(String key, JsonObject value) {
        redisClient.set(Arrays.asList(key, value.toString()));
    }
    
    public void doSaveQuestionAndAnswer(String key, String value) {
        redisClient.set(Arrays.asList(key, value));
    }
    
    //The question is passed with the language code
    public void doSaveQuestionAndAnswerByLangauge(String key, String value) {
        redisClient.set(Arrays.asList(key, value));
    }
    
    public void doSave(String key, String value) {
        redisClient.append(key, value);
    }
    
    public void doSaveGameEntry(String key, String value) {
        //System.out.println("- key "+key+" - value = " + value);
        redisClient.append(key, value);
    }
    
    public String getGameEntryInfo(String key) {
       io.vertx.redis.client.Response resp =  redisClient.get(key);
       
          System.out.println("resp = " + resp);
          
          
        return (resp !=null)? resp.toString():"NOT-FOUND";
    }
    
    
    

    /*
     public void set(String key, Integer value) {
        redisClient.set(Arrays.asList(key, value.toString()));
    }

    public void increment(String key, Integer incrementBy) {
        redisClient.incrby(key, incrementBy.toString());
    }
    */
   

   public Uni<Void> del(String key) {
        return reactiveRedisClient.del(Arrays.asList(key))
                .map(response -> null);
    }

    

    public Uni<List<String>> keys() {
        return reactiveRedisClient
                .keys("*")
                .map(response -> {
                    List<String> result = new ArrayList<>();
                    for (Response r : response) {
                        result.add(r.toString());
                    }
                    return result;
                });
    }
}
    

