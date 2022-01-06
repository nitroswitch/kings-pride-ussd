package com.paycraftsystems.kingstrivia.boundary;


import com.paycraftsystems.kingstrivia.dto.StaticMenuInterface;
import com.paycraftsystems.kingstrivia.controllers.RequestProcessor;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/processor")
public class USSDResource {
    
    
   private  static Logger LOGGER =  LoggerFactory.getLogger(USSDResource.class);
   
    @Inject
    RequestProcessor requestProcessor;

    /*
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }
    */
    
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String doProcess(@Valid @Context UriInfo info) {
        String resp = "";
        try 
        {
          
            
            LocalDateTime start = LocalDateTime.now();
            
            //LOGGER.info(" @--doProcess info --  "+start);
           
            
            resp = requestProcessor.doProcessRequest(info);
            
            
            LocalDateTime end = LocalDateTime.now();
            
           // LOGGER.info("@-done processing  = start " + start);
           // LOGGER.info("@-done processing  =   end " + end);
            
            long until = start.until(end, ChronoUnit.MILLIS);
            
             LOGGER.info(" - processed in = " + until+" millis ");
          
        } 
        catch (Exception e) {
        
            resp = StaticMenuInterface.EXIT_ON_ERROR;
        }
     
     return resp;
    }
}